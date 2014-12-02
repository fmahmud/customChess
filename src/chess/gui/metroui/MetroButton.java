package chess.gui.metroui;

import chess.config.ConfigMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * Created by Fez on 11/25/14.
 */
public class MetroButton extends MetroPanel {
    private static final int BORDER_WIDTH = 1;
    private JLabel label;
    private Vector<ActionListener> listeners;
    private Color foregroundColor, backgroundColor, borderColor;

    public MetroButton(String _s) {
        super(_s);
        foregroundColor = Color.white;
        backgroundColor = ConfigMaster.moveLocationColor;
        borderColor = Color.white;

        listeners = new Vector<ActionListener>();
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.Y_AXIS));

        label = new JLabel(_s);
        label.setAlignmentX(0.5f);
        label.setFont(ConfigMaster.headerFiveFont);

        canvas.add(Box.createVerticalGlue());
        canvas.add(label, BorderLayout.CENTER);
        canvas.add(Box.createVerticalGlue());

        updateColors();

        MouseListener ms = new MetroButtonListener();
        canvas.addMouseListener(ms);

    }

    private void updateColors() {
        canvas.setBackground(backgroundColor);
        canvas.setBorder(BorderFactory.createMatteBorder(
                BORDER_WIDTH, BORDER_WIDTH,
                BORDER_WIDTH, BORDER_WIDTH,
                borderColor
        ));
        label.setForeground(foregroundColor);
    }

    public void setBackgroundColor(Color c) {
        backgroundColor = c;
    }

    public void setForegroundColor(Color c) {
        foregroundColor = c;
    }

    public void setBorderColor(Color c) {
        borderColor = c;
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public void addActionListener(ActionListener al) {
        listeners.add(al);
    }

    public void setBackground(Color _bg) {
        canvas.setBackground(_bg);
    }

    public void setText(String s) {
        label.setText(s);
    }

    private class MetroButtonListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            for(ActionListener al : listeners) {
                al.actionPerformed(new ActionEvent(mouseEvent.getSource(), mouseEvent.getID(), ""));
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            canvas.setBackground(ConfigMaster.selectedItemColor);
            label.setForeground(Color.gray);
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            canvas.setBackground(backgroundColor);
            label.setForeground(Color.white);
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }
}
