package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import java.util.Map;
import java.util.HashMap;

import service.*;

public class Server {
    private final Javalin javalin;

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final UserService userService;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.


        javalin.post("/user", this::register);


        javalin.delete("/db", Server::clearDb);// stub that will eventually handle database clear calls
        // ^ the Server::clearDb just means this is still a lambda function, it's using clearDb below, split up for clarity to avoid a huge line/lambda function

    }

    private void register(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try{
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(gson.toJson(result));
        }
        catch (InvalidRequestException e){
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (AlreadyTakenException e){
            ctx.status(403);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));

        }
    }
    private void register(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try{


            
        }
    }

    private static void clearDb(Context ctx){
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
