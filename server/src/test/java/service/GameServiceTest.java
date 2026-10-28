package service;

import dataaccess.*;
import model.UserData;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTest {


    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    void initializeTests(){
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
    }



    @Test
    void createGameSuccess() throws DataAccessException{
        // ^ is it bad that this depends on other services like userService for RegisterRequest/Result? How else to add games?
        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("game", registerResult.authToken());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        GameData testGame = gameDAO.getGame(createGameResult.gameID());

        assertNotNull(testGame);
        assertEquals("game", testGame.gameName());
        assertNull(testGame.whiteUsername());
        assertNull(testGame.blackUsername());
    }


    @Test
    void createGameFail() throws DataAccessException{
        assertThrows(UnauthorizedException.class, () -> {
            CreateGameRequest createGameRequest = new CreateGameRequest("game", "1234");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        });

        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        assertThrows(InvalidRequestException.class, () -> {
            CreateGameRequest createGameRequest = new CreateGameRequest("", registerResult.authToken());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        });
        assertThrows(InvalidRequestException.class, () -> {
            CreateGameRequest createGameRequest = new CreateGameRequest("game", "");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        });
    }


    @Test
    void getGamesSuccess() throws DataAccessException{

        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("game", registerResult.authToken());
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest2 = new CreateGameRequest("game2", registerResult.authToken());
        gameService.createGame(createGameRequest2);
        GetGamesResult getGamesResult = gameService.getGames(new GetGamesRequest(createGameRequest.authToken()));

        assertEquals(2, getGamesResult.games().size());
        assertEquals(1, getGamesResult.games().get(0).gameID());
        assertEquals("game2", getGamesResult.games().get(1).gameName());
    }


    @Test
    void getGamesFail() throws DataAccessException{
        assertThrows(UnauthorizedException.class, () -> {
            GetGamesRequest getGamesRequest = new GetGamesRequest("1234");
            GetGamesResult getGamesResult = gameService.getGames(getGamesRequest);
        });
        assertThrows(InvalidRequestException.class, () -> {
            GetGamesRequest getGamesRequest = new GetGamesRequest("");
            GetGamesResult getGamesResult = gameService.getGames(getGamesRequest);
        });
    }


    @Test
    void joinGameSuccess() throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("game", registerResult.authToken());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "WHITE", createGameResult.gameID());
        gameService.joinGame(joinGameRequest);

        assertEquals("caleb", gameDAO.getGame(createGameResult.gameID()).whiteUsername());
    }


    @Test
    void joinGameFail() throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        assertThrows(UnauthorizedException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest("1234", "WHITE", 1);
            gameService.joinGame(joinGameRequest);
        });//join game with authtoken not in db

        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("game", registerResult.authToken());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);


        assertThrows(InvalidRequestException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest("", "WHITE", 1);
            gameService.joinGame(joinGameRequest);
        });//join with empty authToken
        assertThrows(InvalidRequestException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "", 1);
            gameService.joinGame(joinGameRequest);
        });//join with empty color
        assertThrows(InvalidRequestException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "PURPLE", 1);
            gameService.joinGame(joinGameRequest);
        });//join wth invalid color type
        assertThrows(InvalidRequestException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "WHITE", -1);
            gameService.joinGame(joinGameRequest);
        });//join with invalid gameID type (negative)
        assertThrows(InvalidRequestException.class, () -> {
            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "WHITE", 999);
            gameService.joinGame(joinGameRequest);
        });//join gameID that doesn't exist

        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "BLACK", 1);
        gameService.joinGame(joinGameRequest);

        assertThrows(AlreadyTakenException.class, () -> {
            JoinGameRequest joinGameRequest2 = new JoinGameRequest(registerResult.authToken(), "BLACK", 1);
            gameService.joinGame(joinGameRequest2);
        });//join game where requested color is already taken
    }


    @Test
    void clearSuccess() throws DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("game", registerResult.authToken());
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        assertEquals(1, gameDAO.getGame(createGameResult.gameID()).gameID());

        gameService.clear();

        assertNull(gameDAO.getGame(1));
    }
}
