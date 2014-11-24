package chess.objects;

/**
 * todo: description for Move class
 * Created by Fez on 11/23/14.
 */
public class Move {
    private int turnCount;
    private float directValue, indirectValue;
    private Square source, destination;
    private Piece actor, other;

    public Move(int _tc, Square _s, Square _t) {
        this.turnCount = _tc;
        this.source = _s;
        this.destination = _t;
        this.actor = source.getPiece();
        this.other = destination.getPiece();
    }

    public Square getSource() {
        return source;
    }

    public Square getDestination() {
        return destination;
    }

    public Piece getActor() {
        return actor;
    }

    public Piece getOther() {
        return other;
    }

    public float getDirectValue() {
        return directValue;
    }

    public void setDirectValue(float directValue) {
        this.directValue = directValue;
    }

    public float getIndirectValue() {
        return indirectValue;
    }

    public void setIndirectValue(float indirectValue) {
        this.indirectValue = indirectValue;
    }

    public int getTurnCount() {
        return turnCount;
    }
}
