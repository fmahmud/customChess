package chess.baseObjects;


import chess.custom.Faction;
import chess.general.Loggable;

import javax.swing.*;

/**
 * Class that keeps track of the various teams, the board and the turn order.
 *
 */
public abstract class GameMode extends Loggable {
    protected Team[] teams;
    protected int numTeams;
    protected int turnCount = 0;
    protected Player[] playerOrder;
    protected JPanel leftPanel, rightPanel, headerPanel, footerPanel, centerPanel;
    protected String modeName;


    /**
     * Game start safe: returns 0 for the first turn
     *
     * @return - Returns the player who's turn it is currently.
     */
    public abstract Player getNextActivePlayer();

    public abstract Player getCurrentActivePlayer();

    public int getTurnCount() {
        return turnCount;
    }

    public GameMode(String _name, int _numTeams, int[] numPlayersInTeams, Faction[][] factions) {
        super(_name);
        modeName = _name;
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        headerPanel = new JPanel();
        footerPanel = new JPanel();
        centerPanel = new JPanel();
        this.numTeams = _numTeams;
        teams = new Team[numTeams];
        int totalNumPlayers = 0;
        for (int i = 0; i < numTeams; i++) {
            //todo: fix team name - push to user input
            teams[i] = new Team(numPlayersInTeams[i], factions[i], "Team"+(i+1));
            totalNumPlayers += numPlayersInTeams[i];
        }
        playerOrder = new Player[totalNumPlayers];
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    public JPanel getFooterPanel() {
        return footerPanel;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public abstract boolean tryMoveFromTo(Square from, Square to);

    public abstract boolean tryKillAt(Square from, Square to);

    public abstract void undo();

    public void onEndGame() {
        //do something later?
    }
}
