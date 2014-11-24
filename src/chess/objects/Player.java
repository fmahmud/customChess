package chess.objects;

import chess.custom.Faction;
import chess.general.Loggable;

import java.util.Vector;

public class Player extends Loggable {

    private Faction faction;
    private String name;
    private Team team;
    private Vector<Piece> killedPieces, pieces;


    public Player(Team _t, Faction _f, String _n) {
        super(_n);
        faction = _f;
        team = _t;
        name = _n;
        killedPieces = new Vector<Piece>();
        pieces = new Vector<Piece>();
    }

    public void ownPiece(Piece p) {
        pieces.add(p);
    }

    public void addKilledPiece(Piece p) {
        if(pieces.contains(p))
            killedPieces.add(p);
    }

    public Vector<Piece> getKilledPieces() {
        return killedPieces;
    }

    public Faction getFaction() {
        return faction;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }
}