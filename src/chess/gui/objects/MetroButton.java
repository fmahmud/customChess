package chess.gui.objects;

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
public class MetroButton {
    private static final int BORDER_WIDTH = 1;
    private JLabel label;
    private JPanel canvas;
    private Vector<ActionListener> listeners;
    private Color foregroundColor, backgroundColor, borderColor;

    public MetroButton(String _s, Color _bg, Color _fg, Color _border) {
        foregroundColor = _fg;
        backgroundColor = _bg;
        borderColor = _border;
        listeners = new Vector<ActionListener>();

        canvas = new JPanel();

        canvas.setLayout(new BoxLayout(canvas, BoxLayout.Y_AXIS));
        canvas.setBackground(_bg);

        label = new JLabel(_s);
        label.setForeground(_fg);
        label.setAlignmentX(0.5f);
        label.setFont(ConfigMaster.headerFiveFont);
        canvas.add(Box.createVerticalGlue());
        canvas.add(label, BorderLayout.CENTER);
        canvas.add(Box.createVerticalGlue());

        canvas.setBorder(BorderFactory.createMatteBorder(
                BORDER_WIDTH, BORDER_WIDTH,
                BORDER_WIDTH, BORDER_WIDTH,
                _border
        ));

        MouseListener ms = new MetroButtonListener();
        canvas.addMouseListener(ms);
        label.addMouseListener(ms);

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

    public void setRequiredDimension(Dimension dim) {
        canvas.setPreferredSize(dim);
        canvas.setMinimumSize(dim);
        canvas.setMaximumSize(dim);
    }


    private class MetroButtonListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            System.out.println("Was pressed");
            for(ActionListener al : listeners) {
                al.actionPerformed(new ActionEvent(mouseEvent.getSource(), mouseEvent.getID(), ""));
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            canvas.setBackground(ConfigMaster.selectedItem);
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
