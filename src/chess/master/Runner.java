package chess.master;

import chess.config.ConfigMaster;
import chess.general.Curator;
import chess.general.Loggable;
import chess.gui.GUIMaster;
import chess.gui.slates.MainMenuSlate;

import javax.swing.*;

public class Runner extends Loggable {
    public static GUIMaster guiMaster;
    public static Curator pieceCollection, gameCollection;

    public Runner() {
        super("Runner");
        log("Defining Curators!", 0);
        pieceCollection = new Curator("Pieces", "name");
        pieceCollection.addDirectory(ConfigMaster.piecesLocation);
        gameCollection = new Curator("GameModes", "key");
        gameCollection.addDirectory(ConfigMaster.gameModesLocation);
        log("...Done!\n", 0);
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
     *      - Allow board to be flipped for pvp/orient board so lower is player
     *      - done: Custom Piece maker
     *      - (advanced) AI? for suggestion and playing against
     */
}
