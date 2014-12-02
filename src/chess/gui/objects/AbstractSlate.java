package chess.gui.objects;

import chess.general.Loggable;
import chess.gui.GUIMaster;
import chess.gui.metroui.MetroButton;
import chess.gui.metroui.MetroPanel;
import chess.master.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class AbstractSlate extends Loggable {
    public final static int centerWidth = GUIMaster.SQUARE_WIDTH * 8;
    public final static int headFootHeight = (GUIMaster.WINDOW_HEIGHT - centerWidth) / 2;
    public final static int sideWidth = (GUIMaster.WINDOW_WIDTH - centerWidth) / 2;

    private final MetroPanel headerMPanel = new MetroPanel("Header Panel");
    private final MetroPanel centerMPanel = new MetroPanel("Center Panel");
    private final MetroPanel leftMPanel = new MetroPanel("Left Panel");
    private final MetroPanel rightMPanel = new MetroPanel("Right Panel");
    private final MetroPanel mainMPanel = new MetroPanel("Main Panel");
    private final MetroPanel footerMPanel = new MetroPanel("Footer Panel");

    protected final JPanel headerPanel = headerMPanel.getCanvas();
    protected final JPanel centerPanel = centerMPanel.getCanvas();
    protected final JPanel leftPanel = leftMPanel.getCanvas();
    protected final JPanel rightPanel = rightMPanel.getCanvas();
    protected final JPanel footerPanel = footerMPanel.getCanvas();
    protected final JPanel mainPanel = mainMPanel.getCanvas();

    private AbstractSlate returnTo;

    public AbstractSlate(String prefix, AbstractSlate _returnTo) {
        super(prefix);
        returnTo = _returnTo;
        headerMPanel.setRequiredDimension(new Dimension(GUIMaster.WINDOW_WIDTH, headFootHeight));
        centerMPanel.setRequiredDimension(new Dimension(centerWidth, centerWidth));
        leftMPanel.setRequiredDimension(new Dimension(sideWidth, centerWidth));
        rightMPanel.setRequiredDimension(new Dimension(sideWidth, centerWidth));
        footerMPanel.setRequiredDimension(new Dimension(GUIMaster.WINDOW_WIDTH, headFootHeight));
        mainMPanel.setRequiredDimension(new Dimension(GUIMaster.WINDOW_WIDTH, GUIMaster.WINDOW_HEIGHT));
    }

    protected void panelSetup() {
        setupHeaderPanel();
        setupCenterPanel();
        setupFooterPanel();
        setupLeftPanel();
        setupRightPanel();
        setupMainPanel();
    }

    protected void returnToPreviousSlate() {
        Runner.guiMaster.setCurrentSlate(returnTo);
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

    protected MetroButton makeMetroButton(String s, ActionListener al) {
        MetroButton toRet = new MetroButton(s);
        toRet.setRequiredDimension(new Dimension(200, 80));
        toRet.addActionListener(al);
        return toRet;
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
