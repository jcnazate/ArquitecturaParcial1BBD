package ec.edu.monster.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;

public class SoapHelper {

    // ===== Config WCF (.NET) =====
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String BASE_URL =
            "http://10.0.2.2:8733/Design_Time_Addresses/ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts/";

    // Endpoints (SIN /mex y SIN ?wsdl)
    private static final String CUENTA_URL     = BASE_URL + "CuentaServicio/";
    private static final String LOGIN_URL      = BASE_URL + "LoginServicio/";
    private static final String MOVIMIENTO_URL = BASE_URL + "MovimientoServicio/";

    // SOAPAction “base”
    private static final String SA_CUENTA_DEPOSITO = NAMESPACE + "ICuentaServicio/Deposito";
    private static final String SA_LOGIN_AUTH      = NAMESPACE + "ILoginServicio/Auth";           // <-- A mayúscula
    private static final String SA_MOV_CONSULTAR   = NAMESPACE + "IMovimientoServicio/Movimientos"; // <-- nombre real

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType XML = MediaType.parse("text/xml; charset=utf-8");

    // ==========================================================
    // 1) Compatibilidad: callWebService("WSCuenta","cuenta", body)
    // ==========================================================
    public static String callWebService(String serviceName, String methodName, String soapBody) {
        try {
            String svc = serviceName == null ? "" : serviceName.trim().toLowerCase();
            String mappedMethod = methodName == null ? "" : methodName.trim();
            String url;
            String soapAction;

            if (svc.equals("wscuenta") || svc.equals("cuentaservicio")) {
                url = CUENTA_URL;
                // Java antiguo usaba "cuenta" -> en WCF es "Deposito"
                if (mappedMethod.equalsIgnoreCase("cuenta")) mappedMethod = "Deposito";
                // Mantén otros: "ObtenerCuentaPorNumero", etc.
                soapAction = NAMESPACE + "ICuentaServicio/" + mappedMethod;

            } else if (svc.equals("wslogin") || svc.equals("loginservicio")) {
                url = LOGIN_URL;
                // En tu WSDL la operación es "Auth" (A mayúscula)
                mappedMethod = "Auth";
                soapAction = NAMESPACE + "ILoginServicio/" + mappedMethod;

            } else if (svc.equals("wsmovimiento") || svc.equals("movimientoservicio")) {
                url = MOVIMIENTO_URL;
                // La operación expuesta es "Movimientos"
                mappedMethod = "Movimientos";
                soapAction = NAMESPACE + "IMovimientoServicio/" + mappedMethod;

            } else {
                return "ERROR: servicio no reconocido: " + serviceName;
            }

            String fixedBody = sanitizeBody(soapBody);        // ws: -> tem:, limpia nil accidentales
            String envelope  = envelope(mappedMethod, fixedBody);
            return postSoap(url, soapAction, envelope);

        } catch (Exception e) {
            android.util.Log.e("SOAP", "callWebService error", e);
            return null;
        }
    }

    // ==========================================================
    // 2) Métodos específicos (opcional)
    // ==========================================================
    public static String loginAuth(String username, String password) {
        try {
            String body =
                    "<tem:username>" + escape(username) + "</tem:username>" +
                            "<tem:password>" + escape(password) + "</tem:password>";
            return postSoap(LOGIN_URL, SA_LOGIN_AUTH, envelope("Auth", body)); // Auth mayúscula
        } catch (Exception e) { android.util.Log.e("SOAP", "loginAuth", e); return null; }
    }

    public static String cuentaDeposito(String cuenta, String monto, String tipo, String cuentaDestinoNullable) {
        try {
            String dest = (cuentaDestinoNullable == null || cuentaDestinoNullable.trim().isEmpty())
                    ? "<tem:cuentaDestino xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/>"
                    : "<tem:cuentaDestino>" + escape(cuentaDestinoNullable) + "</tem:cuentaDestino>";

            String body =
                    "<tem:cuenta>" + escape(cuenta) + "</tem:cuenta>" +
                            "<tem:monto>" + escape(monto) + "</tem:monto>" +
                            "<tem:tipo>" + escape(tipo) + "</tem:tipo>" +
                            dest;

            return postSoap(CUENTA_URL, SA_CUENTA_DEPOSITO, envelope("Deposito", body));
        } catch (Exception e) { android.util.Log.e("SOAP", "cuentaDeposito", e); return null; }
    }

