package chess.game.objects;

import chess.config.ConfigMaster;
import chess.general.Common;
import chess.gui.objects.DrawableObject;

import java.awt.*;

/**
 * Created by Fez on 11/13/14.
 */
public class Event extends DrawableObject {

    private int moveType = 0; //1 = move, 2 = kill.
    private int effectType = 0; //1 = check, 2 = stale, 3 = checkmate
    //4 = castle, 5 = enpassant?
    private Square origin, destination;
    private Piece victim, offender;
    private String gameTime;
    private int tCount = -1; //the turn count

    public Event(Square _or, Square _d,
                 Piece _v, Piece _of,
                 int _mt, int _et,
                 int _tc,
                 String _gt) {
        super("Event(" + _tc + ")");
        origin = _or;
        destination = _d;
        victim = _v;
        offender = _of;
        tCount = _tc;
        moveType = _mt;
        effectType = _et;
        gameTime = _gt;
        canvas.setPreferredSize(new Dimension(100, 70));
        this.setUnselectedColors();
    }

    public void setSelectedColors() {
        canvas.setBackground(ConfigMaster.selectedItem);
        canvas.setForeground(new Color(0, 0, 0));
    }

    public void setUnselectedColors() {
        if (tCount % 2 == 1) {
            canvas.setBackground(ConfigMaster.whiteItem);
            canvas.setForeground(ConfigMaster.blackItem);
        } else {
            canvas.setBackground(ConfigMaster.blackItem);
            canvas.setForeground(ConfigMaster.whiteItem);
        }
    }

    private String getActionEffect() {
        String s = "";
        if (moveType == 1) {
            s += "moved " + offender.getPieceName() + " from<br>"
                    + origin.getCoordinatesAsString() + " to "
                    + destination.getCoordinatesAsString() + ".";
        } else {
            s += "killed " + victim.getPieceName() + " at "
                    + destination.getCoordinatesAsString() + " with<br>"
                    + offender.getPieceName() + " from "
                    + origin.getCoordinatesAsString() + ".";
        }

        switch (effectType) {
            case 1:
                s += " Check!";
                break;
            case 2:
                s += " Stalemate!";
                break;
            case 3:
                s += " Checkmate!";
                break;
            case 4:
                s += " Castled.";
                break;
            case 5:
                s += " Enpassant.";
                break;
            default:
                break;
        }
        return s;
    }


    /**
     * Should return the event in string form.
     *
     * @return
     */
    @Override
    public String toString() {
        return "[" + gameTime + "][" + tCount + "]: " + offender.getOwner().getName() + "<br>"
                + getActionEffect() + "";
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
        String[] toDraw = this.toString().split("<br>");
        int y = 20;
        for (String s : toDraw) {
            g2.drawString(s, 5, y);
            y += 20;
        }
    }

    public int getMoveType() {
        return moveType;
    }

    public int getEffectType() {
        return effectType;
    }

    public Piece getVictim() {
        return victim;
    }

    public Piece getOffender() {
        return offender;
    }
}
