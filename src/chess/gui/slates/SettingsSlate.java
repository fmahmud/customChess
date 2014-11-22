package chess.gui.slates;

import chess.general.Common;
import chess.gui.objects.AbstractSlate;
import chess.master.ConfigHandler;
import chess.master.GUIMaster;
import chess.master.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Fez on 11/7/14.
 */
public class SettingsSlate extends AbstractSlate { //abstract a menu class.
    Font buttonFont = new Font("FacitWeb-Regular", Font.PLAIN, 14);
    JButton btnAcceptChanges, btnCancel, btnReset;
    JPanel colorBlackSquare, colorWhiteSquare, colorSelectedSquare,
            colorKillDestination, colorMoveDestination;
    JLabel lblColorBlackSquare, lblColorWhiteSquare, lblColorSelectedSquare,
            lblColorKillDestination, lblColorMoveDestination;
    JLabel lblTitle;
    AbstractSlate slateToReturn;

    public SettingsSlate(AbstractSlate _return) {
        super("Settings");
        slateToReturn = _return;
        btnAcceptChanges = makeButton("Accept", "accept");
        btnCancel = makeButton("Cancel", "cancel");
        btnReset = makeButton("Reset", "reset");
        panelSetup();
    }

    private JButton makeButton(String name, String action) {
        JButton toRet = Common.buttonFactory(name, action, buttonFont);
        ActionListener a = null;
        if(action.equals("accept")) {
            a = new AcceptChangesBtnListener();
        }
        toRet.addActionListener(a);
        return toRet;
    }

    @Override
    protected void setupHeaderPanel() {
        lblTitle = new JLabel("Settings");
        lblTitle.setFont(new Font("FacitWeb-Regular", Font.BOLD, 80));
        headerPanel.add(lblTitle);
    }

    @Override
    protected void setupLeftPanel() {

    }

    @Override
    protected void setupCenterPanel() {
        colorSelectedSquare = new JPanel(null);
        colorSelectedSquare.setBackground(ConfigHandler.selectedItem);
        colorKillDestination = new JPanel(null);
        colorKillDestination.setBackground(ConfigHandler.offendLocation);
        colorMoveDestination = new JPanel(null);
        colorMoveDestination.setBackground(ConfigHandler.moveLocation);
        colorWhiteSquare = new JPanel(null);
        colorWhiteSquare.setBackground(ConfigHandler.whiteSquare);
        colorBlackSquare = new JPanel(null);
        colorBlackSquare.setBackground(ConfigHandler.blackSquare);
        colorSelectedSquare.addMouseListener(new ColorSquareListener(colorSelectedSquare, ConfigHandler.selectedItem));
        colorKillDestination.addMouseListener(new ColorSquareListener(colorKillDestination, ConfigHandler.offendLocation));
        colorMoveDestination.addMouseListener(new ColorSquareListener(colorMoveDestination, ConfigHandler.moveLocation));
        colorWhiteSquare.addMouseListener(new ColorSquareListener(colorWhiteSquare, ConfigHandler.whiteSquare));
        colorBlackSquare.addMouseListener(new ColorSquareListener(colorBlackSquare, ConfigHandler.blackSquare));

        lblColorBlackSquare = new JLabel("Black Square");
        lblColorWhiteSquare = new JLabel("White Square");
        lblColorSelectedSquare = new JLabel("Selected Square");
        lblColorKillDestination = new JLabel("Kill Destination");
        lblColorMoveDestination = new JLabel("Move Destination");
        if(centerPanel == null) {
            logLine("Center panel was null", 0);
        }
        centerPanel.setLayout(null);
        centerPanel.add(colorBlackSquare);
        centerPanel.add(colorWhiteSquare);
        centerPanel.add(colorSelectedSquare);
        centerPanel.add(colorKillDestination);
        centerPanel.add(colorMoveDestination);
        centerPanel.add(lblColorBlackSquare);
        centerPanel.add(lblColorWhiteSquare);
        centerPanel.add(lblColorSelectedSquare);
        centerPanel.add(lblColorKillDestination);
        centerPanel.add(lblColorMoveDestination);
        centerPanel.add(btnAcceptChanges);
        centerPanel.add(btnCancel);
        centerPanel.add(btnReset);

        int firstCol = 210;
        int x = firstCol; int y = 5;
        int width = 64;
        int lblWidth = 150;
        lblColorSelectedSquare.setBounds(x, y, lblWidth, width);
        x += 10 + lblWidth;
        colorSelectedSquare.setBounds(x, y, width, width);
        x = firstCol; y += 5 + width;

        lblColorKillDestination.setBounds(x, y, lblWidth, width);
        x += 10 + lblWidth;
        colorKillDestination.setBounds(x, y, width, width);
        x = firstCol; y += 5 + width;

        lblColorMoveDestination.setBounds(x, y, lblWidth, width);
        x += 10 + lblWidth;
        colorMoveDestination.setBounds(x, y, width, width);
        x = firstCol; y += 5 + width;

        lblColorWhiteSquare.setBounds(x, y, lblWidth, width);
        x += 10 + lblWidth;
        colorWhiteSquare.setBounds(x, y, width, width);
        x = firstCol; y += 5 + width;

        lblColorBlackSquare.setBounds(x, y, lblWidth, width);
        x += 10 + lblWidth;
        colorBlackSquare.setBounds(x, y, width, width);
        x = firstCol-40; y += 20 + width;

        int btnWidth = 90;
        int btnHeight = 20;
        btnReset.setBounds(x, y, btnWidth, btnHeight);
        x += 10 + btnWidth;
        btnCancel.setBounds(x, y, btnWidth, btnHeight);
        x += 10 + btnWidth;
        btnAcceptChanges.setBounds(x, y, btnWidth, btnHeight);
    }

    @Override
    protected void setupRightPanel() {

    }

    @Override
    protected void setupFooterPanel() {

    }

    public class AcceptChangesBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ConfigHandler.selectedItem = colorSelectedSquare.getBackground();
            ConfigHandler.offendLocation = colorKillDestination.getBackground();
            ConfigHandler.moveLocation = colorMoveDestination.getBackground();
            ConfigHandler.whiteSquare = colorWhiteSquare.getBackground();
            ConfigHandler.blackSquare = colorBlackSquare.getBackground();
            Runner.guiMaster.setCurrentSlate(slateToReturn);
        }
    }

    public class ResetBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    public class CancelBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    public class ColorSquareListener implements MouseListener {
        private JPanel square;
        private Color tempColor;
        public ColorSquareListener(JPanel p, Color c) {
            square = p;
            tempColor = c;
        }

        private Color askUserForColor(Color original) {
            Color c = JColorChooser.showDialog(null, "Done", original);
            return c == null? original : c;
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            tempColor = askUserForColor(tempColor);
            square.setBackground(tempColor);
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }
}
