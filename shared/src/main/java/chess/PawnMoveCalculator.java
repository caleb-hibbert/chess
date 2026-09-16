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
        int moveDirection = 1;
        int[] attackPositionLeft = {1, -1};
        int[] attackPositionRight = {1, 1};
        int startingRow = -1;
        int promotionRow = -1;

        if (piceColor == ChessGame.TeamColor.WHITE){
            moveDirection = 1;
            attackPositionLeft = new int[] {1, -1};
            attackPositionRight = new int[] {1, 1};
            startingRow = 2;
            promotionRow = 8;
        }
        else if (piceColor == ChessGame.TeamColor.BLACK){
            moveDirection = -1;
            attackPositionLeft = new int[] {-1, -1};// the "left" attack will go left from White's POV, and right from Black's POV, towards column 1
            attackPositionRight = new int[] {-1, 11};
            startingRow = 7;
            promotionRow = 1;
        }



        // Movement checks
        if (row >= 2 && row <= 7) {// make sure we're at least 1 spot from the edge so moving forward doesn't take us off the board
            if (board.getPiece(new ChessPosition(row + moveDirection, col)) == null) {// if first space in front clear, that's a valid move
                possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), null));

                if (row + moveDirection == promotionRow) {
                    //promote
                }

                if (row == startingRow && board.getPiece(new ChessPosition(row + moveDirection + moveDirection, col)) == null) {//if on starting row and first space was empty, check next space to validate double move forward
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection + moveDirection, col), null));
                }
            }
        }


        // Attack checks
        if (row >= 2 && row <= 7) {
            if (board.getPiece(new ChessPosition(row + attackPositionLeft[0], col + attackPositionLeft[1])) != null) {// some piece is diagonal left to us, check type to see if we can attack it
                if (board.getPiece(new ChessPosition(row + attackPositionLeft[0], col + attackPositionLeft[1])).getTeamColor() != board.getPiece(position).getTeamColor()) {// if piece in our way is opposite color, that spot is a valid move (capture)
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + attackPositionLeft[0], col + attackPositionLeft[1]), null));
                }
            }
            if (board.getPiece(new ChessPosition(row + attackPositionRight[0], col + attackPositionRight[1])) != null) {// some piece is diagonal right to us, check type to see if we can attack it
                if (board.getPiece(new ChessPosition(row + attackPositionRight[0], col + attackPositionRight[1])).getTeamColor() != board.getPiece(position).getTeamColor()) {// if piece in our way is opposite color, that spot is a valid move (capture)
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + attackPositionRight[0], col + attackPositionRight[1]), null));
                }
            }
        }




        return possibleMoves;
    }
}
