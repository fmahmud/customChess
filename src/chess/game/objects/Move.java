package chess.game.objects;

/**
 * todo: description for Move class
 * Created by Fez on 11/23/14.
 */
public class Move {
    private Square to;
    private Piece actor;
    private boolean isKill;

    public Move(Piece _a, Square _t, boolean _b) {
        this.actor = _a;
        this.to = _t;
        this.isKill = _b;
    }

    public boolean isKill() {return isKill;}

    public Piece getActor() {
        return actor;
    }

    public Square getTo() {
        return to;
    }

    public String toString() {
        return actor.getPieceName()+", "+to.getCoordinatesAsString() + ", "+isKill;
    }

}