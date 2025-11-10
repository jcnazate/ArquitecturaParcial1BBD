package ec.edu.monster.util;

public class ApiConfig {
    // URL base del servidor RESTful Java
    // Para desarrollo local con emulador Android: http://10.0.2.2:8080/ws_eureka_bank_restful_java/resources/
    // Para dispositivo físico: http://IP_DE_TU_PC:8080/ws_eureka_bank_restful_java/resources/
    // NOTA: 10.0.2.2 es la IP especial del emulador que apunta a localhost de la PC
    public static final String BASE_URL = "http://10.0.2.2:8080/ws_eureka_bank_restful_java/resources/";

    // Endpoints
    public static final String LOGIN = "login";
    public static final String CUENTA = "cuenta/";
    public static final String MOVIMIENTO = "movimiento";

    // Métodos de cuenta
    public static final String DEPOSITO = "deposito";
    public static final String RETIRO = "retiro";
    public static final String TRANSFERENCIA = "transferencia";
}

