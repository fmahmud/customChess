package chess.game.objects;

import chess.general.Loggable;

import java.util.Vector;

public class Player extends Loggable {

    private String name;
    private Team team;
    private Vector<Piece> killedPieces, pieces;


    public Player(Team _t, String _n) {
        super(_n);
        team = _t;
        name = _n;
        killedPieces = new Vector<Piece>();
        pieces = new Vector<Piece>();
    }

    public void addPiece(Piece p) {
        if(!pieces.contains(p)) {
            pieces.add(p);
            p.setOwner(this);
        }
    }

    public void addKilledPiece(Piece p) {
        if (pieces.contains(p))
            killedPieces.add(p);
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