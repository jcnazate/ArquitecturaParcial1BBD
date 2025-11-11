package ec.edu.monster.service;

import android.util.Log;

import ec.edu.monster.model.MovimientoModel;
import ec.edu.monster.util.SoapHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de consulta de movimientos con logging detallado.
 */
public class MovimientoService {

    private static final String TAG = "MOVIMIENTOS";

    public List<MovimientoModel> obtenerMovimientos(String cuenta) {
        long t0 = System.currentTimeMillis();
        List<MovimientoModel> out = new ArrayList<>();

        try {
            // 1) Normaliza y valida la cuenta
            String cta = (cuenta == null) ? "" : cuenta.trim();
            Log.d(TAG, "obtenerMovimientos() cuenta=\"" + cta + "\" len=" + cta.length());
            if (cta.isEmpty()) {
                Log.e(TAG, "Cuenta vacía; se devuelve lista vacía.");
                return out;
            }

            // 2) Body con prefijo correcto para WCF
            String soapBody = "<tem:numcuenta>" + escape(cta) + "</tem:numcuenta>";
            Log.d(TAG, "SOAP Body: " + soapBody);

            // 3) Llamada #1: operación "Movimientos"
            String response = SoapHelper.callWebService("WSMovimiento", "Movimientos", soapBody);
            if (response == null) {
                Log.e(TAG, "SOAP-RAW: null (Movimientos)");
                return out;
            } else {
                String shortResp = response.length() > 1500 ? response.substring(0, 1500) + "…(trimmed)" : response;
                Log.d(TAG, "SOAP-RAW (Movimientos): " + shortResp);
            }

            // 3.1) Fallback si ActionNotSupported → intentar "consultarMovimientos"
            if (response.contains("ActionNotSupported")) {
                Log.w(TAG, "ActionNotSupported con 'Movimientos' → reintentando 'consultarMovimientos'");
                response = SoapHelper.callWebService("WSMovimiento", "consultarMovimientos", soapBody);
                if (response == null) {
                    Log.e(TAG, "SOAP-RAW: null (consultarMovimientos)");
                    return out;
                } else {
                    String shortResp = response.length() > 1500 ? response.substring(0, 1500) + "…(trimmed)" : response;
                    Log.d(TAG, "SOAP-RAW (consultarMovimientos): " + shortResp);
                }
            }

            // 4) Normaliza (quita prefijos de namespace, cabecera xml, envuelve si hace falta)
            String normalized = response
                    .replaceAll("<(/)?[a-zA-Z0-9]+:", "<$1")        // quita prefijos a:, s:, etc.
                    .replaceAll("^\\s*<\\?xml[\\s\\S]*?\\?>", "")   // quita declaración xml
                    .replaceAll("xmlns[^=]*=\"[^\"]*\"", "")        // quita atributos xmlns
                    .trim();
            if (!normalized.startsWith("<root")) {
                normalized = "<root>" + normalized + "</root>";
            }
            Log.d(TAG, "XML normalizado (1.5k): " +
                    normalized.substring(0, Math.min(normalized.length(), 1500)));

            // 5) Parseo con PullParser
            XmlPullParserFactory f = XmlPullParserFactory.newInstance();
            f.setNamespaceAware(false);
            XmlPullParser xpp = f.newPullParser();
            xpp.setInput(new StringReader(normalized));

            MovimientoModel cur = null;
            int itemDepth = -1;
            int event = xpp.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = xpp.getName();

                    if (isFieldTag(name)) {
                        if (cur == null) {
                            cur = new MovimientoModel();
                            itemDepth = xpp.getDepth() - 1; // padre inmediato del campo
                        }

                        String val = safeNextText(xpp).trim();
                        String nameLower = name.toLowerCase();
                        
                        // Usa equalsIgnoreCase para manejar PascalCase y camelCase
                        if ("numeromovimiento".equals(nameLower)) {
                            cur.setNumeroMovimiento(parseInt(val));
                        } else if ("fechamovimiento".equals(nameLower)) {
                            cur.setFechaMovimiento(val);
                        } else if ("codigotipomovimiento".equals(nameLower)) {
                            cur.setCodigoTipoMovimiento(val);
                        } else if ("tipodescripcion".equals(nameLower)) {
                            cur.setTipoDescripcion(val);
                        } else if ("importemovimiento".equals(nameLower)) {
                            cur.setImporteMovimiento(parseDecimal(val));
                        } else if ("cuentareferencia".equals(nameLower)) {
                            cur.setCuentaReferencia(val);
                        } else if ("saldo".equals(nameLower)) {
                            cur.setSaldo(parseDecimal(val));
                        } else {
                            Log.d(TAG, "Campo desconocido: " + name + " = " + val);
                        }

                        // nextText() ya avanzó el cursor al END_TAG del campo
                        event = xpp.getEventType();
                        continue;
                    }

                } else if (event == XmlPullParser.END_TAG) {
                    String tagName = xpp.getName();
                    // Detectar fin de MovimientoModel o cualquier tag que termine con "MovimientoModel"
                    if (cur != null && ("MovimientoModel".equalsIgnoreCase(tagName) || tagName != null && tagName.endsWith("MovimientoModel"))) {
                        if (cur.getNumeroMovimiento() != 0) {
                            out.add(cur);
                            Log.d(TAG, "Movimiento agregado: nro=" + cur.getNumeroMovimiento() + " saldo=" + cur.getSaldo());
                        } else {
                            Log.w(TAG, "Descartado bloque sin numeroMovimiento válido (depth=" + xpp.getDepth() + ", tag=" + tagName + ")");
                        }
                        cur = null;
                        itemDepth = -1;
                    } else if (cur != null && xpp.getDepth() == itemDepth) {
                        // Fallback: si llegamos a la profundidad esperada
                        if (cur.getNumeroMovimiento() != 0) {
                            out.add(cur);
                            Log.d(TAG, "Movimiento agregado (fallback): nro=" + cur.getNumeroMovimiento());
                        } else {
                            Log.w(TAG, "Descartado bloque sin numeroMovimiento (depth=" + itemDepth + ", tag=" + tagName + ")");
                        }
                        cur = null;
                        itemDepth = -1;
                    }
                }

                event = xpp.next();
            }

