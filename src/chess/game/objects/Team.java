package chess.game.objects;

import chess.general.Loggable;
import org.json.JSONObject;

public class Team extends Loggable {
    private Player[] players;
    private String name;
    //idea: something for score?

    public Team(int numPlayers, String _n) {
        super(_n);
        define(numPlayers, _n);
    }

    public Team(JSONObject obj) {
        super(obj.getString("teamName"));
        define(obj.getInt("numberOfPlayers") , obj.getString("teamName"));
    }

    private void define(int _p, String _n) {
        name = _n;
        players = new Player[_p];
        for (int i = 0; i < _p; i++) {
            //todo: fix player name - push to user input
            players[i] = new Player(this, name + "_" + (i + 1));
        }
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public Player getPlayer(int i) {
        return players[i];
    }
}
