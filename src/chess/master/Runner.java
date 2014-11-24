package chess.master;

import chess.config.ConfigMaster;
import chess.curator.Curator;
import chess.general.Loggable;
import chess.gui.GUIMaster;
import chess.gui.slates.MainMenuSlate;

import javax.swing.*;

public class Runner extends Loggable {
    public static GUIMaster guiMaster;
    public static Curator pieceLibrary;

    public Runner() {
        super("Runner");
        pieceLibrary = new Curator("Pieces", "name");
        pieceLibrary.addDirectory(ConfigMaster.piecesLocation);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        } catch (UnsupportedLookAndFeelException e) {

        }
        Runner r = new Runner();
        r.run();
    }

    private void run() {
        guiMaster = new GUIMaster(new MainMenuSlate());
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
