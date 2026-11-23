import client.ServerFacade;
import dataaccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import client.ClientUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        String result = clientUI.eval("register caleb mypassword caleb@byu.edu extraArgument");
        assertTrue(result.contains("Incorrect number of arguments entered for Register - use the format"));
        result = clientUI.eval("register notEnoughArguments");
        assertTrue(result.contains("Incorrect number of arguments entered for Register - use the format"));


        clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        result = clientUI.eval("register caleb, mypassword, caleb@byu.edu");
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
    void loginMessageSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("logout");
        String result = clientUI.eval("login caleb mypassword");
        assertTrue(result.contains("You're signed in as"));
    }


    @Test
    void loginFailure() throws ResponseException{
        String result = clientUI.eval("login notEnoughArguments");
        assertTrue(result.contains("Incorrect number of arguments entered for Login - use the format"));
        result = clientUI.eval("login");
        assertTrue(result.contains("Incorrect number of arguments entered for Login - use the format"));

        result = clientUI.eval("login caleb mypassword");
        assertTrue(result.contains("Error: unauthorized"));
        clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        result = clientUI.eval("register caleb, mypassword, caleb@byu.edu");
        assertTrue(result.contains("You need to log out before performing that operation"));
    }


    @Test
    void logoutSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("login caleb mypassword");
        String result = clientUI.eval("logout");
        assertTrue(result.contains("caleb has logged out"));
        result = clientUI.eval("list");
        assertTrue(result.contains("You must sign in before performing that operation"));
    }


    @Test
    void logoutFailure() throws ResponseException{
        String result = clientUI.eval("logout tooManyArgs");
        assertTrue(result.contains("Incorrect number of arguments entered for Logout - use the format"));
        result = clientUI.eval("logout");
        assertTrue(result.contains("You must sign in before performing that operation"));
    }


    @Test
    void createGameSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        String result = clientUI.eval("create mygame");
        assertTrue(result.contains("New game created called"));
        result = clientUI.eval("create mygame");
        assertTrue(result.contains("New game created called"));
    }


    @Test
    void createGameFailure() throws ResponseException{
        String result = clientUI.eval("create mygame extraArgument");
        assertTrue(result.contains("Incorrect number of arguments entered for Create - use the format"));
        result = clientUI.eval("create");
        assertTrue(result.contains("Incorrect number of arguments entered for Create - use the format"));
    }


    @Test
    void listGamesSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        String result = clientUI.eval("list");
        assertTrue(result.contains("List of chess games:"));
        clientUI.eval("create mygame");
        result = clientUI.eval("list");
        assertTrue(result.contains("mygame"));
    }


    @Test
    void listGamesFailure() throws ResponseException{
        String result = clientUI.eval("list extraArgument");
        assertTrue(result.contains("Incorrect number of arguments entered for List - use the format"));
        result = clientUI.eval("list");
        assertTrue(result.contains("You must sign in before performing that operation"));
        clientUI.eval("register caleb mypassword caleb@byu.edu");
    }


    @Test
    void joinGameSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("create mygame");
        String result = clientUI.eval("join white 1");
        assertTrue(result.contains("Joined the game"));
        assertTrue(result.contains("WHITE"));
    }


    @Test
    void joinGameFailure() throws ResponseException{
        String result = clientUI.eval("join white 1 extraArgument");
        assertTrue(result.contains("Incorrect number of arguments entered for Join - use the format"));
        result = clientUI.eval("join 1");
        assertTrue(result.contains("Incorrect number of arguments entered for Join - use the format"));
        result = clientUI.eval("join white");
        assertTrue(result.contains("Incorrect number of arguments entered for Join - use the format"));
        result = clientUI.eval("join");
        assertTrue(result.contains("Incorrect number of arguments entered for Join - use the format"));
        result = clientUI.eval("join white 1");
        assertTrue(result.contains("You must sign in before performing that operation"));

        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("create mygame");
        result = clientUI.eval("join white 999");
        assertTrue(result.contains("The game you tried to join"));
        clientUI.eval("join white 1");
        result = clientUI.eval("join white 1");
        System.out.println(result);
        assertTrue(result.contains("Error: already taken"));
    }


    @Test
    void observeGameSuccess() throws ResponseException{
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("create mygame");
        String result = clientUI.eval("observe 1");
        assertTrue(result.contains("Now observing the game"));

    }



    @Test
    void observeGameFailure() throws ResponseException{
        String result = clientUI.eval("observe 1 extraArgument");
        assertTrue(result.contains("Incorrect number of arguments entered for Observe - use the format"));
        result = clientUI.eval("observe");
        assertTrue(result.contains("Incorrect number of arguments entered for Observe - use the format"));

        // ^ observeGame isn't currently functional, do a check for joining game 999 once it is
    }


    @Test
    void fdsafdsa() throws ResponseException{
        assertDoesNotThrow(() -> clientUI.eval("quit"));
        String input = "help\nquit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        clientUI.run();

        String result = output.toString();
        assertTrue(result.contains("Welcome to 240 chess. Type Help to get started."));
        assertTrue(result.contains("quit"));



    }





    @Test
    void evalBadInputDefaultToHelp() throws ResponseException{
        String result = clientUI.eval("this is an invalid input");
        assertTrue(result.contains("Select an option:"));
    }


    @Test
    void helpSuccessPreLogin() throws ResponseException {
        String result = clientUI.eval("help");
        assertTrue(result.contains("Register"));
        assertTrue(result.contains("Login"));
        assertTrue(result.contains("Help"));
        assertTrue(result.contains("Select an option:"));
    }


    @Test
    void helpSuccessPostLogin() throws ResponseException {
        clientUI.eval("register caleb mypassword caleb@byu.edu");
        clientUI.eval("register caleb, mypassword, caleb@byu.edu");

        String result = clientUI.eval("help");
        assertTrue(result.contains("Logout"));
        assertTrue(result.contains("List"));
        assertTrue(result.contains("Create"));
        assertTrue(result.contains("Join"));
        assertTrue(result.contains("Observe"));
        assertTrue(result.contains("Help"));
        assertTrue(result.contains("Select an option:"));
    }



}
