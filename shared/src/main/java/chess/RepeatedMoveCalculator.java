package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RepeatedMoveCalculator {
    public static Collection<ChessMove> calculateMoves(
            ChessBoard board,
            ChessPosition position,
            ChessGame.TeamColor color,
            int[][] movableDirections){

        Collection<ChessMove> possibleMoves = new ArrayList<>();


        for (int[] direction : movableDirections){
            int row = position.getRow() + direction[0];
            int col = position.getColumn() + direction[1];// increment row + col in each direction

            while (row >= 1 && col >= 1 && row <= 8 && col <= 8){// while in bounds
                if (board.getPiece(new ChessPosition(row, col)) == null){
                    possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                    row += direction[0];
                    col += direction[1];
                }
                else if (board.getPiece(new ChessPosition(row, col)) != null){// piece was found where we want to go
                    ChessPiece piece_in_way = board.getPiece(new ChessPosition(row, col));
                    if (piece_in_way.getTeamColor() != board.getPiece(position).getTeamColor()){
                        // if piece in our way is opposite color, that spot is a valid move (capture), then end checking in this direction
                        possibleMoves.add(new ChessMove(new ChessPosition(position.getRow(),position.getColumn()), new ChessPosition(row,col), null));
                    }
                    break;
                }
            }
        }
        return possibleMoves;
    }
}
