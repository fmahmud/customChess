package chess.objects;

import java.util.Vector;

/**
 * todo: description for MoveDestinations class
 * Created by Fez on 11/23/14.
 */
public class MoveDestinations {
    private Vector<Square> moveOnlyLocations;
    private Vector<Square> killOnlyLocations;

    public MoveDestinations() {
        moveOnlyLocations = new Vector<Square>();
        killOnlyLocations = new Vector<Square>();
    }

    public void addMoveLocation(Square s) {
        if(!moveOnlyLocations.contains(s))
            moveOnlyLocations.add(s);
    }

    public void addKillLocation(Square s) {
        if(!killOnlyLocations.contains(s))
            killOnlyLocations.add(s);
    }

    public void clearMoveLocations() {
        moveOnlyLocations.clear();
    }

    public void clearKillLocations() {
        killOnlyLocations.clear();
    }

    public void clearAll() {
        clearMoveLocations();
        clearKillLocations();
    }

    public boolean canMoveTo(Square s) {
        return moveOnlyLocations.contains(s);
    }

    public boolean canKillAt(Square s) {
        return killOnlyLocations.contains(s);
    }
}
