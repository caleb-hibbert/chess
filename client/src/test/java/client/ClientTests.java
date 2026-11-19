import client.ServerFacade;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import client.ClientMain;
import ui.ClientUI;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTests {
    private static  ClientUI clientUI;

    private static Server server;
    private static ServerFacade facade;
    private int port;

    @BeforeAll
    public static void init(){
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);

        clientUI = new ClientUI(facade);
    }


    @BeforeEach
    void initializeTests() throws DataAccessException, ResponseException {
        server.clearWithoutContext();
        clientUI.clear();
    }


    @Test
    void registerSuccess() throws ResponseException{
        assertDoesNotThrow(() -> clientUI.eval("register caleb, mypassword, caleb@byu.edu"));
    }


    @Test
    void registerFailure() throws ResponseException{
        clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        String result = clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        assertTrue(result.contains("You need to log out before performing that operation"));
    }


    @Test
    void registerSuccessMessage() throws ResponseException{
        //String result = clientUI.register("caleb2", "mypassword2", "caleb2@byu.edu");
        String result = clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        assertTrue(result.contains("You've registered a new account and signed in as caleb"));
    }


    @Test
    void loginSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        assertDoesNotThrow(() -> clientUI.eval("login caleb mypassword"));
    }


    @Test
    void loginFailure() throws ResponseException{
        String result = clientUI.eval("login caleb mypassword");
        assertTrue(result.contains("Error: unauthorized"));
        clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        result = clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        assertTrue(result.contains("You need to log out before performing that operation"));
    }


    @Test
    void helpSuccess() throws ResponseException {
        String result = clientUI.eval("help");
        assertTrue(result.contains("Register"));
        assertTrue(result.contains("Login"));
        assertTrue(result.contains("Help"));
        assertTrue(result.contains("Select an option:"));

        //register, then check others


    }



}
