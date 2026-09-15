package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KnightMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor piceColor) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();


        int[] up_right = {2,1};
        int[] up_left = {2,-1};
        int[] left_up = {1,-2};
        int[] left_down = {-1,-2};
        int[] down_left = {-2,-1};
        int[] down_right = {-2,1};
        int[] right_up = {1,2};
        int[] right_down = {-1,2};
        int[][] movableDirections = {up_right, up_left, left_up, left_down, down_left, down_right, right_up, right_down};

        for (int[] directions : movableDirections){
            int row = position.getRow() + directions[0];
            int col = position.getColumn() + directions[1];// increment row + col in each direction


            if (row >= 1 && col >= 1 && row <= 8 && col <= 8){// while in bounds
                if (board.getPiece(new ChessPosition(row, col)) == null){
                    possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                }
                else if (board.getPiece(new ChessPosition(row, col)) != null){// piece was found where we want to go
                    ChessPiece piece_in_way = board.getPiece(new ChessPosition(row, col));
                    if (piece_in_way.getTeamColor() != board.getPiece(position).getTeamColor()){// if piece in our way is opposite color, that spot is a valid move (capture), then end checking in this direction
                        possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                    }

                }
            }
        }





        return possibleMoves;
    }
}
