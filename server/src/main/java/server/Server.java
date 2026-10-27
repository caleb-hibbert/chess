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
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.delete("/db", this::clear);
        //TODO - make sure game creation logic is good, implement HTTP catch, handler, etc.


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
    private void login(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try{
            LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(request);

            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(result));
        }
        catch (InvalidRequestException e){
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (UnauthorizedException e){
            ctx.status(401);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }

    }

    private void logout(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try{

            String authToken = ctx.header("authorization");
            LogoutRequest request = new LogoutRequest(authToken);
            userService.logout(request);


            ctx.status(200);
            ctx.contentType("application/json");
        }
        catch (InvalidRequestException e){
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (UnauthorizedException e){
            ctx.status(401);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }

    }

    private void clear(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try{
            userService.clear();


            ctx.status(200);
            ctx.contentType("application/json");
        }
        catch(DataAccessException e){
            ctx.status(500);
        }

    }




        public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
