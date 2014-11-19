package chess.general;

/**
 * Created by Fez on 11/12/14.
 */
public abstract class Loggable {
    private final static int DEBUG_LEVEL = 0;

    private String debug_prefix;

    public Loggable(String s) {
        debug_prefix = "["+s+"]: ";
    }

    protected void logLine(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.println("["+System.currentTimeMillis()+"]"+debug_prefix+s);
    }

    protected void log(String s, int level) {
        if(level <= DEBUG_LEVEL)
            System.out.print(debug_prefix+s);
    }
}
