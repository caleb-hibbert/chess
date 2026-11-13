package exception;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {
    private int statusCode;


    public enum Code {
        ServerError,
        ClientError,
    }

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public static ResponseException fromJson(String json, int statusCode) {
        var map = new Gson().fromJson(json, HashMap.class);
        String message = map.get("message").toString();
        return new ResponseException(statusCode, message);
    }


    public int StatusCode(){
        return statusCode;
    }


}