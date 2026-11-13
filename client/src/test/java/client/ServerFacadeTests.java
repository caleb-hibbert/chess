import client.ServerFacade;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlUserDAO;
import dataaccess.sql.MySqlGameDAO;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;
import service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.javalin.http.Context;


public class ServerFacadeTests {

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
    }


    @BeforeEach
    void initializeTests() throws DataAccessException {
        server.clearWithoutContext();
    }


    @Test
    void registerSuccess() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertNotNull(authData.authToken());
        assertEquals("caleb", authData.username());
    }


    @Test
    void registerFailure() throws ResponseException {
        facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertThrows(ResponseException.class, () ->{
            facade.register("caleb", "mypassword", "caleb@byu.edu");
        });

        assertThrows(ResponseException.class, () ->{
            facade.register("", "mypassword", "caleb@byu.edu");
        });
        assertThrows(ResponseException.class, () ->{
            facade.register("caleb", "", "caleb@byu.edu");
        });
        assertThrows(ResponseException.class, () ->{
            facade.register("caleb", "mypassword", "");
        });
    }


    @Test
    void loginSuccess() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertDoesNotThrow(() -> facade.login("caleb", "mypassword"));
    }


    @Test
    void loginFailure() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertThrows(ResponseException.class, () ->{
            facade.login("caleb", "badpassword");
        });
    }


    @Test
    void logoutSuccess() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
        assertThrows(ResponseException.class, () ->{
            facade.logout(authData.authToken());
        });
    }


    @Test
    void createGameSuccess() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertDoesNotThrow(() -> facade.createGame("testGame", authData.authToken()));
    }


    @Test
    void createGameFailure() throws ResponseException {
        assertThrows(ResponseException.class, () ->{
            facade.createGame("testGame", "badAuthToken");
        });
    }


    @Test
    void getGamesSuccess() throws ResponseException {
        var authData = facade.register("caleb", "mypassword", "caleb@byu.edu");

        assertDoesNotThrow(() -> facade.getGames(authData.authToken()));
        facade.createGame("testGame", authData.authToken());
        assertEquals(1, facade.getGames(authData.authToken()).games().size());
    }


    @Test
    void getGamesFailure() throws ResponseException {
        assertThrows(ResponseException.class, () ->{
            facade.getGames("badAuthToken");
        });
    }





    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
