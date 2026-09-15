package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor piceColor) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[] up = {1,0};
        int[] down = {-1,0};
        int[] left = {0,-1};
        int[] right = {0,1};
        int[][] movableDirections = {up, down, left, right};


        for (int[] directions : movableDirections){
            int row = position.getRow() + directions[0];
            int col = position.getColumn() + directions[1];// increment row + col in each direction

            while (row >= 1 && col >= 1 && row <= 8 && col <= 8){// while in bounds
                if (board.getPiece(new ChessPosition(row, col)) == null){
                    possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                    row += directions[0];
                    col += directions[1];
                }
                else if (board.getPiece(new ChessPosition(row, col)) != null){// piece was found where we want to go
                    ChessPiece piece_in_way = board.getPiece(new ChessPosition(row, col));
                    if (piece_in_way.getTeamColor() != board.getPiece(position).getTeamColor()){// if piece in our way is opposite color, that spot is a valid move (capture), then end checking in this direction
                        possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                    }
                    break;
                }
            }
        }









        return possibleMoves;
    }

}
