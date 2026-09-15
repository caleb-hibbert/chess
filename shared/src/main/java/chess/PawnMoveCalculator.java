package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor piceColor) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();



        int row = position.getRow();
        int col = position.getColumn();



        if (piceColor == ChessGame.TeamColor.WHITE){
            if (row == 2){
                //check 2 squares in front + attack option
            }
            else if (row == 8){
                // promote
            }
            else{
                //check 1 square in front + attack option
            }


        }
        else if (piceColor == ChessGame.TeamColor.BLACK){


        }



        int[] white_forward = {1,0};
        int[] black_forward = {-1,0};


        int[][] movableDirections = {white_forward,};
        //TODO: Account for whether the piece has moved or not to determine if add +2 first move to possiblemoves
        //TODO: Account for what side the piece is on
        //TODO: Account for pieces in front stopping movement, pieces diagonal allowing for attack+move

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
