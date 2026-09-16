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
        int startingRow = -1;
        int promotionRow = -1;

        if (piceColor == ChessGame.TeamColor.WHITE){
            moveDirection = 1;
            startingRow = 2;
            promotionRow = 8;
        }
        else if (piceColor == ChessGame.TeamColor.BLACK){
            moveDirection = -1;
            startingRow = 7;
            promotionRow = 1;
        }



        // Movement checks
        if (row >= 2 && row <= 7) {// make sure we're at least 1 spot from the edge so moving forward doesn't take us off the board
            if (board.getPiece(new ChessPosition(row + moveDirection, col)) == null) {// if first space in front clear, that's a valid move
                if (row + moveDirection == promotionRow) {
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.QUEEN));

                    // add all promotion moves
                    //TODO - implement promote - have this run before null check ^ so we don't return a pawn move with no promotion if it tries to move to last tile
                }
                else{
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col), null));
                }


                if (row == startingRow && board.getPiece(new ChessPosition(row + moveDirection + moveDirection, col)) == null) {//if on starting row and first space was empty, check next space to validate double move forward
                    possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection + moveDirection, col), null));
                }
            }
        }


        // Attack checks
        // attack forward + left
        if (row >= 2 && row <= 7) {// make sure we're at least 1 spot from the top/bottom so moving forward while attacking doesn't take us off the board
            if (col > 1) {
                ChessPiece forwardLeftPiece = board.getPiece(new ChessPosition(row + moveDirection, col - 1));
                if (forwardLeftPiece != null && forwardLeftPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {// some piece is diagonal left to us, attack is valid if its color is opposite ours
                    if (row + moveDirection == promotionRow) {
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.QUEEN));
                        //TODO - implement promote - have this run before null check ^ so we don't return a pawn move without promotion if it tries to move to last tile
                    }
                    else{
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col - 1), null));
                    }
                }
            }
            if (col < 8) {
                // attack forward + right
                ChessPiece forwardRightPiece = board.getPiece(new ChessPosition(row + moveDirection, col + 1));
                if (forwardRightPiece != null && forwardRightPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {// some piece is diagonal right to us, check type to see if we can attack it - // if piece in our way is opposite color, that spot is a valid move (capture)
                    if (row + moveDirection == promotionRow){
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.QUEEN));
                        // TODO - add all promotion moves
                    }
                    else{
                        possibleMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + moveDirection, col + 1), null));

                    }
                }
            }
        }



        return possibleMoves;
    }
}
