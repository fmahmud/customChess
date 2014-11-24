package chess.general;

import chess.config.ConfigMaster;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class Loggable {
    private final static int DEBUG_LEVEL = 0;
    private final static int NUM_LOGGERS = 600;
    private static HashSet<String> availableNames;

    private String debugPrefix;
    private String name;
    public Loggable(String s) {
        debugPrefix = "["+s+"]: ";
        define();
    }

    private void define() {
        if(availableNames == null) {
            try {
                availableNames = Common.getNRandomUniqueLinesInFile(NUM_LOGGERS, ConfigMaster.namesFileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        name = availableNames.iterator().next();
        availableNames.remove(name);
    }

//    protected void logAction(String actionName, String parameters, )

    protected void logLine(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.println("["+System.currentTimeMillis()+"]"+ "[" + name + "]" + debugPrefix +s);
    }

    protected void log(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.print(debugPrefix +s);
    }
}
