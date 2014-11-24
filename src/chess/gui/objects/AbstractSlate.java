package chess.gui.objects;

import chess.general.Loggable;
import chess.gui.GUIMaster;
import chess.master.Runner;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class AbstractSlate extends Loggable {
    public final static int centerWidth = GUIMaster.SQUARE_WIDTH * 8;
    public final static int headFootHeight = (GUIMaster.WINDOW_HEIGHT - centerWidth) / 2;
    public final static int sideWidth = (GUIMaster.WINDOW_WIDTH - centerWidth) / 2;
    protected final JPanel headerPanel = new JPanel();
    protected final JPanel centerPanel = new JPanel();
    protected final JPanel leftPanel = new JPanel();
    protected final JPanel rightPanel = new JPanel();
    protected final JPanel footerPanel = new JPanel();
    protected final JPanel mainPanel = new JPanel();

    public AbstractSlate(String prefix) {
        super(prefix);

        setPrefSize(headerPanel, new Dimension(GUIMaster.WINDOW_WIDTH, headFootHeight));
        setPrefSize(centerPanel, new Dimension(centerWidth, centerWidth));
        setPrefSize(leftPanel, new Dimension(sideWidth, centerWidth));
        setPrefSize(rightPanel, new Dimension(sideWidth, centerWidth));
        setPrefSize(footerPanel, new Dimension(GUIMaster.WINDOW_WIDTH, headFootHeight));
        setPrefSize(mainPanel, new Dimension(GUIMaster.WINDOW_WIDTH, GUIMaster.WINDOW_HEIGHT));
    }

    protected void panelSetup() {
        setupHeaderPanel();
        setupCenterPanel();
        setupFooterPanel();
        setupLeftPanel();
        setupRightPanel();
        setupMainPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setPrefSize(Component c, Dimension d) {
        c.setMinimumSize(d);
        c.setMaximumSize(d);
        c.setPreferredSize(d);
    }

    public void setDebugColor(Component c) {
        int r = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        c.setBackground(new Color(r, b, g));
    }

    public void onClose() {
        //by default do nothing.
    }

    public void closeFrame() {
        onClose();
        Runner.guiMaster.close();
    }

    protected abstract void setupHeaderPanel();

    protected abstract void setupLeftPanel();

    protected abstract void setupCenterPanel();

    protected abstract void setupRightPanel();

    protected abstract void setupFooterPanel();

    protected void setupMainPanel() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3;
        c.gridy = 0;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(headerPanel, c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(leftPanel, c);

        c.gridx = 1;
        mainPanel.add(centerPanel, c);

        c.gridx = 2;
        mainPanel.add(rightPanel, c);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 3;
        mainPanel.add(footerPanel, c);
    }

}
