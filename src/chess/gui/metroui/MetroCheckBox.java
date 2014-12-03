package chess.gui.metroui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Fez on 12/2/14.
 */
public class MetroCheckBox extends MetroPanel {
    private boolean isChecked = false;
    private Dimension dimension = new Dimension(40, 40);
    private JPanel selectedOverlay;

    public MetroCheckBox(String s) {
        super(s);
        canvas.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
        canvas.setLayout(null);
        selectedOverlay = new JPanel();
        selectedOverlay.setBackground(Color.white);
        selectedOverlay.setPreferredSize(new Dimension(26, 26));
        canvas.add(selectedOverlay);
        selectedOverlay.setBounds(7, 7, 26, 26);
        selectedOverlay.setVisible(false);
        canvas.addMouseListener(new CheckedActionListener());
        setRequiredDimension(dimension);
    }

    class CheckedActionListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            isChecked = !isChecked;
            selectedOverlay.setVisible(isChecked);
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
