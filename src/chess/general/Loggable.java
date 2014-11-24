package chess.general;

import chess.master.ConfigMaster;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class Loggable {
    private final static int DEBUG_LEVEL = 0;
    private static HashSet<String> availableNames;

    private String debugPrefix;
    private String name;

    private static int numLoggers = 0;

    public Loggable(String s) {
        debugPrefix = "["+s+"]: ";
        if(availableNames == null) {
            try {
                availableNames = Common.getNRandomUniqueLinesInFile(500, ConfigMaster.namesFileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        name = availableNames.iterator().next();
        availableNames.remove(name);
        numLoggers++;
        System.out.println(debugPrefix+" is logger number "+numLoggers);
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
