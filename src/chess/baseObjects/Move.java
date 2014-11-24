package chess.baseObjects;

/**
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

    public void setSource(Square source) {
        this.source = source;
    }

    public Square getDestination() {
        return destination;
    }

    public void setDestination(Square destination) {
        this.destination = destination;
    }

    public Piece getActor() {
        return actor;
    }

    public void setActor(Piece actor) {
        this.actor = actor;
    }

    public Piece getOther() {
        return other;
    }

    public void setOther(Piece other) {
        this.other = other;
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
}
