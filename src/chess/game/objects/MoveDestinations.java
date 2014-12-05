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
    private Vector<Square> pathAfterObjective;
    private Vector<Square> indirectKillLocations;

    public MoveDestinations(String pieceName) {
        super("MoveDests("+pieceName+")");
        moveOnlyLocations = new Vector<Square>();
        killOnlyLocations = new Vector<Square>();
        pinnedPieces = new HashMap<Piece, Vector<Square>>();
        pathToObjective = new Vector<Square>();
        pathAfterObjective = new Vector<Square>();
        indirectKillLocations = new Vector<Square>();
    }

    public void addMoveLocation(Square s) {
        if (!moveOnlyLocations.contains(s)) {
            moveOnlyLocations.add(s);
        }
    }

    public void addKillLocation(Square s) {
        if (!killOnlyLocations.contains(s)) {
            killOnlyLocations.add(s);
        }
    }

    public void addIndirectKillLocation(Square s) {
        if(!killOnlyLocations.contains(s) && !indirectKillLocations.contains(s))
            indirectKillLocations.add(s);
    }

    public void addPinnedPiece(Piece p, Vector<Square> rail) {
        // each piece that is pinned can only move down the "rail"
        // with which the pinner is attacking it
        if(p != null && !pinnedPieces.containsKey(p)) {
            pinnedPieces.put(p, rail);
//            logLine("Pinning " + p.getPieceName(), 0);
        }
    }

    public void intersectWith(Vector<Square> locations) {
//        logLine("Before "+moveOnlyLocations.size(), 0);
        for(int i = 0; i < moveOnlyLocations.size();) {
            Square s = moveOnlyLocations.get(i);
            if(!locations.contains(s)) {
                moveOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }
//        logLine("after "+moveOnlyLocations.size(), 0);


        for(int i = 0; i < killOnlyLocations.size();) {
            Square s = killOnlyLocations.get(i);
            if(!locations.contains(s)) {
                killOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }

    }

    public void subtractWith(Vector<Square> locations) {
        for(int i = 0; i < moveOnlyLocations.size();) {
            Square s = moveOnlyLocations.get(i);
            if(locations.contains(s)) {
                moveOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }

        for(int i = 0; i < killOnlyLocations.size();) {
            Square s = killOnlyLocations.get(i);
            if(locations.contains(s)) {
                killOnlyLocations.remove(s);
            } else {
                ++i;
            }
        }

    }

    public void addPathToObjective(Vector<Square> squares) {
        boolean foundKing = false;
        for(Square s : squares) {
            Piece p = s.getPiece();
            if(foundKing)
                pathAfterObjective.add(s);
            else
                pathToObjective.add(s);
            if(p != null && p.isObjective())
                foundKing = true;
        }
    }

    public Vector<Square> getPathToObjective() {
        return pathToObjective;
    }

    public Vector<Square> getPathAfterObjective() {
        return pathAfterObjective;
    }

    public boolean containsObjective() {
        return !pathToObjective.isEmpty();
    }

    public void clearMoveLocations() {
        moveOnlyLocations.clear();
    }

    public void clearKillLocations() {
        indirectKillLocations.clear();
        killOnlyLocations.clear();
    }

    public void clearPiecesPinning() {
        pinnedPieces.clear();
    }

    public void clearObjectivePaths() {
        pathToObjective.clear();
        pathAfterObjective.clear();
    }

    public void clearAll() {
        clearMoveLocations();
        clearKillLocations();
        clearPiecesPinning();
        clearObjectivePaths();
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

    public Vector<Square> getAllAsOne() {
        return Common.union(
                    Common.union(pathAfterObjective, pathToObjective  ),
                    Common.union(moveOnlyLocations , killOnlyLocations)
        );
    }

    public Vector<Square> getNotMoveOnlyLocations() {
        return Common.union(
                Common.union(pathAfterObjective, pathToObjective),
                Common.union(killOnlyLocations, indirectKillLocations)
        );
    }

    public int getTotalNumMoves() {
        return killOnlyLocations.size() + moveOnlyLocations.size();
    }

    public Square getRandomKillLocation() {
        if(killOnlyLocations.size() == 0) return null;
        return killOnlyLocations.get((int)(Math.random() * killOnlyLocations.size()));
    }

    public Square getRandomMoveLocation() {
        if(moveOnlyLocations.size() == 0) return null;
        return moveOnlyLocations.get((int)(Math.random() * moveOnlyLocations.size()));
    }

}
