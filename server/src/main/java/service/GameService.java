package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import java.util.ArrayList;
import java.util.Map;


public class  GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


    private void checkAuthData(String authToken) throws InvalidRequestException, UnauthorizedException, DataAccessException{
        if (authToken == null || authToken.isBlank()){
            throw new InvalidRequestException("Error: bad request");

        }
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    private void checkCreateGameRequestData(CreateGameRequest createGameRequest) throws InvalidRequestException{
        if (createGameRequest == null ||
                createGameRequest.gameName() == null || createGameRequest.gameName().isBlank()){
            throw new InvalidRequestException("Error: bad request");
        }
    }


    public CreateGameResult createGame(CreateGameRequest createGameRequest)throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        checkAuthData(createGameRequest.authToken());
        checkCreateGameRequestData(createGameRequest);
        int newGameID = gameDAO.createGame(createGameRequest.gameName());

        return new CreateGameResult(newGameID);
    }


    public GetGamesResult getGames(GetGamesRequest getGamesRequest) throws InvalidRequestException, DataAccessException{
        checkAuthData(getGamesRequest.authToken());
        Map<Integer, GameData> gameMap = gameDAO.listGames();
        ArrayList<GameData> gameList = new ArrayList<>(gameMap.values());

        return new GetGamesResult(gameList);
    }

    private void checkJoinGameRequestData(JoinGameRequest joinGameRequest) throws InvalidRequestException{
        if (joinGameRequest == null ||
                joinGameRequest.playerColor() == null || joinGameRequest.playerColor().isBlank() ||
                (!"WHITE".equals(joinGameRequest.playerColor()) && !"BLACK".equals(joinGameRequest.playerColor())) ||
                joinGameRequest.gameID() <= 0){
            throw new InvalidRequestException("Error: bad request");
        }
    }


    public void joinGame(JoinGameRequest joinGameRequest) throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        checkAuthData(joinGameRequest.authToken());
        checkJoinGameRequestData(joinGameRequest);
        GameData gameData = gameDAO.getGame(joinGameRequest.gameID());

        if (gameData == null){
            throw new InvalidRequestException("Error: bad request");
        }

        GameData updatedGame;
        String authToken = joinGameRequest.authToken();
        AuthData data = authDAO.getAuth(authToken);
        String username = data.username();

        if (joinGameRequest.playerColor().equals("WHITE")){
            if (gameData.whiteUsername() != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(gameData.gameID(),username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else {
            if (joinGameRequest.playerColor().equals("BLACK") && gameData.blackUsername() != null){
                // ^ having the BLACK check here and using if-else rather than 2 IF's stops IDE from...
                // ...complaining about updateGame not being initialized
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        }
        gameDAO.updateGame(updatedGame);
    }


    public void clear() throws DataAccessException{
        gameDAO.clear();
    }
}
