package chess.game.objects;

import chess.config.ConfigMaster;
import chess.gui.objects.DrawableObject;

import java.awt.*;

/**
 * Created by Fez on 11/13/14.
 */
public class Event extends DrawableObject {

    private int moveType = 0; //1 = move, 2 = kill.

    //1 = check, 2 = stale, 3 = checkmate, 4 = castle, 5 = enpassant?
    private int effectType = 0;

    private Square origin, destination;
    private Piece victim, offender;
    private String gameTime;
    private int tCount = -1; //the turn count

    private static Color oddEvent = new Color(255, 255, 255, 40);
    private static Color evenEvent = new Color(255, 255, 255, 0);

    public Event(Square _or, Square _d,
                 Piece _v, Piece _o,
                 int _mt, int _et,
                 int _tc,
                 String _gt) {
        super("Event(" + _tc + ")");
        origin = _or;
        destination = _d;
        victim = _v;
        offender = destination.getPiece();
        tCount = _tc;
        moveType = _mt;
        effectType = _et;
        gameTime = _gt;
        canvas.setPreferredSize(new Dimension(100, 70));
        this.setUnselectedColors();
    }

    public void setSelectedColors() {
        canvas.setBackground(ConfigMaster.selectedItemColor);
    }

    public void setUnselectedColors() {
        if (tCount % 2 == 1) {
            canvas.setBackground(oddEvent);
        } else {
            canvas.setBackground(evenEvent);
        }
        canvas.setForeground(Color.white);
    }

    private String getActionEffect() {
        String s = "";
        if (moveType == 1) {
            s += "moved " + offender.getPieceName() + " from<br>"
                    + origin.getCoordinatesAsString() + " to "
                    + destination.getCoordinatesAsString() + ". ";
        } else {
            s += "killed " + victim.getPieceName() + " at "
                    + destination.getCoordinatesAsString() + " with<br>"
                    + offender.getPieceName() + " from "
                    + origin.getCoordinatesAsString() + ". ";
        }
        return s + getStringFromEffect(effectType);
    }

    public static String getStringFromEffect(int et) {
        String s = "";
        switch (et) {
            case 1:
                s += "Check!";
                break;
            case 2:
                s += "Stalemate!";
                break;
            case 3:
                s += "Checkmate!";
                break;
            case 4:
                s += "Castled.";
                break;
            case 5:
                s += "Enpassant.";
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
        StringBuilder b = new StringBuilder();
        b.append("[");
        b.append(gameTime);
        b.append("][");
        b.append(tCount);
        b.append("]: ");
        b.append(offender.getOwner().getPlayerName());
        b.append("<br>");
        b.append(getActionEffect());
        return b.toString();
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
        g2.setFont(ConfigMaster.headerFiveFont);
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
