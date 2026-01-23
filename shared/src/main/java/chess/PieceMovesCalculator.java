package chess;
import java.util.Collection;
import java.util.List;


public interface PieceMovesCalculator {
    Collection<ChessMove> calculateMoves(
            ChessBoard board,
            ChessPosition piecePosition,
            ChessGame.TeamColor pieceColor
    );




}
