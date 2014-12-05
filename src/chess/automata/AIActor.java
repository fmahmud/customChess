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
        if(pieces.size() == 0) return null;
        Piece randomPiece = pieces.get((int)(Math.random() * pieces.size()));
        MoveDestinations moveDestinations;
        Square dest = null;
        int depth = 1000;
        while(depth-- > 0) {
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
            randomPiece = pieces.get((int)(Math.random() * pieces.size()));
        }
        if(dest == null) return null;
        return new Move(randomPiece, dest, dest.hasPiece());
    }
}
