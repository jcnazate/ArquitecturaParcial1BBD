package ec.edu.monster.service;

import android.util.Log;
import ec.edu.monster.model.CuentaModel;
import ec.edu.monster.util.SoapHelper;

public class CuentaService {

    public boolean realizarDeposito(String cuenta, String monto, String tipo, String cd) {
        try {
            // Usa el método específico de SoapHelper que maneja correctamente los parámetros y valores null
            String response = SoapHelper.cuentaDeposito(cuenta, monto, tipo, cd);

            if (response != null) {
                // Usa el parser robusto de SoapHelper
                return SoapHelper.parseSoapBoolean(response, "Deposito");
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String TAG = "CUENTA";

    public CuentaModel obtenerCuentaPorNumero(String cuenta) {
        try {
            // Usa el prefijo tem: para WCF
            String soapBody = "<tem:cuenta>" + cuenta + "</tem:cuenta>";

            String response = SoapHelper.callWebService("WSCuenta", "obtenerCuentaPorNumero", soapBody);
            
            if (response != null) {
                Log.d(TAG, "Respuesta obtenerCuentaPorNumero: " + (response.length() > 500 ? response.substring(0, 500) + "..." : response));
                
                CuentaModel cuentaModel = new CuentaModel();

                // Buscar campos con diferentes variaciones (con/sin prefijos, mayúsculas/minúsculas)
                String saldoStr = extractTagContent(response, "decCuenSaldo", "DecCuenSaldo");
                String contadorStr = extractTagContent(response, "intCuenContMov", "IntCuenContMov");
                String codigoStr = extractTagContent(response, "chrCuenCodigo", "ChrCuenCodigo");

                if (saldoStr != null && !saldoStr.isEmpty()) {
                    try {
                        cuentaModel.setDecCuenSaldo(Double.parseDouble(saldoStr));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (contadorStr != null && !contadorStr.isEmpty()) {
                    try {
                        cuentaModel.setIntCuenContMov(Integer.parseInt(contadorStr));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (codigoStr != null && !codigoStr.isEmpty()) {
                    cuentaModel.setStrCuenNumero(codigoStr);
                } else {
                    cuentaModel.setStrCuenNumero(cuenta);
                }

                // Si al menos el saldo fue encontrado, devolver el modelo
                if (saldoStr != null || contadorStr != null || codigoStr != null) {
                    Log.d(TAG, "Cuenta parseada: saldo=" + cuentaModel.getDecCuenSaldo() + 
                          " contador=" + cuentaModel.getIntCuenContMov() + 
                          " cuenta=" + cuentaModel.getStrCuenNumero());
                    return cuentaModel;
                } else {
                    Log.w(TAG, "No se encontraron campos en la respuesta SOAP");
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractTagContent(String xml, String... tagVariations) {
        if (xml == null || tagVariations == null) return null;
        
        // Buscar cada variación del tag (con/sin prefijos, mayúsculas/minúsculas)
        for (String tag : tagVariations) {
            // Buscar con prefijos comunes: <a:tag>, <tag>, etc.
            String[] patterns = {
                "<a:" + tag + ">",
                "<" + tag + ">",
                "<" + tag.toLowerCase() + ">",
                "<" + tag.toUpperCase() + ">"
            };
            
            for (String pattern : patterns) {
                String openTag = pattern;
                String closeTag = openTag.replace("<", "</");
                
                int start = xml.indexOf(openTag);
                if (start != -1) {
                    start += openTag.length();
                    int end = xml.indexOf(closeTag, start);
                    if (end != -1) {
                        String content = xml.substring(start, end).trim();
                        if (!content.isEmpty()) {
                            return content;
                        }
                    }
                }
            }
        }
        return null;
    }
}

