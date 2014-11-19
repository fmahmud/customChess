package chess.baseObjects;

import chess.custom.Faction;

public class Player {

    private Faction faction;
    private String name;
    private boolean hasBeenChecked;
    private Team team;
    private long timeTaken;


    //todo: add name to player constructor
    public Player(Team _t, Faction _f, String _n) {
        faction = _f;
        team = _t;
        name = _n;
        timeTaken = 0l;
    }

    public Faction getFaction() {
        return faction;
    }

    public Team getTeam() {
        return team;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public String getFormattedTimeTaken() {
        return "TIME1";
    }

    public String getName() {
        return name;
    }
}