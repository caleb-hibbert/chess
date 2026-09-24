package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMoveCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[] up = {1,0};
        int[] down = {-1,0};
        int[] left = {0,-1};
        int[] right = {0,1};
        int[] upRight = {1,1};
        int[] upLeft = {1,-1};
        int[] downLeft = {-1,-1};
        int[] downRight = {-1,1};
        int[][] movableDirections = {up,down,left,right,upRight, upLeft, downLeft, downRight};


        for (int[] direction : movableDirections){
            int row = position.getRow() + direction[0];
            int col = position.getColumn() + direction[1];

            while (row >= 1 && col >= 1 && row <= 8 && col <= 8){// while in bounds
                ChessPiece pieceToCheck = board.getPiece(new ChessPosition(row,col));

                if (pieceToCheck == null){
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
                else if (pieceToCheck != null && pieceToCheck.getTeamColor() != color){//enemy in spot we're checking
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
                else if (pieceToCheck != null && pieceToCheck.getTeamColor() == color){// friendly piece blocking movement
                    break;
                }
            }
        }
        return possibleMoves;
    }
}
