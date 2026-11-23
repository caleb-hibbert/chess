package ui;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.State;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

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
                        yield "Incorrect number of arguments entered for Register - use the format \"Register <USERNAME> <PASSWORD> <EMAIL>\"";
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
                    if (params.length != 2 ){
                        yield "Incorrect number of arguments entered for Join - use the format \"Join <WHITE|BLACK> <GAME_NUMBER>\"";
                    }
                    try{
                        Integer.valueOf(params[1]);
                    }
                    catch (NumberFormatException exception){
                        yield "Incorrect formatting of arguments entered for Join - use the format \"Observe <GAME_NUMBER>\"";
                    }
                    yield joinGame(params[0].toUpperCase(), Integer.parseInt(params[1]));
                }
                case "observe" -> {
                    if (params.length != 1){
                        yield "Incorrect number of arguments entered for Observe - use the format \"Observe <GAME_NUMBER>\"";
                    }
                    try{
                        Integer.valueOf(params[0]);
                    }
                    catch (NumberFormatException exception){
                        yield "Incorrect formatting of arguments entered for Observe - use the format \"Observe <GAME_NUMBER>\"";
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
        return String.format("New game created called %s", gameName);
    }


    public String listGames() throws ResponseException{
        assertSignedIn();
        List<GameData> listOfGames = facade.getGames(authData.authToken());
        StringBuilder formattedListOfGames = new StringBuilder();


        for (int i = 0; i < listOfGames.size(); i++){
            formattedListOfGames.append(i+1).append(". Name: ").append(listOfGames.get(i).gameName())
                    .append(" | White Username: ").append(listOfGames.get(i).whiteUsername())
                    .append(" | Black Username: ").append(listOfGames.get(i).blackUsername()).append("\n");
        }//would a map work better here?

        return String.format("List of chess games:\n%s", formattedListOfGames);
    }


    public String joinGame(String playerColor, int gameListID) throws ResponseException{
        assertSignedIn();
        List<GameData> listOfGames = facade.getGames(authData.authToken());
        try{
            facade.joinGame(authData.authToken(), playerColor, listOfGames.get(gameListID-1).gameID());
        }
        catch(IndexOutOfBoundsException exception){
            return String.format(
                    "The game you tried to join (%d) doesn't exist." +
                            " Valid game numbers currently range from 1 to %d", gameListID, listOfGames.size());
        }
        printBoard(playerColor, gameListID);
        return String.format("Joined the game %s on the %s team", listOfGames.get((gameListID-1)).gameName(), playerColor);
    }


    public String observeGame(int gameListID) throws ResponseException{
        assertSignedIn();
        List<GameData> listOfGames = facade.getGames(authData.authToken());
        try{
            facade.observeGame(authData.authToken(), listOfGames.get(gameListID-1).gameID());

        }
        catch (IndexOutOfBoundsException exception){
            return String.format(
                    "The game you tried to observe (%d) doesn't exist." +
                            " Valid game numbers currently range from 1 to %d", gameListID, listOfGames.size());
        }
        printBoard("WHITE", gameListID);
        return String.format("Now observing the game %s", listOfGames.get((gameListID-1)).gameName());
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

    public String clear() throws ResponseException {// For testing, should this be removed later?
        facade.clear();
        this.username = null;
        this.authData = null;
        this.state = State.SIGNEDOUT;
        return "Databases cleared";
    }


    void printBoard(String teamColor, int gameListID){
        System.out.print(SET_TEXT_COLOR_BLACK);
        List<GameData> listOfGames = new ArrayList<>();
        try{
            listOfGames = facade.getGames(authData.authToken());
        }
        catch (ResponseException ex){
            System.out.print(RESET_TEXT_COLOR);
            System.out.println("Unable to print the requested game, make sure the list ID you selected was valid and formatted properly");
        }
        ChessBoard board = listOfGames.get(gameListID-1).game().getBoard();
        printTopOrBottomRow(teamColor);
        printBoardMainRows(teamColor, board);
        printTopOrBottomRow(teamColor);
        System.out.print(RESET_TEXT_COLOR);
    }


    void printBoardMainRows(String teamColor, ChessBoard board){
        String currentColor;
        int rowStart;
        int rowStop;
        int incrementDirection;

        if (teamColor.equals("WHITE")){
            currentColor = SET_BG_COLOR_WHITE;
            rowStart = 8;
            rowStop = 0;
            incrementDirection = -1;
        }
        else{
            currentColor = SET_BG_COLOR_LIGHT_GREY;
            rowStart = 1;
            rowStop = 8;
            incrementDirection = 1;
        }

        for (int i = rowStart; incrementDirection == -1 ? i > rowStop: i<= rowStop; i+= incrementDirection){
            System.out.print(SET_BG_COLOR_BLUE + " " + i + " ");
            for (int j = 1; j <= 8; j++){
                System.out.print(currentColor + findPieceToPrint(board, i, j));//row, column
                currentColor = swapColor(currentColor);
            }
            System.out.print(SET_BG_COLOR_BLUE + " " + i + "  ");
            currentColor = swapColor(currentColor);
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }


    String swapColor(String currentColor){
        if (currentColor.equals(SET_BG_COLOR_LIGHT_GREY)){
            return SET_BG_COLOR_WHITE;
        }
        else if (currentColor.equals(SET_BG_COLOR_WHITE)){
            return SET_BG_COLOR_LIGHT_GREY;
        }
        else{
            return SET_BG_COLOR_DARK_GREEN;
        }
    }


    void printTopOrBottomRow(String teamColor) {
        System.out.print(SET_BG_COLOR_BLUE + EMPTY);

        if (teamColor.equals("WHITE")){
            System.out.print(" a   b   c  d   e   f  g   h ");

        }
        else{
            System.out.print(" h   g   f  e   d   c  b   a ");
        }
        System.out.print(SET_BG_COLOR_BLUE + EMPTY);

        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }


    String findPieceToPrint(ChessBoard board, int row, int col){
        String pieceSymbol = EMPTY;
        ChessPiece piece = board.getPiece(new ChessPosition(row,col));
        if (piece != null){
            String pieceString = piece.getTeamColor() + "_" + piece.getPieceType().toString();
            pieceSymbol = getPieceSymbol(pieceString);
        }
        return pieceSymbol;
    }






    private String getPieceSymbol(String pieceType){
        return switch (pieceType){
            case "WHITE_KING" -> WHITE_KING;
            case "WHITE_QUEEN" -> EscapeSequences.WHITE_QUEEN;
            case "WHITE_BISHOP" -> EscapeSequences.WHITE_BISHOP;
            case "WHITE_KNIGHT" -> EscapeSequences.WHITE_KNIGHT;
            case "WHITE_ROOK" -> EscapeSequences.WHITE_ROOK;
            case "WHITE_PAWN" -> EscapeSequences.WHITE_PAWN;

            case "BLACK_KING" -> EscapeSequences.BLACK_KING;
            case "BLACK_QUEEN" -> EscapeSequences.BLACK_QUEEN;
            case "BLACK_BISHOP" -> EscapeSequences.BLACK_BISHOP;
            case "BLACK_KNIGHT" -> EscapeSequences.BLACK_KNIGHT;
            case "BLACK_ROOK" -> EscapeSequences.BLACK_ROOK;
            case "BLACK_PAWN" -> EscapeSequences.BLACK_PAWN;

            default -> EMPTY;
        };
    }



    private void printPrompt() {
        if (state == State.SIGNEDOUT){
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_OUT] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
        else{
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_IN] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
    }


    public void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(401, "You must sign in before performing that operation");
        }
    }


    public void assertSignedOut() throws ResponseException {
        if (state == State.SIGNEDIN) {
            throw new ResponseException(403, "You need to log out before performing that operation");
        }
    }



}
