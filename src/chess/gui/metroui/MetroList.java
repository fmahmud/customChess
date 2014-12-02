package chess.gui.metroui;

import chess.config.ConfigMaster;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fez on 11/27/14.
 */
public class MetroList extends MetroPanel {
    private JList jlist;
    private DefaultListModel listModel;
    private Dimension dimension, singleButtonDim, twoButtonDim;
    private static final int BUTTON_HEIGHT = 50;

    private JPanel nextButtonPanel;

    public MetroList() {
        super("MetroList");
        defineList();
    }

    private void defineList() {
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.Y_AXIS));
        listModel = new DefaultListModel();
        jlist = new JList(listModel);
        jlist.setBackground(Color.black);
        jlist.setForeground(Color.white);
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist.setFont(ConfigMaster.headerFiveFont);
        JScrollPane scrollPane = new JScrollPane(jlist);
        setRequiredDimension(new Dimension(200, 500));
        canvas.add(scrollPane);
    }

    public String getElementAt(int i) {
        return (String)listModel.get(i);
    }

    private void setRequiredDimension() {
        canvas.setPreferredSize(dimension);
        canvas.setMinimumSize(dimension);
        canvas.setMaximumSize(dimension);
    }

    public void setRequiredDimension(Dimension dim) {
        dimension = dim;
        singleButtonDim = new Dimension(dim.width - 15, BUTTON_HEIGHT);
        twoButtonDim = new Dimension((dim.width / 2) - 10, BUTTON_HEIGHT);
        setRequiredDimension();
    }

    public void append(String s) {
        listModel.addElement(s);
    }

    public void prepend(String s) {
        listModel.add(0, s);
    }

    public JPanel getCanvas() {
        return canvas;
    }

    private JPanel getNextPanel() {
        JPanel toRet = nextButtonPanel;
        if(toRet != null) {
            nextButtonPanel = null;
            return toRet;
        }
        toRet = new JPanel();
        Dimension smallPanelDim = new Dimension(dimension.width, BUTTON_HEIGHT);
        toRet.setPreferredSize(smallPanelDim);
        toRet.setMinimumSize(smallPanelDim);
        toRet.setMaximumSize(smallPanelDim);

        toRet.setLayout(new GridBagLayout());
        nextButtonPanel = toRet;
        canvas.add(nextButtonPanel);
        return toRet;
    }

    public void addButton(MetroButton metroButton) {
        JPanel panel = this.getNextPanel();
        panel.add(metroButton.getCanvas());
        refreshPanel(panel);
    }

    private void refreshPanel(JPanel panel) {
        Component[] components = panel.getComponents();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        if(components.length == 1) {
            panel.removeAll();
            components[0].setPreferredSize(singleButtonDim);
            panel.add(components[0], gbc);
        } else if(components.length == 2) {
            panel.removeAll();
            gbc.gridx = 0;
            components[0].setPreferredSize(twoButtonDim);
            panel.add(components[0], gbc);
            gbc.gridx = 1;
            components[1].setPreferredSize(twoButtonDim);
            panel.add(components[1], gbc);
        }
    }

    public void clear() {
        listModel.clear();
    }

    public void setCellRenderer(DefaultListCellRenderer dlcr) {
        jlist.setCellRenderer(dlcr);
    }

    public void setSelectedIndex(int i) {
        if(listModel.size() > 0 && i < listModel.size())
            jlist.setSelectedIndex(i);
    }

    public void removeElementAt(int i) {
        listModel.remove(i);
    }

    public int getSelectedIndex() {
        return jlist.getSelectedIndex();
    }
}
