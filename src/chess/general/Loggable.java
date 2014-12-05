package chess.general;

import chess.config.ConfigMaster;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class Loggable {
    private final static int DEBUG_LEVEL = 0;
    private final static int NUM_NAMES = 1000;
    private static HashSet<String> availableNames;

    private String debugPrefix;
//    private String name;

    public Loggable(String s) {
        debugPrefix = "[" + String.format("%-30s", s) + "]: ";
        defineLogger();
//        logLine("Spawned "+s, 0);
    }

    public static String getName() {
        String toRet = availableNames.iterator().next();
        availableNames.remove(toRet);
        return toRet;
    }

    private void defineLogger() {
        if (availableNames == null) {
            try {
                availableNames = Common.getNRandomUniqueLinesInFile(NUM_NAMES, ConfigMaster.namesFileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        debugPrefix = "[" + String.format("%-30s", getName()) + "]" + debugPrefix;
    }

//    protected void logAction(String actionName, String parameters, )

    protected void logLine(String s, int level) {
        if (level <= DEBUG_LEVEL)
            System.out.println("[" + System.currentTimeMillis() + "]" + debugPrefix + s);
    }

    protected void log(String s, int level) {
        if (level <= DEBUG_LEVEL)
            System.out.print(s);
    }
}
