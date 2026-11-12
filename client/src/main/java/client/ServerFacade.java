package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import dataaccess.*;
import service.RegisterRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.*;
import dataaccess.*;



public class ServerFacade {

    private int port;
    private HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;
    private Gson gson;


    ServerFacade(int port) throws Exception {
        try{
            this.port = port;
            this.serverUrl = "http://localhost:" + port;
            this.client = HttpClient.newHttpClient();
            this.gson = new Gson();

        }
        catch (Exception e){
            throw new Exception("ServerFacade unable to be created");//FIXME - change this later
        }
    }


    public AuthData register(String username, String password, String email) throws Exception{
        var requestData = new RegisterRequest(username, password, email);
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(serverUrl + "/user"))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublisher.ofString(gson.toJson(requestData)));

        var request = buildRequest("POST", "/user", requestData);
        var response = sendRequest(request);
        return han

    }


    //        javalin.post("/user", this::register);



//    public Pet addPet(Pet pet) throws ResponseException {
//        var request = buildRequest("POST", "/pet", pet);
//        var response = sendRequest(request);
//        return handleResponse(response, Pet.class);
//    }


    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }


    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }


    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());//FIXME update exception type
        }
    }


    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}




}
