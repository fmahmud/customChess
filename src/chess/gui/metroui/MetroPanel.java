package chess.gui.metroui;

import chess.general.Loggable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fez on 11/28/14.
 */
public class MetroPanel extends Loggable {

    protected JPanel canvas;

    public MetroPanel(String s) {
        super(s);
        this.definePanel();
    }

    private void definePanel() {
        canvas = new JPanel();
        canvas.setBackground(Color.black);
    }

    public JPanel getCanvas() {
        return canvas;
    }
}
