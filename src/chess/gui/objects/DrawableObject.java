package chess.gui.objects;

import chess.general.Loggable;

import javax.swing.*;
import java.awt.*;

public abstract class DrawableObject extends Loggable {
    protected JPanel canvas;

    public DrawableObject(String prefix) {
        super(prefix);
        canvas = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                prerender(g);
            }
        };
    }

    public void repaint() {
        canvas.repaint();
    }

    private void prerender(Graphics g) {
        render(g);
    }

    public abstract void render(Graphics g);

    public JPanel getCanvas() {
        return canvas;
    }


}
