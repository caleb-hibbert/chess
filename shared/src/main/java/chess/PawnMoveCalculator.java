package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int moveDirection = 1;
        int startRow = 2;
        int promotionRow = 8;

        if (color == ChessGame.TeamColor.BLACK){
            moveDirection = -1;
            startRow = 7;
            promotionRow = 1;
        }

        //move
        if (row+moveDirection >= 1 && row+moveDirection <=8 ) {
            ChessPiece pieceToCheck = board.getPiece(new ChessPosition(row + moveDirection, col));
            if (pieceToCheck == null) {// check if piece in front is empty
                if (row == startRow && board.getPiece(new ChessPosition(row + moveDirection + moveDirection, col)) == null) {
                    // if piece in front empty, and we're on start row, check if an extra space ahead is empty too
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), null));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection + moveDirection, col), null));
                } else if (row + moveDirection == promotionRow && board.getPiece(new ChessPosition(row + moveDirection, col)) == null) {
                    // move ahead is valid and takes us to promotion row
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), ChessPiece.PieceType.QUEEN));
                } else {
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col), null));
                }
            }
        }

        //attack left
        if (row+moveDirection >= 1 && row+moveDirection <= 8 && col-1 >= 1){// while proposed move is in bounds
            ChessPiece forwardLeftPiece = board.getPiece(new ChessPosition(row+moveDirection,col-1));
            if (forwardLeftPiece != null && forwardLeftPiece.getTeamColor() != color){// if enemy piece in forward+left square
                if (row+moveDirection == promotionRow){// attack possible and puts us on promotion row
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col-1), ChessPiece.PieceType.QUEEN));
                }
                else{
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col-1), null));
                }
            }
        }

        //attack right
        if (row+moveDirection >= 1 && row+moveDirection <= 8 && col+1 <= 8){// while proposed move is in bounds
            ChessPiece forwardRightPiece = board.getPiece(new ChessPosition(row+moveDirection,col+1));
            if (forwardRightPiece != null && forwardRightPiece.getTeamColor() != color){// if enemy piece in forward+left square
                if (row+moveDirection == promotionRow){// attack possible and puts us on promotion row
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col+1), ChessPiece.PieceType.QUEEN));
                }
                else{
                    possibleMoves.add(new ChessMove(position, new ChessPosition(row + moveDirection, col+1), null));
                }
            }
        }
        return possibleMoves;
    }
}
