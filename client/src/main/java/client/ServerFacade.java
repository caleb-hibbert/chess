package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import com.google.gson.Gson;
import model.AuthData;
import model.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.*;

import exception.ResponseException;
import model.GetGamesRequest;
import model.GetGamesResult;
import model.JoinGameRequest;


public class ServerFacade {

    //private int port;
    private HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;
    private Gson gson;


    public ServerFacade(int port) {
            //this.port = port;
            this.serverUrl = "http://localhost:" + port;
            this.client = HttpClient.newHttpClient();
            this.gson = new Gson();
    }


    public AuthData register(String username, String password, String email) throws ResponseException{
        var requestData = new RegisterRequest(username, password, email);
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(serverUrl + "/user"))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublisher.ofString(gson.toJson(requestData)));

        var request = buildRequest("POST", "/user", requestData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }// javalin.post("/user", this::register);



    public AuthData login(String username, String password) throws ResponseException{
        var requestData = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", requestData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }// javalin.post("/session", this::login);


    public void logout(String authToken) throws ResponseException {
        var requestData = new LogoutRequest(authToken);
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }// javalin.delete("/session", this::logout);


    public int createGame(String gameName, String authToken) throws ResponseException{
        var requestData = new CreateGameRequest(gameName, authToken);
        var request = buildRequest("POST", "/game", requestData, authToken);
        var response = sendRequest(request);
        var result = handleResponse(response, CreateGameResult.class);
        if (result == null){
            throw new ResponseException(500, "Create game returned an empty response");
        }
        return result.gameID();
    }// javalin.post("/game", this::createGame);


    public GetGamesResult getGames(String authToken) throws ResponseException{
        var requestData = new GetGamesRequest(authToken);
        var request = buildRequest("GET", "/game", requestData, authToken);
        var response = sendRequest(request);
        return handleResponse(response, GetGamesResult.class);
    }// javalin.get("/game", this::getGames);


    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException{
        var requestData = new JoinGameRequest(authToken, playerColor, gameID);
        var request = buildRequest("PUT", "/game", requestData, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }// javalin.put("/game", this::joinGame);


    public void clear() throws ResponseException{
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }// javalin.delete("/db", this::clear);














    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }

        if (authToken != null) {
            request.header("authorization", authToken);
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


    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        }
        catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body, status);
            }

            throw new ResponseException(status, "other failure: " + status);
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





