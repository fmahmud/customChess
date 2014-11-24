package chess.objects;

import chess.custom.Faction;
import chess.general.Loggable;

public class Team extends Loggable {
    int numberOfPlayers;
    Player[] players;
    private String name;
    //idea: something for score?

    public Team(int numPlayers, Faction[] _factions, String _n) {
        super(_n);
        numberOfPlayers = numPlayers;
        name = _n;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            //todo: fix player name - push to user input
            players[i] = new Player(this, _factions[i], name + "_" + (i + 1));
        }
    }

    public Player getPlayer(int i) {
        return players[i];
    }
}
