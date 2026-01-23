package chess;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class BishopMoveCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
    Collection<ChessMove> possibleMoves = new ArrayList<>(); // The type being ChessMove is already declared at beginning, can leave <> blank for arraylist

        int[] up_left = {1,-1};
        int[] up_right = {1,1};
        int[] down_left = {-1,-1};
        int[] down_right = {-1,1};
        int[][] movableDirections = {up_left, up_right, down_left, down_right};


        int row = position.getRow();
        int column = position.getColumn();
        int quadrant = 0;
        int[] current_direction = movableDirections[quadrant]; //start with up+left



        while (row >= 1 && row <= 8 && column >= 1 && column <= 8){

            row += current_direction[0];
            column += current_direction[1];

            ChessPosition spotToCheck = new ChessPosition(row, column);
            ChessPiece pieceInSpotWeWantToGo = board.getPiece(spotToCheck);

            if (pieceInSpotWeWantToGo == null){// nothing here, valid move! :D
                possibleMoves.add(new ChessMove(position, spotToCheck, null));
            }
            else if (pieceInSpotWeWantToGo.getTeamColor() != color){// check color of pice in possible spot, if enemy, good to move there, but need to stop looking further
                possibleMoves.add(new ChessMove(position, spotToCheck, null));
                break;
                quadrant += 1;// TODO - implement moving of quadrants to cycle which direction to check. Make sure we cycle when hitting end of board?
            }

        }
        return possibleMoves;
    }
}
