package chess.baseObjects;

import chess.general.Common;
import chess.master.ConfigMaster;

import java.awt.*;

/**
 * Created by Fez on 11/13/14.
 */
public class Event extends DrawableObject {

    private int moveType = 0; //1 = move, 2 = kill.
    private int effectType = 0; //1 = check, 2 = stale, 3 = checkmate
    //4 = castle, 5 = enpassant?
    private int whiteTime, blackTime;
    private Square origin, destination;
    private Piece victim, offender;
    private int tCount = -1; //the turn count

    public Event(Square _or, Square _d,
                 Piece _v, Piece _of,
                 int _mt, int _et,
                 int _tc,
                 int _wt, int _bt) {
        super("Event("+_tc+")");
        origin = _or;
        destination = _d;
        victim = _v;
        offender = _of;
        tCount = _tc;
        moveType = _mt;
        effectType = _et;
        whiteTime = _wt;
        blackTime = _bt;
        canvas.setMinimumSize(new Dimension(canvas.getPreferredSize().width, 50));
        this.setUnselectedColors();
    }

    public void setSelectedColors() {
        canvas.setBackground(ConfigMaster.selectedItem);
        canvas.setForeground(new Color(0, 0, 0));
    }

    public void setUnselectedColors() {
        if(tCount % 2 == 1) {
            canvas.setBackground(ConfigMaster.whiteItem);
            canvas.setForeground(ConfigMaster.blackItem);
        } else {
            canvas.setBackground(ConfigMaster.blackItem);
            canvas.setForeground(ConfigMaster.whiteItem);
        }
    }


    /**
     * Should return the event in string form.
     * @return
     */
    @Override
    public String toString() {
        //todo: Implement this function.
        return "Turn = "+tCount+"";
    }

    public Square getOrigin() {
        return origin;
    }

    public Square getDestination() {
        return destination;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(Common.buttonFont);
        g2.drawString(this.toString(), 5, 20);
    }

    public int getMoveType() {
        return moveType;
    }

    public int getEffectType() {
        return effectType;
    }

    public long getWhiteTime() {
        return whiteTime;
    }

    public long getBlackTime() {
        return blackTime;
    }

    public Piece getVictim() {
        return victim;
    }

    public Piece getOffender() {
        return offender;
    }
}
