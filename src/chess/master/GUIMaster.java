package chess.master;

import chess.general.Loggable;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fez on 11/12/14.
 */
public class GUIMaster extends Loggable {
    private static AbstractSlate currentSlate;
    private JFrame frame;
    private static Container pane;

    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 1024;

    public GUIMaster(AbstractSlate as) {
        super("GUIMaster");
        currentSlate = as;
        createWindow();
    }

    public static void setCurrentSlate(AbstractSlate as) {
        currentSlate = as;
        addPanelToPane();
    }

    private static void addPanelToPane() {
        pane.removeAll();
        pane.add(currentSlate.getMainPanel());
        pane.validate();
        pane.repaint();
    }

    private void createWindow() {
        frame = new JFrame("Chess!");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        pane = frame.getContentPane();
        addPanelToPane();
        frame.setDefaultCloseOperation(3);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.pack();
        frame.setVisible(true);
    }
}
