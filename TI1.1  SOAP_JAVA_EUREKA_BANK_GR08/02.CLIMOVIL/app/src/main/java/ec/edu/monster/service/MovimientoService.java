package ec.edu.monster.service;

import android.util.Log;


import ec.edu.monster.model.MovimientoModel;
import ec.edu.monster.util.SoapHelper;

import java.util.ArrayList;
import java.util.List;

public class MovimientoService {

    public List<MovimientoModel> obtenerMovimientos(String cuenta) {
        try {
            String soapBody = "<numcuenta>" + cuenta + "</numcuenta>";

            String response = SoapHelper.callWebService("WSMovimiento", "consultarMovimientos", soapBody);

            List<MovimientoModel> movimientos = new ArrayList<>();

            if (response == null || response.isEmpty()) {
                Log.d("MovimientoService", "Empty response for cuenta=" + cuenta);
                return movimientos;
            }

            // Log raw response (helpful for debugging). Remove/comment in production.
            Log.d("MovimientoService", "Raw response: " + response);

            // Normalizar respuesta: eliminar prefijos de namespace (soapenv:, ns2:, ws:, etc.)
            String normalized = response.replaceAll("<(/)?[a-zA-Z0-9]+:", "<$1");
            // remover declaración XML si existe
            normalized = normalized.replaceAll("^\\s*<\\?xml[\\s\\S]*?\\?>", "").trim();

            Log.d("MovimientoService", "Normalized response: " + normalized);

            // Buscamos ocurrencias de tags de movimiento y extraemos campos básicos.
            // Este parser es simple (basado en índices) y funciona con respuestas XML planas.
            int idx = 0;
            while (true) {
                int pos = normalized.indexOf("<numeroMovimiento>", idx);
                if (pos == -1) break;

                MovimientoModel mov = new MovimientoModel();

                mov.setNumeroMovimiento(parseIntTag(normalized, "numeroMovimiento", pos));
                mov.setFechaMovimiento(parseStringTag(normalized, "fechaMovimiento", pos));
                mov.setCodigoTipoMovimiento(parseStringTag(normalized, "codigoTipoMovimiento", pos));
                mov.setTipoDescripcion(parseStringTag(normalized, "tipoDescripcion", pos));

                // importeMovimiento and saldo may come with commas; normalize to dot
                mov.setImporteMovimiento(parseDoubleTag(normalized, "importeMovimiento", pos));
                mov.setCuentaReferencia(parseStringTag(normalized, "cuentaReferencia", pos));
                mov.setSaldo(parseDoubleTag(normalized, "saldo", pos));

                movimientos.add(mov);

                idx = pos + 1;
            }

            return movimientos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Helpers simples para extraer contenido de tags XML a partir de una posición base
    private String parseStringTag(String xml, String tag, int fromIndex) {
        try {
            String open = "<" + tag + ">";
            String close = "</" + tag + ">";
            int s = xml.indexOf(open, fromIndex);
            int e = -1;
            if (s != -1) e = xml.indexOf(close, s + open.length());
            if (s != -1 && e != -1) {
                return xml.substring(s + open.length(), e).trim();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private int parseIntTag(String xml, String tag, int fromIndex) {
        String val = parseStringTag(xml, tag, fromIndex);
        try {
            if (val != null && !val.isEmpty()) return Integer.parseInt(val);
        } catch (Exception ignored) {}
        return 0;
    }

    private double parseDoubleTag(String xml, String tag, int fromIndex) {
        String val = parseStringTag(xml, tag, fromIndex);
        try {
            if (val != null) {
                val = val.replace(".", "").replace(",", "."); // normalizar mil y decimales
                return Double.parseDouble(val);
            }
        } catch (Exception ignored) {}
        return 0.0;
    }
}

