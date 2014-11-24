package chess.master;

import chess.general.Loggable;
import chess.gui.objects.AbstractSlate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Fez on 11/12/14.
 */
public class GUIMaster extends Loggable {
    private static AbstractSlate currentSlate;
    private JFrame frame;
    private static Container pane;

    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 1024;
    public static final int SQUARE_WIDTH = 84;

    public GUIMaster(AbstractSlate as) {
        super("GUIMaster");
        currentSlate = as;
        createWindow();
    }

    public void close() {
        logLine("Closing!", 0);
        Runner.pieceLibrary.saveAllPieces();
        frame.dispose();
    }

    public void setCurrentSlate(AbstractSlate as) {
        currentSlate = as;
        addPanelToPane();
    }

    private void addPanelToPane() {
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
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                currentSlate.closeFrame();
            }
        });
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.pack();
        frame.setVisible(true);
    }
}
