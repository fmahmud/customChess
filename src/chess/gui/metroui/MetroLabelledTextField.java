package chess.gui.metroui;

import chess.config.ConfigMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Fez on 11/25/14.
 */
public class MetroLabelledTextField extends MetroPanel {
    private JLabel label;
    private JTextField textField;
    private String defaultText;

    public MetroLabelledTextField(String lblText, String defText, Dimension pnlDim) {
        super("mlbltf("+lblText+")");
        defineMLTF(lblText, defText, pnlDim);
    }

    private void defineMLTF(String lblText, String defText, Dimension pnlDim) {
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.X_AXIS));

        label = new JLabel(lblText);
        label.setForeground(Color.white);
        label.setFont(ConfigMaster.headerFiveFont);

        textField = new JTextField(20);
        textField.setForeground(Color.gray);
        textField.setFont(ConfigMaster.headerFiveFont);
        textField.setText(defaultText = defText);
        textField.addFocusListener(new TextFieldFocusListener());

        canvas.setPreferredSize(pnlDim);
        canvas.add(Box.createHorizontalStrut(5));
        canvas.add(label);
        canvas.add(textField);
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public void setFont(Font f) {
        textField.setFont(f);
        label.setFont(new Font(f.getName(), f.getStyle(), f.getSize()-2));
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String s) {
        textFieldHasBeenFocused = true;
        textField.setText(s);
    }

    public void setEditable(boolean b) {
        textField.setEditable(b);
    }

    boolean textFieldHasBeenFocused = false;

    private class TextFieldFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent focusEvent) {
            if(textFieldHasBeenFocused) {
                textField.selectAll();
            } else {
                textField.setText("");
                textField.setForeground(Color.black);
            }
            textFieldHasBeenFocused = true;
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            if(textField.getText().equals("")) {
                textField.setText(defaultText);
                textField.setForeground(Color.gray);
            }
        }
    }
}
