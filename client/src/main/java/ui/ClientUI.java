package ui;
import client.ServerFacade;
import client.State;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

import static ui.EscapeSequences.*;




public class ClientUI {
    private final ServerFacade facade;
    private State state = State.SIGNEDOUT;
    private String username = null;
    private AuthData authData;
    public static final String RESET_TEXT_COLOR_DEFAULT = "\u001B[0m";


    public ClientUI(ServerFacade facade){
        this.facade = facade;
    }

    public void run(){
        System.out.println("\nWelcome to 240 chess. Type Help to get started.\n");
        System.out.println(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")){
            printPrompt();
            String input = scanner.nextLine().trim();

            try{
                result = eval(input);
                System.out.print(result);
            }
            catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String operation;
            if (tokens.length > 0){
                operation = tokens[0];
            }
            else{
                operation = "help";
            }
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (operation){
                case "register" -> {
                    if (params.length != 3){
                        yield "Incorrect number of arguments entered for register - use the format \"Register <USERNAME> <PASSWORD> <EMAIL>\"";
                    }
                    yield register(params[0], params[1], params[2]);
                }
                case "login" -> {
                    if (params.length != 2){
                        yield "Incorrect number of arguments entered for Login - use the format \"Login <USERNAME> <PASSWORD>\"";
                    }
                    yield login(params[0], params[1]);
                }
                case "logout" -> {
                    if (params.length != 0){
                        yield "Incorrect number of arguments entered for Logout - use the format \"Logout\"";
                    }
                    yield logout();
                }
                case "create" -> {
                    if (params.length != 1){
                        yield "Incorrect number of arguments entered for Create - use the format \"Create <GAME_NAME>\"";
                    }
                    yield createGame(params[0]);
                }
                case "list" -> {
                    if (params.length != 0){
                        yield "Incorrect number of arguments entered for List - use the format \"List\"";
                    }
                    yield listGames();
                }
                case "join" -> {
                    if (params.length != 2){
                        yield "Incorrect number of arguments entered for Join - use the format \"Join <WHITE|BLACK> <GAME_LIST_NUMBER>\"";
                    }
                    yield joinGame(params[0].toUpperCase(), Integer.parseInt(params[1]));
                }
                case "observe" -> {
                    if (params.length != 1){
                        yield "Incorrect number of arguments entered for Observe - use the format \"Observe <GAME_LIST_NUMBER>\"";
                    }
                    yield observeGame(Integer.parseInt(params[0]));
                }

                case "help" -> help();
                case "quit" -> "quit";
                case "clear" -> clear();

                default -> help();
            };
        }
        catch (ResponseException ex){
            return ex.getMessage();
        }
    }


    public String register(String username, String password, String email) throws ResponseException {
        assertSignedOut();
        this.authData = facade.register(username, password, email);
        this.username = username;
        state = State.SIGNEDIN;
        return String.format("You've registered a new account and signed in as %s", username);
    }




    public String login(String username, String password) throws ResponseException {
        assertSignedOut();
        this.authData = facade.login(username, password);
        this.username = username;
        state = State.SIGNEDIN;
        return String.format("You're signed in as %s", username);

    }


    public String logout() throws ResponseException{
        assertSignedIn();
        facade.logout(authData.authToken());
        state = State.SIGNEDOUT;
        return String.format("%s has logged out", username);
    }


    public String createGame(String gameName) throws ResponseException{
        assertSignedIn();
        int gameID = facade.createGame(gameName, authData.authToken());
        return String.format("New game created with ID %d", gameID);
    }


    public String listGames() throws ResponseException{
        assertSignedIn();
        Map<Integer, GameData> mapOfGames = new HashMap<>();

//        List<GameData> listOfGames = facade.getGames(authData.authToken());
//        StringBuilder formattedListOfGames = new StringBuilder();

//        for (GameData game : listOfGames){
//            formattedListOfGames.append("f" + game);
//            formattedListOfGames.append("\n");
//        }

//        for (int i = 0; i < listOfGames.size(); i++){
//            formattedListOfGames.append(i+1).append(". ").append(listOfGames.get(i)).append("\n");
//        }//TODO - would a map work better here?

        return String.format("List of chess games:\n%s", formattedListOfGames);
    }


    public String joinGame(String playerColor, int gameListID) throws ResponseException{
        assertSignedIn();
        List<GameData> listOfGames = facade.getGames(authData.authToken());
        facade.joinGame(authData.authToken(), playerColor, );

        facade.joinGame(authData.authToken(), playerColor, gameListID);
        return String.format("Joined the game with ID %d on the %s team", gameListID, playerColor );

    }


    public String observeGame(int gameListID) throws ResponseException{
        assertSignedIn();
        facade.observeGame(authData.authToken(), gameListID);
        return String.format("Now observing the game with ID %d", gameListID);

    }


    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Register <USERNAME> <PASSWORD> <EMAIL> - Create an account
                    Login <USERNAME> <PASSWORD> - Play or watch chess games
                    Help - Get help with possible commands
                    Quit - Quit the chess program
                    
                    Select an option:
                    """;
        }
        return """
                Logout - Sign out of the Chess program
                List - List all chess games
                Create <GAME_NAME> - Create a new chess game
                Join <WHITE|BLACK> <GAME_LIST_NUMBER> - Join a specific chess game
                Observe <GAME_LIST_NUMBER> - Observe a specific chess game
                Help - Get help with possible commands
                Quit - Quit the chess program
                
                Select an option:
                """;
    }

    public String clear() throws ResponseException {//TODO -For testing, remove this for final submission
        facade.clear();
        return "Databases cleared";
    }


    private void printPrompt() {
        if (state == State.SIGNEDOUT){
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_OUT] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
        else{
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_IN] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
    }


    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(401, "You must sign in before performing that operation");
        }
    }


    private void assertSignedOut() throws ResponseException {
        if (state == State.SIGNEDIN) {
            throw new ResponseException(403, "You need to log out before performing that operation");
        }
    }

}
