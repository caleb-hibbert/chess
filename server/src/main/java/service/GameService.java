package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import model.GameData;


public class  GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


    private void checkAuthData(String authToken) throws InvalidRequestException, UnauthorizedException, DataAccessException{// this was copied from UserService, may need to break out into a separate class
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

    public void getGames(GetGamesRequest getGamesRequest){
        //TODO - finish
    }

//    public void joinGame(){
//
//    }

    public void clear() throws DataAccessException{
        gameDAO.clear();
    }
}
