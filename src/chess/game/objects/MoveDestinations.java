package chess.game.objects;

import chess.general.Common;
import chess.general.Loggable;

import java.util.HashMap;
import java.util.Vector;

/**
 * todo: description for MoveDestinations class
 * Created by Fez on 11/23/14.
 */
public class MoveDestinations extends Loggable {
    private Vector<Square> moveOnlyLocations;
    private Vector<Square> killOnlyLocations;
    private HashMap<Piece, Vector<Square>> pinnedPieces;
    private Vector<Square> pathToObjective;

    public MoveDestinations(String pieceName) {
        super("MoveDestinations("+pieceName+")");
        moveOnlyLocations = new Vector<Square>();
        killOnlyLocations = new Vector<Square>();
        pinnedPieces = new HashMap<Piece, Vector<Square>>();
        pathToObjective = new Vector<Square>();
    }

    public void addMoveLocation(Square s) {
        if (!moveOnlyLocations.contains(s))
            moveOnlyLocations.add(s);
    }

    public void addKillLocation(Square s) {
        if (!killOnlyLocations.contains(s))
            killOnlyLocations.add(s);
    }

    public void addPinnedPiece(Piece p, Vector<Square> rail) {
        // each piece that is pinned can only move down the "rail"
        // with which the pinner is attacking it
        if(p != null && !pinnedPieces.containsKey(p)) {
            pinnedPieces.put(p, rail);
            logLine("Pinning " + p.getPieceName(), 0);
        }
    }

    public void intersectWith(Vector<Square> locations) {
        for(int i = 0; i < moveOnlyLocations.size();) {
            Square s = moveOnlyLocations.get(i);
            if(!locations.contains(s)) {
                moveOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }

        for(int i = 0; i < killOnlyLocations.size();) {
            Square s = killOnlyLocations.get(i);
            if(!locations.contains(s)) {
                killOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }
    }

    public void addPathToObjective(Vector<Square> squares) {
        pathToObjective.addAll(squares);
    }

    public void clearMoveLocations() {
        moveOnlyLocations.clear();
    }

    public void clearKillLocations() {
        killOnlyLocations.clear();
    }

    public void clearPiecesPinning() {
        pinnedPieces.clear();
    }

    public void clearPathToObjective() {
        pathToObjective.clear();
    }

    public void clearAll() {
        clearMoveLocations();
        clearKillLocations();
        clearPiecesPinning();
        clearPathToObjective();
    }

    public boolean canMoveTo(Square s) {
        return moveOnlyLocations.contains(s);
    }

    public boolean canKillAt(Square s) {
        return killOnlyLocations.contains(s);
    }

    public HashMap<Piece, Vector<Square>> getPinnedPieces() {
        return pinnedPieces;
    }

    public Vector<Square> getAllLocationsAsOne() {
        return Common.union(moveOnlyLocations, killOnlyLocations);
    }
}
