package chess.gui.slates;

import chess.general.Common;
import chess.gui.objects.AbstractSlate;
import chess.master.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Fez on 9/15/14.
 */


public class MainMenuSlate extends AbstractSlate {

    /**
     * Define a color palette for the displayed board.
     * Specific per game mode therefore should be in each implementation
     */

    public MainMenuSlate() {
        super("MainMenu", null);
        panelSetup();
    }

    @Override
    protected void setupHeaderPanel() {
        JLabel lblLogo = new JLabel("Chess!");
        lblLogo.setFont(new Font("FacitWeb-Regular", Font.BOLD, 80));
        headerPanel.add(lblLogo);
    }

    @Override
    protected void setupLeftPanel() {

    }

    @Override
    protected void setupCenterPanel() {
        JButton btnNewGame = makeButton("New Game", "newGame");
        JButton btnLoadGame = makeButton("Load Game", "loadGame");
        JButton btnQuit = makeButton("Quit", "quit");
        JButton btnSettings = makeButton("Settings", "settings");
        JButton btnBuilder = makeButton("Builder", "builder");
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 0;
        c.ipadx = 20;
        c.ipady = 20;

        c.gridy++;
        centerPanel.add(btnNewGame, c);

        c.gridy++;
        centerPanel.add(btnLoadGame, c);

        c.gridy++;
        centerPanel.add(btnBuilder, c);

        c.gridy++;
        centerPanel.add(btnSettings, c);

        c.gridy++;
        centerPanel.add(btnQuit, c);

        btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void setupRightPanel() {

    }

    @Override
    protected void setupFooterPanel() {

    }

    private JButton makeButton(String name, String action) {
        JButton b = Common.buttonFactory(name, action, Common.buttonFont);
        ActionListener a = null;
        if (action.equals("newGame")) {
            a = new NewGameListener();
        } else if (action.equals("quit")) {
            a = new QuitListener();
        } else if (action.equals("loadGame")) {
            a = new LoadGameListener();
        } else if (action.equals("settings")) {
            a = new SettingsListener();
        } else if (action.equals("builder")) {
            a = new BuilderListener();
        }
        b.addActionListener(a);
        return b;
    }

    public class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Runner.guiMaster.setCurrentSlate(new PlayGameSlate(MainMenuSlate.this));
        }
    }

    public class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    public class SettingsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Runner.guiMaster.setCurrentSlate(new SettingsSlate(MainMenuSlate.this));
        }
    }

    public class QuitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            closeFrame();
        }
    }

    private class BuilderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Runner.guiMaster.setCurrentSlate(new PieceBuilderSlate(MainMenuSlate.this));
        }
    }
}