    public static String movimientoConsultar(String numCuenta) {
        try {
            String body = "<tem:numcuenta>" + escape(numCuenta) + "</tem:numcuenta>";
            return postSoap(MOVIMIENTO_URL, SA_MOV_CONSULTAR, envelope("Movimientos", body)); // nombre real
        } catch (Exception e) { android.util.Log.e("SOAP", "movimientoConsultar", e); return null; }
    }

    // ==========================================================
    // 3) Parser Boolean compatible con Android
    // ==========================================================
    public static boolean parseSoapBoolean(String xml, String methodName) {
        if (xml == null || xml.isEmpty()) return false;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            // En Android, algunas features no existen; ignóralas si fallan
            try { dbf.setFeature("http://xml.org/sax/features/external-general-entities", false); } catch (Throwable ignored) {}
            try { dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false); } catch (Throwable ignored) {}
            try { dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); } catch (Throwable ignored) {}
            // No uses setXIncludeAware en Android antiguo

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

            // Busca <AuthResult>, <DepositoResult>, etc., respetando el case del método
            NodeList n = doc.getElementsByTagNameNS("*", methodName + "Result");
            if (n.getLength() == 0) n = doc.getElementsByTagNameNS("*", "return");
            if (n.getLength() == 0) return false;

            return "true".equalsIgnoreCase(n.item(0).getTextContent().trim());
        } catch (Exception e) {
            android.util.Log.e("SOAP", "parseSoapBoolean error", e);
            return false;
        }
    }

    // ==========================================================
    // 4) Helpers internos
    // ==========================================================
    private static String envelope(String method, String bodyXml) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:tem=\"" + NAMESPACE + "\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<tem:" + method + ">"
                + (bodyXml == null ? "" : bodyXml)
                + "</tem:" + method + ">"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
    }

    private static String postSoap(String url, String soapAction, String envelope) throws IOException {
        // Pequeños fallbacks de SOAPAction por si el servidor compara diferente
        String[] actions = new String[] {
                soapAction,                              // p.ej. http://tempuri.org/ILoginServicio/Auth
                soapAction.replace("ILoginServicio/", ""), // http://tempuri.org/Auth
                soapAction.replace("IMovimientoServicio/", ""),
                soapAction.replace("ICuentaServicio/", "")
        };

        IOException lastIo = null;
        String lastResp = null;

        for (String action : actions) {
            RequestBody body = RequestBody.create(XML, envelope);
            Request req = new Request.Builder()
                    .url(url) // ¡endpoint del servicio, NO ?wsdl!
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("SOAPAction", "\"" + action + "\"")
                    .post(body)
                    .build();
            try (Response resp = client.newCall(req).execute()) {
                lastResp = (resp.body() != null) ? resp.body().string() : null;
                // Si no hay Fault de ActionNotSupported, damos por bueno
                if (lastResp == null || !lastResp.contains("ActionNotSupported")) {
                    return lastResp;
                }
                android.util.Log.w("SOAP", "ActionNotSupported con SOAPAction=" + action + " → probando alternativa…");
            } catch (IOException ex) {
                lastIo = ex;
            }
        }
        if (lastIo != null) throw lastIo;
        return lastResp;
    }

    /** Convierte prefijos viejos y limpia nil accidentales */
    private static String sanitizeBody(String body) {
        if (body == null) return "";
        String fixed = body.replace("ws:", "tem:");
        fixed = fixed.replace("xmlns:ws=\"http://ws.monster.edu.ec/\"", "");
        fixed = fixed.replace("xsi:nil=\"true\"", "");
        fixed = fixed.replace("i:nil=\"true\"", "");
        return fixed;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private SoapHelper() {}
}