package chess.gui.slates;

import chess.config.ConfigMaster;
import chess.gui.objects.AbstractSlate;
import chess.gui.objects.LabelledTextField;

import javax.swing.*;

/**
 * This Slate is the pre-game menu. Players choose game mode
 * and input player names etc.
 *
 * Created by Fez on 11/24/14.
 */
public class PreGameSlate extends AbstractSlate {

    JButton buttonNext, buttonBack;
    LabelledTextField[] playerNames;


    public PreGameSlate(AbstractSlate _returnTo) {
        super("PreGameSlate", _returnTo);
        panelSetup();
    }

    @Override
    protected void setupHeaderPanel() {

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
