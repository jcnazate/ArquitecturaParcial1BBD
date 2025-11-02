package ec.edu.monster.service;

import android.util.Log;
import ec.edu.monster.util.SoapHelper;

public class LoginService {

    private static final String TAG = "LOGIN";

    public boolean auth(String username, String password) {
        try {
            // 1) Normaliza y loguea entradas
            String u = username == null ? "" : username.trim();
            String p = password == null ? "" : password.trim();
            Log.d(TAG, "Auth() user=\"" + u + "\"  pass.len=" + p.length());

            // 2) Body con el prefijo correcto (tem:) para WCF
            String soapBody =
                    "<tem:username>" + escape(u) + "</tem:username>" +
                            "<tem:password>" + escape(p) + "</tem:password>";

            Log.d(TAG, "SOAP Body: " + soapBody);

            // 3) Llamada (usa WSLogin/auth → mapeado a ILoginServicio/auth)
            String response = SoapHelper.callWebService("WSLogin", "auth", soapBody);

            // 4) Log de la respuesta cruda (recorta si es muy larga)
            if (response == null) {
                Log.e(TAG, "SOAP-RAW: null");
                return false;
            } else {
                String shortResp = response.length() > 1000 ? response.substring(0, 1000) + "…(trimmed)" : response;
                Log.d(TAG, "SOAP-RAW: " + shortResp);
            }

            // 5) Parseo robusto: <authResult> o <return>
            boolean ok = SoapHelper.parseSoapBoolean(response, "auth");
            Log.d(TAG, "SOAP-PARSED auth=" + ok);

            // 6) Fallback ultra-defensivo: si aparece >true< en cualquier nodo
            if (!ok && response.toLowerCase().contains(">true<")) {
                Log.w(TAG, "Fallback hit: found '>true<' in XML.");
                ok = true;
            }

            return ok;

        } catch (Exception e) {
            Log.e(TAG, "Auth error", e);
            return false;
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
