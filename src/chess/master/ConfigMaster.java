package chess.master;

import java.awt.*;
import java.io.File;

/**
 * Created by Fez on 11/13/14.
 */
public class ConfigMaster {
    public static File piecesLocation = new File("/Users/Fez/Documents/workspace/ChessProject/src/chess/pieces");
    public static File namesFileLocation = new File("/Users/Fez/Documents/workspace/ChessProject/src/chess/custom/names.txt");

    public static Font defaultFont = new Font("Calibri", Font.PLAIN, 13);
    public static Font textFieldFont = new Font("Calibri", Font.PLAIN, 18);
    public static Font headerFourFont = new Font("Calibri", Font.PLAIN, 27);
    public static Font headerThreeFont = new Font("Calibri", Font.PLAIN, 36);
    public static Font headerTwoFont = new Font("Calibri", Font.PLAIN, 45);
    public static Font headerOneFont = new Font("Calibri", Font.PLAIN, 54);
    public static Font titleFont = new Font("Calibri", Font.BOLD, 81);

    public static Color selectedItem    = new Color( 95, 192, 144     );
    public static Color offendLocation  = new Color(134,   6,   0, 200);
    public static Color moveLocation    = new Color(  0, 151, 237, 200);
    public static Color whiteSquare     = new Color(151, 135, 124     );
    public static Color blackSquare     = new Color( 44,  33,  20     );
    public static Color defaultSquare   = new Color(  0,   0,   0,   0);

    public static Color whiteItem       = new Color(216, 204, 190, 217);
    public static Color blackItem       = new Color(41, 32, 19, 173);
}
