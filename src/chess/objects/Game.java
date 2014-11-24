package chess.objects;

import chess.general.Loggable;

import javax.swing.*;
import java.awt.*;

public class Game extends Loggable {
    private static JLabel[] labels;
    protected Player currentPlayer;
    private GameMode mode;
    private JPanel scorePanel, eventPanel, menuPanel;

    public Game(GameMode _mode) {
        super("Game");
        mode = _mode;
        scorePanel = new JPanel();
        eventPanel = new JPanel();
        menuPanel = new JPanel();
        currentPlayer = this.mode.getNextActivePlayer();
        scorePanel = new JPanel();
        labels = new JLabel[4];
        initializeScore();
    }

    public void updateCurrentPlayer(Player p) {
        currentPlayer = p;
        labels[0].setText("Player 1 time: T");
        labels[1].setText("Turn count = " + mode.getTurnCount());
        labels[2].setText("Current Player = " + currentPlayer.getName());
        labels[3].setText("Player 2 time: T");
        for (JLabel l : labels) {
            l.repaint();
        }
    }

    public void endGame() {
        mode.onEndGame();
    }

    public void setRightPanel(JPanel pnl) {
        mode.setRightPanel(pnl);
    }

    public void setLeftPanel(JPanel pnl) {
        mode.setLeftPanel(pnl);
    }

    public void setHeaderPanel(JPanel pnl) {
        mode.setHeaderPanel(pnl);
    }

    public void setFooterPanel(JPanel pnl) {
        mode.setFooterPanel(pnl);
    }

    public void setCenterPanel(JPanel pnl) {
        mode.setCenterPanel(pnl);
    }

    private void initializeScore() {
        labels[0] = new JLabel("Player 1 time: T");
        labels[1] = new JLabel("Turn count = " + mode.getTurnCount());
        labels[2] = new JLabel("Current Player = " + currentPlayer.getName());
        labels[3] = new JLabel("Player 2 time: T");
        scorePanel.setLayout(new FlowLayout(0, 30, 0));
        for (JLabel label : labels) {
            label.setForeground(Color.white);
            scorePanel.add(label);
        }
        scorePanel.setBackground(Color.black);
        scorePanel.setPreferredSize(new Dimension(720, 40));
    }

    public GameMode getMode() {
        return this.mode;
    }

    public void callUndo() {
        mode.undo();
    }

}
