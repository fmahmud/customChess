package chess.gui.objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Fez on 11/25/14.
 */
public class LabelledTextField {
    JPanel canvas;
    JLabel label;
    JTextField textField;
    String defaultText;

    public LabelledTextField(String lblText, String defText, Dimension pnlDim) {
        canvas = new JPanel();
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.X_AXIS));
        label = new JLabel(lblText);
        textField = new JTextField(20);
        textField.setText(defaultText = defText);
        textField.addFocusListener(new TextFieldFocusListener());
        canvas.setPreferredSize(pnlDim);
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
        textField.setText(s);
    }

    private class TextFieldFocusListener implements FocusListener {
        boolean hasBeenFocused = false;
        @Override
        public void focusGained(FocusEvent focusEvent) {
            if(hasBeenFocused) {
                textField.selectAll();
            } else {
                textField.setText("");
                textField.setForeground(Color.black);
            }
            hasBeenFocused = true;
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            if(textField.getText().equals("")) {
                textField.setText(defaultText);
                textField.setForeground(Color.gray);
                hasBeenFocused = false;
            }
        }
    }
}
