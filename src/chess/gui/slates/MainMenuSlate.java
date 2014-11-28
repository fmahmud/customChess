package chess.gui.slates;

import chess.gui.metroui.MetroButton;
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
        headerPanel.setBackground(Color.black);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel lblLogo = new JLabel("Chess!");
        lblLogo.setAlignmentX(0.5f);
        lblLogo.setFont(new Font("FacitWeb-Regular", Font.BOLD, 80));
        lblLogo.setForeground(Color.white);
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalGlue());
    }

    @Override
    protected void setupLeftPanel() {
        leftPanel.setBackground(Color.black);

    }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setBackground(Color.black);
        MetroButton newGameMBtn = makeMetroButton("New Game", new NewGameListener());
        MetroButton loadGameMBtn = makeMetroButton("Load Game", new LoadGameListener());
        MetroButton builderMBtn = makeMetroButton("Piece Builder", new BuilderListener());
        MetroButton settingsMBtn = makeMetroButton("Settings", new SettingsListener());
        MetroButton exitMBtn = makeMetroButton("Exit", new ExitListener());


        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(newGameMBtn.getCanvas());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(loadGameMBtn.getCanvas());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(builderMBtn.getCanvas());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(settingsMBtn.getCanvas());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(exitMBtn.getCanvas());
        centerPanel.add(Box.createVerticalGlue());

    }

    @Override
    protected void setupRightPanel() {
        rightPanel.setBackground(Color.black);

    }

    @Override
    protected void setupFooterPanel() {
        footerPanel.setBackground(Color.black);

    }


    public class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
//            Runner.guiMaster.setCurrentSlate(new PreGameSlate(MainMenuSlate.this));
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

    public class ExitListener implements ActionListener {

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
