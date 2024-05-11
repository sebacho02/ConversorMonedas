import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;

public class ConsultaApi {
    public Rate tasaApi(String tasaInicio, String tasaCambio, Double cantidad) throws
            UnsupportedEncodingException{
        String tasaInicioEncoded = URLEncoder.encode(tasaInicio,"UTF-8");
        String tasaCambioEncoded = URLEncoder.encode(tasaCambio,"UTF-8");

        String direction = "https://v6.exchangerate-api.com/v6/bd5da2170854172d6c82b219/pair/"+tasaInicioEncoded+"/"+tasaCambioEncoded+"/"+cantidad;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direction))
                .build();
        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Rate tasa = gson.fromJson(response.body(), Rate.class);
            return tasa;
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Eleccion Invalida");
        } catch (Exception e){
            throw new RuntimeException("No encontre esa Moneda, Vuelve a intentarlo");
        }
    }

}

