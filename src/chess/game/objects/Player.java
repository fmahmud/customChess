package chess.game.objects;

import chess.general.Loggable;

import java.util.Vector;

public class Player extends Loggable {

    private String name;
    private Team team;
    private Vector<Piece> killedPieces, pieces;
    private Vector<Piece> objectives;


    public Player(Team _t, String _n) {
        super(_n);
        team = _t;
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

    public void addKilledPiece(Piece p) {
        if (pieces.contains(p)) {
            killedPieces.add(p);
            pieces.remove(p);
        }
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
}