package chess.automata;

import chess.game.objects.*;

/**
 * Created by Fez on 12/4/14.
 */
public class AIActor extends Player {

    public AIActor(String s) {
        super("AIActor: "+s);
        setIsComputer(true);
    }

    public Move getMove() {
        Piece randomPiece;
        MoveDestinations moveDestinations;
        Square dest = null;
        while(true) {
            randomPiece = pieces.get((int)(Math.random() * pieces.size()));
            moveDestinations = randomPiece.getMoveDestinations();
            if(moveDestinations.getTotalNumMoves() > 0) {
                if(Math.random() > 0.9) {
                    dest = moveDestinations.getRandomMoveLocation();
                } else {
                    dest = moveDestinations.getRandomKillLocation();
                }
            }
            if(dest != null) {
                break;
            }
        }

        return new Move(randomPiece, dest, dest.hasPiece());
    }
}
