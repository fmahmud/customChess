package chess.game.objects;

import chess.general.Loggable;

import java.util.Vector;

public class Player extends Loggable {

    private String name;
    private Team team;
    protected Vector<Piece> killedPieces, pieces;
    protected Vector<Piece> objectives;
    private boolean isComputer = false;

    public Player(String _n) {
        super(_n);
        name = _n;
        killedPieces = new Vector<Piece>();
        pieces = new Vector<Piece>();
        objectives = new Vector<Piece>();
    }

    public void addPiece(Piece p) {
        if(!pieces.contains(p)) {
            pieces.add(p);
            p.setOwner(this);
            if(p.isObjective())
                objectives.add(p);
        }
    }

    public void setIsComputer(boolean b) {
        isComputer = b;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public void addKilledPiece(Piece p) {
        if (pieces.contains(p)) {
            killedPieces.add(p);
            pieces.remove(p);
        }
    }

    public void setTeam(Team t) {
        team = t;
    }

    public Vector<Piece> getObjectives() {
        return objectives;
    }

    public Vector<Piece> getKilledPieces() {
        return killedPieces;
    }

    public Vector<Piece> getPieces() {
        return pieces;
    }

    public Team getTeam() {
        return team;
    }

    public String getPlayerName() {
        return name;
    }

    public void resurrectPiece(Piece piece) {
        if(killedPieces.contains(piece)) {
            killedPieces.remove(piece);
            pieces.add(piece);
        }
    }
}