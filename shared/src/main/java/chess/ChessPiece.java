package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;// both using enums, pieceColor just uses an enum in ChessGame, making the definition longer

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        return switch (piece.getPieceType()) {
            case BISHOP -> {
                PieceMovesCalculator calculator = new BishopMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
            case ROOK -> {
                PieceMovesCalculator calculator = new RookMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
            case QUEEN -> {
                PieceMovesCalculator calculator = new QueenMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
            case KNIGHT -> {
                PieceMovesCalculator calculator = new KnightMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
            case KING -> {
                PieceMovesCalculator calculator = new KingMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
            case PAWN ->{
                PieceMovesCalculator calculator = new PawnMoveCalculator();
                yield calculator.calculateMoves(board, myPosition, piece.pieceColor);
            }
        };
    }


    @Override
    public String toString() {return "ChessPiece{" + "pieceColor=" + pieceColor + ", type=" + type + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
