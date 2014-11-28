package chess.gui.slates;

import chess.config.ConfigMaster;
import chess.gui.metroui.MetroLabelledTextField;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;

/**
 * This Slate is the pre-game menu. Players choose game mode
 * and input player names etc.
 *
 * Created by Fez on 11/24/14.
 */
public class PreGameSlate extends AbstractSlate {

    JButton buttonNext, buttonBack;
    MetroLabelledTextField[] playerNames;


    public PreGameSlate(AbstractSlate _returnTo) {
        super("PreGameSlate", _returnTo);
        panelSetup();
    }

    @Override
    protected void setupHeaderPanel() {
        JLabel label = new JLabel("Setup your game");
        label.setFont(ConfigMaster.titleFont);
    }

    @Override
    protected void setupLeftPanel() {

    }

    @Override
    protected void setupCenterPanel() {
    }

    @Override
    protected void setupRightPanel() {

    }

    @Override
    protected void setupFooterPanel() {
        JLabel lblsomething = new JLabel(">");
        lblsomething.setFont(ConfigMaster.titleFont);
        footerPanel.add(lblsomething);
    }
}
