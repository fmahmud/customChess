package chess.gui.slates;

import chess.game.objects.GameMode;
import chess.general.Common;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;
import java.io.File;


/**
 * Created by Fez on 9/17/14.
 */
public class PlayGameSlate extends AbstractSlate {
    private GameMode game;

    public PlayGameSlate(AbstractSlate _returnTo) {
        super("PlayGameSlate", _returnTo);
        //usually would invoke a choice of game style
        //then with the game style create a new game
        //and then the game would create a new board.
        //todo new game menu fixing.
        File classicChess = new File("/Users/Fez/Documents/workspace/ChessProject/src/chess/gamemodes/classicchess.json");
        game = new GameMode(Common.getJSONObjFromFile(classicChess));
        panelSetup();
        game.setHeaderPanel(headerPanel);
        game.setLeftPanel(leftPanel);
        game.setCenterPanel(centerPanel);
        game.setRightPanel(rightPanel);
        game.setFooterPanel(footerPanel);
        game.setupPanels();
    }

    public JPanel getPnlUpper() {
        return headerPanel;
    }

    public JPanel getPnlLower() {
        return centerPanel;
    }

    @Override
    public void onClose() {
        game.endGame();
    }

    @Override
    protected void setupHeaderPanel() {
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void setupLeftPanel() {
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void setupRightPanel() {
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void setupFooterPanel() {
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
    }
}