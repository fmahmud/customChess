package chess.game.objects;

import chess.general.Loggable;

import java.util.Vector;

public class Team extends Loggable {
    private Vector<Player> players;
    private String name;
    //idea: something for score?

    /**
     *
     * @param _n
     */
    public Team(String _n) {
        super(_n);
        define(_n);
    }


    private void define(String _n) {
        name = _n;
        players = new Vector<Player>();
    }

    public void addPlayer(Player p) {
        if(!players.contains(p))
            players.add(p);
        p.setTeam(this);
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }
}
