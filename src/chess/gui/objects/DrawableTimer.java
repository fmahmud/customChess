package chess.gui.objects;

import chess.general.Loggable;

import javax.swing.*;

/**
 * Created by Fez on 11/24/14.
 */
public class DrawableTimer extends Loggable {
    JPanel canvas;
    JLabel time, title;
    Timer t;

    public DrawableTimer(String title) {
        super(title);
    }
}
