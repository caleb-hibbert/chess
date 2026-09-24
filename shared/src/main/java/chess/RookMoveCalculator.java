package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[] up = {1,0};
        int[] down = {-1,0};
        int[] left = {0,-1};
        int[] right = {0,1};
        int[][] movableDirections = {up, down, left, right};

        return RepeatedMoveCalculator.calculateMoves(board, position, color, movableDirections);
    }
}
