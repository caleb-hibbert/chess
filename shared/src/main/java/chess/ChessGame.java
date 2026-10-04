package chess;
import java.util.*;
import java.util.Set;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeamTurn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTeamTurn = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {//remove moves that would cause check/checkmate issues
        if (board.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> allPossibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> allValidMoves = new ArrayList<>();

        for (ChessMove move : allPossibleMoves){
            ChessBoard tempBoard = new ChessBoard(board);//makes a copy of original board w/copy constructor
            ChessPiece piece = tempBoard.getPiece(move.getStartPosition());//this can be shortened by just passing in startPosition if needed
            tempBoard.removePiece(move.getStartPosition());
            tempBoard.removePiece(move.getEndPosition());
            tempBoard.addPiece(move.getEndPosition(), piece);
            if (isInCheck(tempBoard, piece.getTeamColor())){//if we make a move and find that the king ends up in check, discard that move
                continue;
            }
            else{
                allValidMoves.add(move);
            }
        }


        //System.out.printf("List of moves we returned that were valid: %s\n", allPossibleMoves.toString());//for testing only
        return allValidMoves;
    }

    private void changeTeamTurn(){
        if (currentTeamTurn == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else if (currentTeamTurn == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null){
            throw new InvalidMoveException("The move " + move.toString() + " didn't have a piece in the starting position");
        }
        if (piece.getTeamColor() != currentTeamTurn){
            throw new InvalidMoveException("The move " + move.toString() + " couldn't be done since it's currently " + currentTeamTurn + "'s turn");
        }
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        if (validMovesList.contains(move)){
            board.removePiece(move.getStartPosition());
            board.removePiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            changeTeamTurn();
            //System.out.printf("The move %s was completed\n", move.toString());
        }
        else{
            throw new InvalidMoveException("The move " + move.toString() + " wasn't in the list of valid moves. That list was: " + validMovesList.toString());
        }

    }


    public record Result(ChessPiece king, ChessPosition kingPosition) {}

    public Result getKingInfo(TeamColor teamColor){//returns the king piece and position for the check/checkmate/stalemate methods
        ChessPiece king = null;
        ChessPosition kingPosition = null;
        for (int i = 0; i < 8; i++){// iterate over rows
            for (int j = 0; j < 8; j++){//iterate over columns
                ChessPiece pieceToCheck = board.getPiece(new ChessPosition(i+1,j+1));
                if (pieceToCheck != null && pieceToCheck.getPieceType() == ChessPiece.PieceType.KING && pieceToCheck.getTeamColor() == teamColor){
                    king = pieceToCheck;
                    kingPosition = new ChessPosition(i+1,j+1);
                }
            }
        }
        return new Result(king, kingPosition);
    }
    public Result getKingInfo(ChessBoard boardToCheck, TeamColor teamColor){//returns the king piece and position for the check/checkmate/stalemate methods
        ChessPiece king = null;
        ChessPosition kingPosition = null;
        for (int i = 0; i < 8; i++){// iterate over rows
            for (int j = 0; j < 8; j++){//iterate over columns
                ChessPiece pieceToCheck = boardToCheck.getPiece(new ChessPosition(i+1,j+1));
                if (pieceToCheck != null && pieceToCheck.getPieceType() == ChessPiece.PieceType.KING && pieceToCheck.getTeamColor() == teamColor){
                    king = pieceToCheck;
                    kingPosition = new ChessPosition(i+1,j+1);
                }
            }
        }
        return new Result(king, kingPosition);
    }


    private boolean checkIfEnemyCanAttackHere(ChessBoard boardToCheck, ChessPosition position, TeamColor teamColor){
        for (int i = 0; i < 8; i++){// iterate over rows
            for (int j = 0; j < 8; j++){//iterate over columns
                ChessPiece pieceToCheck = boardToCheck.getPiece(new ChessPosition(i+1,j+1));
                if (pieceToCheck != null && pieceToCheck.getTeamColor() != teamColor){// if space has enemy piece, get its moves and see if it can attack our king on the temp board
                    Collection<ChessMove> enemyMoves = pieceToCheck.pieceMoves(boardToCheck, new ChessPosition(i+1,j+1));
                    for (ChessMove move : enemyMoves){
                        if (move.getEndPosition().equals(position)){
                            return true; // enemy piece can attack this position
                        }
                    }
                }
            }
        }
        return false;
    }


//    private Collection<ChessMove> getSafeKingMoves(ChessPiece king, ChessPosition kingPosition, TeamColor teamColor){
//        Collection<ChessMove> proposedKingMoves = king.pieceMoves(board, kingPosition);
//        Collection<ChessMove> safeKingMoves = new ArrayList<>();
//
//        for (ChessMove singleProposedKingMove : proposedKingMoves){
//            ChessBoard tempBoard = new ChessBoard(board);//makes a copy of original board w/copy constructor
//            tempBoard.removePiece(kingPosition);
//            tempBoard.removePiece(singleProposedKingMove.getEndPosition());
//            tempBoard.addPiece(singleProposedKingMove.getEndPosition(), king);
//            if (checkIfEnemyCanAttackHere(tempBoard, singleProposedKingMove.getEndPosition(), teamColor)){
//                continue;
//            }
//            else{
//                safeKingMoves.add(singleProposedKingMove);
//            }
//        }
//        return safeKingMoves;
//    }


    private boolean teamHasValidMoveOptions(TeamColor teamColor){
        for (int i = 0; i < 8; i++){// iterate over rows
            for (int j = 0; j < 8; j++){//iterate over columns
                ChessPiece pieceToCheck = board.getPiece(new ChessPosition(i+1,j+1));
                if (pieceToCheck != null && pieceToCheck.getTeamColor() == teamColor){// if space has one of our pieces, see if it can make a move that doesn't result in check/checkmate, or resolves check/checkmate
                    Collection<ChessMove> friendlyValidMoves = validMoves(new ChessPosition(i+1,j+1));
                    if (!friendlyValidMoves.isEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }



    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Result result = getKingInfo(board, teamColor);
        ChessPiece king = result.king;
        ChessPosition kingPosition = result.kingPosition;

        return checkIfEnemyCanAttackHere(board, kingPosition, teamColor);
    }
    private boolean isInCheck(ChessBoard boardToCheck, TeamColor teamColor){
        Result result = getKingInfo(boardToCheck, teamColor);
        ChessPiece king = result.king;
        ChessPosition kingPosition = result.kingPosition;
        return checkIfEnemyCanAttackHere(boardToCheck, kingPosition, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Result result = getKingInfo(teamColor);
        ChessPiece king = result.king;
        ChessPosition kingPosition = result.kingPosition;
        boolean currentlyInDanger = checkIfEnemyCanAttackHere(board, kingPosition, teamColor);
        //Collection<ChessMove> safeKingMoves = getSafeKingMoves(king,kingPosition, teamColor);

        if (currentlyInDanger && !teamHasValidMoveOptions(teamColor)){
            return true;
        }
        else{
            return false;
        }


    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Result result = getKingInfo(teamColor);
        ChessPiece king = result.king;
        ChessPosition kingPosition = result.kingPosition;
        boolean currentlyInDanger = checkIfEnemyCanAttackHere(board, kingPosition, teamColor);
        //Collection<ChessMove> safeKingMoves = getSafeKingMoves(king,kingPosition, teamColor);
        if (!currentlyInDanger && !teamHasValidMoveOptions(teamColor)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTeamTurn == chessGame.currentTeamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeamTurn, board);
    }
}
