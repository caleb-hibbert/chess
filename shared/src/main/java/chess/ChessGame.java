package chess;
import java.util.*;


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
        Collection<ChessMove> allPossibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);

        System.out.printf("List of moves we returned that were valid: %s\n", allPossibleMoves.toString());//for testing only
        return allPossibleMoves;
        //throw new RuntimeException("Not implemented");
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
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        if (validMovesList.contains(move)){
            ChessPiece piece = board.getPiece(move.getStartPosition());
            board.removePiece(move.getStartPosition());
            board.removePiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            changeTeamTurn();
            System.out.printf("The move %s was completed\n", move.toString());
        }
        else{
            throw new InvalidMoveException("The move " + move.toString() + " wasn't in the list of valid moves. That list was: " + validMovesList.toString());
        }

    }


    public record Result(ChessPiece king, ChessPosition kingPosition) {}

    public Result getKingInfo(TeamColor teamColor){//returns the king piece and position for the check/checkmate/stalemate methods
        ChessPiece king = null;
        ChessBoard board = getBoard();
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

    private boolean checkIfEnemyCanAttackHere(ChessPosition position, TeamColor teamColor){
        ChessBoard tempBoard = new ChessBoard(board);//makes a copy of original board w/copy constructor

        for (int i = 0; i < 8; i++){// iterate over rows
            for (int j = 0; j < 8; j++){//iterate over columns
                ChessPiece pieceToCheck = tempBoard.getPiece(new ChessPosition(i+1,j+1));
                if (pieceToCheck != null && pieceToCheck.getTeamColor() != teamColor){// if space has enemy piece, get its moves and see if it can attack our king on the temp board
                    Collection<ChessMove> enemyMoves = pieceToCheck.pieceMoves(tempBoard, new ChessPosition(i,j));
                    for (ChessMove move : enemyMoves){
                        if (move.getEndPosition() == position){
                            return true; // enemy piece can attack this position
                        }
                    }
                }
            }
        }
        return false;
    }


    private Collection<ChessMove> getSafeKingMoves(ChessPiece king, ChessPosition kingPosition, TeamColor teamColor){
        Collection<ChessMove> proposedKingMoves = king.pieceMoves(board, kingPosition);
        Collection<ChessMove> safeKingMoves = new ArrayList<>();

        for (ChessMove singleProposedKingMove : proposedKingMoves){
            ChessBoard tempBoard = new ChessBoard(board);//makes a copy of original board w/copy constructor
            tempBoard.removePiece(kingPosition);
            tempBoard.removePiece(singleProposedKingMove.getEndPosition());
            tempBoard.addPiece(singleProposedKingMove.getEndPosition(), king);
            if (checkIfEnemyCanAttackHere(kingPosition, teamColor)){
                break;
            }
            else{
                safeKingMoves.add(singleProposedKingMove);
            }
//            for (int i = 0; i < 8; i++){// iterate over rows
//                for (int j = 0; j < 8; j++){//iterate over columns
//                    ChessPiece pieceToCheck = tempBoard.getPiece(new ChessPosition(i+1,j+1));
//                    if (pieceToCheck != null && pieceToCheck.getTeamColor() != teamColor){// if space has enemy piece, get its moves and see if it can attack our king on the temp board
//                        Collection<ChessMove> enemyMoves = pieceToCheck.pieceMoves(tempBoard, new ChessPosition(i,j));
//                        for (ChessMove move : enemyMoves){
//                            if (move.getEndPosition() == singleProposedKingMove.getEndPosition()){
//                                break; // if enemy piece can move where temp king is standing, the king can't go here
//                            }
//                            else{
//                                safeKingMoves.add(singleProposedKingMove);
//                            }
//                        }
//                    }
//                }
//            }
        }
        return safeKingMoves;
    }






    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Result result = getKingInfo(teamColor);
        ChessPiece king = result.king;
        ChessPosition kingPosition = result.kingPosition;
        Collection<ChessMove> safeKingMoves = getSafeKingMoves(king,kingPosition, teamColor);



        //TODO - we have king color/piece/position, now we need to get the original list of "valid moves" that end here to see if we're in check?




        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
