//package chess.gamemodes;
//
//import chess.baseObjects.*;
//import chess.baseObjects.Timer;
//import chess.custom.Faction;
//import chess.general.Common;
//import chess.gui.objects.DrawableBoard;
//import chess.master.ConfigHandler;
//import chess.master.Runner;
//import org.json.JSONObject;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Vector;
//
///**
// * Particular type of <code>GameMode</code>. This class sets the game up
// * to play a standard 1v1 game of chess.
// */
//public class ClassicChess extends GameMode {
//
//
//
//    /**
//     * Constructor for the <code>ClassicChess</code> <code>GameMode</code>.
//     * Instantiates the two factions: black and white, the <code>Pathfinder</code>,
//     * the <code>Board</code> object, and passes that to the instance of <code>DrawableBoard</code>.
//     */
//    public ClassicChess() {
//        super("Classic Chess", 2, new int[] {1, 1},
//                new Faction[][] {
//                    new Faction[] { Faction.WHITE },
//                    new Faction[] { Faction.BLACK }
//                }
//        );
//
//    }
//
//}
