package chess.gui.slates;

import chess.baseObjects.Game;
import chess.baseObjects.GameMode;
import chess.custom.Faction;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;


/**
 * Created by Fez on 9/17/14.
 *
 */
public class PlayGameSlate extends AbstractSlate {
    private Game game;

    public JPanel getPnlUpper() {
        return headerPanel;
    }

    public JPanel getPnlLower() {
        return centerPanel;
    }

    public PlayGameSlate() {
        super("PlayGameSlate");
        //usually would invoke a choice of game style
        //then with the game style create a new game
        //and then the game would create a new board.
        //todo new game menu fixing.
        game = new Game(new GameMode("Classic Chess", 2,
                new int[] {1, 1},
                new Faction[][] {
                        new Faction[] { Faction.WHITE },
                        new Faction[] { Faction.BLACK }
                }, new JPanel[]{
                    headerPanel, centerPanel, rightPanel, leftPanel, footerPanel

        }
        ));
        panelSetup();
    }

    @Override
    public void onClose() {
        game.endGame();
    }

    @Override
    protected void setupHeaderPanel() {
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        game.setHeaderPanel(headerPanel);
    }

    @Override
    protected void setupLeftPanel() {
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        game.setLeftPanel(leftPanel);
    }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        game.setCenterPanel(centerPanel);
    }

    @Override
    protected void setupRightPanel() {
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        game.setRightPanel(rightPanel);
    }

    @Override
    protected void setupFooterPanel() {
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        game.setFooterPanel(footerPanel);
    }
}