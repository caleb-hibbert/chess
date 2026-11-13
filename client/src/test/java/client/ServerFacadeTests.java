import client.ServerFacade;
import dataaccess.DataAccessException;
import dataaccess.sql.MySqlAuthDAO;
import dataaccess.sql.MySqlUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        this.facade = new TestServerFacade("localhost", Integer.toString(port));

    }


    @BeforeEach
    void initializeTests() throws DataAccessException, ResponseException {
        facade.clear();
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
