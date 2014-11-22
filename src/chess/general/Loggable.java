package chess.general;

import chess.master.ConfigHandler;

import java.io.IOException;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class Loggable {
    private final static int DEBUG_LEVEL = 0;
    private static Vector<String> namesInUse = new Vector<String>();

    private String debugPrefix;
    private String name;

    public Loggable(String s) {
        debugPrefix = "["+s+"]: ";
        try {
            while(namesInUse.contains(
                    name = Common.getRandomLineInFile(ConfigHandler.namesFileLocation)))
            { /*spin till new name found.*/ }
            namesInUse.add(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void logLine(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.println("["+System.currentTimeMillis()+"]"+ "[" + name + "]" + debugPrefix +s);
    }

    protected void log(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.print(debugPrefix +s);
    }
}