            // 6) Logs finales
            Log.d(TAG, "Movimientos parseados: " + out.size());
            if (!out.isEmpty()) {
                MovimientoModel first = out.get(0);
                Log.d(TAG, "Primer mov -> nro=" + first.getNumeroMovimiento()
                        + " tipo=" + safe(first.getCodigoTipoMovimiento())
                        + " desc=" + safe(first.getTipoDescripcion())
                        + " imp=" + first.getImporteMovimiento()
                        + " saldo=" + first.getSaldo());
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al obtener movimientos", e);
        } finally {
            Log.d(TAG, "obtenerMovimientos() tardó " + (System.currentTimeMillis() - t0) + " ms");
        }

        return out;
    }

    // =========== Helpers ===========

    private static String escape(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private static String safe(String s) { return (s == null) ? "" : s; }

    private boolean isFieldTag(String name) {
        return "numeroMovimiento".equalsIgnoreCase(name)
                || "fechaMovimiento".equalsIgnoreCase(name)
                || "codigoTipoMovimiento".equalsIgnoreCase(name)
                || "tipoDescripcion".equalsIgnoreCase(name)
                || "importeMovimiento".equalsIgnoreCase(name)
                || "cuentaReferencia".equalsIgnoreCase(name)
                || "saldo".equalsIgnoreCase(name);
    }

    private String safeNextText(XmlPullParser xpp) throws Exception {
        String t = xpp.nextText();
        return (t == null) ? "" : t;
    }

    private int parseInt(String s) {
        try {
            if (s == null) return 0;
            s = s.trim();
            if (s.isEmpty()) return 0;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Parser decimal robusto para formatos con coma o punto. */
    private double parseDecimal(String raw) {
        if (raw == null) return 0.0;
        String s = raw.trim().replaceAll("[^0-9,\\.]", "");
        int lastComma = s.lastIndexOf(',');
        int lastDot   = s.lastIndexOf('.');

        if (lastComma != -1 && lastDot != -1) {
            if (lastComma > lastDot) { s = s.replace(".", "").replace(',', '.'); }
            else { s = s.replace(",", ""); }
        } else if (lastComma != -1) {
            s = s.replace(".", "").replace(',', '.');
        } else {
            s = s.replace(",", "");
        }
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }
}
