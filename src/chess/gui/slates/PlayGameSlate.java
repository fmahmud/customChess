package chess.gui.slates;

import chess.baseObjects.Game;
import chess.gamemodes.ClassicChess;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;
import java.io.File;


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
        game = new Game(new ClassicChess());
        panelSetup();
    }

    @Override
    public void onClose() {

    }

    @Override
    protected void setupHeaderPanel() {
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.add(game.getHeaderPanel());
    }

    @Override
    protected void setupLeftPanel() {
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(game.getLeftPanel());
    }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(game.getCenterPanel());
    }

    @Override
    protected void setupRightPanel() {
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(game.getRightPanel());
    }

    @Override
    protected void setupFooterPanel() {
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.add(game.getFooterPanel());
    }
}