package ec.edu.monster.service;

import com.google.gson.Gson;

import ec.edu.monster.model.LoginRequest;
import ec.edu.monster.util.ApiConfig;
import ec.edu.monster.util.OkHttpClientHelper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class LoginService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final Gson gson;

    public LoginService() {
        this.client = OkHttpClientHelper.createUnsafeOkHttpClient();
        this.gson = new Gson();
    }

    public boolean auth(String username, String password) {
        try {
            LoginRequest request = new LoginRequest(username, password);
            String json = gson.toJson(request);

            RequestBody body = RequestBody.create(json, JSON);
            Request httpRequest = new Request.Builder()
                    .url(ApiConfig.BASE_URL + ApiConfig.LOGIN)
                    .post(body)
                    .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string().trim();
                    // El servidor Java retorna un boolean como JSON (true o false)
                    // JAX-RS serializa boolean primitivos directamente sin comillas
                    return Boolean.parseBoolean(responseBody);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

