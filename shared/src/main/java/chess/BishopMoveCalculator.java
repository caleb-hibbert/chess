package chess;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


public class BishopMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        Collection <ChessMove> possibleMoves = new ArrayList<>(); // The type being ChessMove is already declared at beginning, can leave <> blank for arraylist

        //possibleMoves.add(new ChessMove(new ChessPosition(1,1), new ChessPosition(2,2), null));

        // check up+right - if spot empty and in bounds, add to possiblemoves
        // if spot filled with piece, check type. If opposite type of piece, add to possiblemoves


        int[] up_left = {1,-1};
        int[] up_right = {1,1};
        int[] down_left = {-1,-1};
        int[] down_right = {-1,1};
        int[][] movableDirections = {up_left, up_right, down_left, down_right};

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
