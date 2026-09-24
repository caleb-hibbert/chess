package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[] up_right = {1,1};
        int[] up_left = {1,-1};
        int[] down_left = {-1,-1};
        int[] down_right = {-1,1};
        int[] up = {1,0};
        int[] down = {-1,0};
        int[] left = {0,-1};
        int[] right = {0,1};
        int[][] movableDirections = {up, down, left, right, up_left, up_right, down_left, down_right};

        return RepeatedMoveCalculator.calculateMoves(board, position, color, movableDirections);
    }
}
