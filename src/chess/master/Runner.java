package chess.master;

import chess.general.Loggable;
import chess.gui.slates.MainMenuSlate;

import javax.swing.*;

public class Runner extends Loggable {
    MainMenuSlate menu;
    GUIMaster guiMaster;

    public Runner() {
        super("Runner");
        menu = new MainMenuSlate();
        guiMaster = new GUIMaster(menu);
    }

    private void run() {
        menu = new MainMenuSlate();
    }
    
    public static void main(String[] args) {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}

        Runner r = new Runner();
        r.run();
    }

    /**
     *
     * idea list:
     *      - done: Color board with possible destinations
     *      - Allow board to be flipped for pvp
     *      - Various game modes like bug house
     *      - Custom Piece maker
     *          - Various kill styles
     *          - Various special abilities?
     *          - Various move styles
     *              - wrapping
     *              - color only
     *              - multi-step moves
     *              - move to double occupy square?
     *              - weirder yet?
     *              - kill while moving
     *      - traps/contingencies?
     *      - (advanced) AI? for suggestion and playing against
     *      - fat pieces? take up more space?
     */
}
