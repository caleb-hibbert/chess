package chess;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


public class BishopMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        Collection <ChessMove> possibleMoves = new ArrayList<>();

        // check up+right - if spot empty and in bounds, add to possiblemoves
        // if spot filled with piece, check type. If opposite type of piece, add to possiblemoves
        int[] up_right = {1,1};
        int[] up_left = {1,-1};
        int[] down_left = {-1,-1};
        int[] down_right = {-1,1};
        int[][] movableDirections = {up_left, up_right, down_left, down_right};

        return RepeatedMoveCalculator.calculateMoves(board, position, color, movableDirections);
    }
}
