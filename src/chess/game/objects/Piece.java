package chess.game.objects;

import chess.gui.objects.DrawableObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class Piece extends DrawableObject {
    private static final int IMG_X_OFFSET = 10;
    private static final int IMG_Y_OFFSET = 10;
    protected Image img;
    private Vector<MoveStyle> moveStyles;
    private MoveDestinations moveDestinations;
    private int currentRow = -1, currentColumn = -1;
    private String pieceName;
    private String imagePath;
    private boolean isObjective = false;

    private Player owner;
    private boolean beenAttacked = false;
    private int numberOfMoves = 0;

    /**
     * Constructor
     *
     * @param d - takes a String. Look at getPieceFromChar(char c)
     */
    public Piece(String d, String _path, JSONArray _ms) {
        super(d);
        define(d, _path, _ms);
    }

    public Piece(JSONObject obj) {
        super(obj.getString("name"));
        define(obj.getString("name"), obj.getString("imagePath"), obj.getJSONArray("moveStyles"));
    }

    public void moved() {
        ++numberOfMoves;
    }

    public void undoMove() {
        --numberOfMoves;
    }

    public boolean hasMoved() {
        return numberOfMoves > 0;
    }

    public MoveDestinations getMoveDestinations() {
        return moveDestinations;
    }

    public void setBeenAttacked(boolean b) {
        beenAttacked = b;
    }

    public void addMoveStyle(MoveStyle ms) {
        if (!moveStyles.contains(ms))
            moveStyles.add(ms);
    }

    public Vector<MoveStyle> getMoveStyles() {
        return moveStyles;
    }

    public void addMoveStyle(JSONObject jso) {
        addMoveStyle(new MoveStyle(jso));
    }

    private void define(String d, String _path, JSONArray _ms) {
        pieceName = d;
        currentRow = 0;
        currentColumn = 0;
        moveStyles = new Vector<MoveStyle>();
        imagePath = _path;
        for (int i = 0; i < _ms.length(); ++i) {
            this.addMoveStyle(_ms.getJSONObject(i));
        }
        moveDestinations = new MoveDestinations(pieceName);

        try {
            img = ImageIO.read(getClass().getResource("../"+_path));
        } catch (IOException e) {
            logLine("IOException...", 0);
        } catch (Exception npe) {
            logLine("Something went wrong with " + getPieceName()+" trying to read from "+_path, 0);
        }
    }

    public JSONObject getPieceAsJSON() {
        JSONObject toRet = new JSONObject();

        toRet.put("name", pieceName);
        toRet.put("imagePath", imagePath);
        JSONArray ms = new JSONArray();
        for (MoveStyle m : moveStyles) {
            ms.put(m.getAsJSONObject());
        }
        toRet.put("moveStyles", ms);

        return toRet;
    }

    public String getPieceName() {
        return pieceName;
    }

    private String getLocationAsString() {
        return "(" + currentColumn + ", " + currentRow + ")]: ";
    }

    /**
     * Renders the piece's <code>img</code> onto the given <code>Graphic</code>
     *
     * @param g
     */
    private void renderImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, IMG_X_OFFSET, IMG_Y_OFFSET, canvas);
    }

    /**
     * Overriding DrawableObject's render function so that when this
     * piece needs to be rendered this function is called.
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        if (img != null) {
            renderImage(g);
        }
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Set the owner of this piece to the provided Player
     *
     * @param _p - The owner of this piece
     */
    public void setOwner(Player _p) {
        owner = _p;
    }

    /**
     * Returns the Team the Piece is associated with
     *
     * @return - Team pointer to the team the owner of the piece is on.
     */
    public Team getTeam() {
        return owner.getTeam();
    }

    /**
     * Returned using top-left-zeroed method
     *
     * @return - the current row the piece is on
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * @param i - the row to set to
     */
    public void setCurrentRow(int i) {
        currentRow = i;
    }

    /**
     * @return - the current column the piece is in
     */
    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * @param i - the column to set to
     */
    public void setCurrentColumn(int i) {
        currentColumn = i;
    }

    public boolean canMoveTo(Square s) {
        return moveDestinations.canMoveTo(s);
    }

    public boolean canKillAt(Square s) {
        return moveDestinations.canKillAt(s);
    }

    /**
     * @param other
     * @return
     */
    public boolean canKill(Piece other) {
        if (other == null) return false;
        return other.getTeam() != this.getTeam();
    }

    public boolean isObjective() {
        return isObjective;
    }

    public void setObjective(boolean isObjective) {
        this.isObjective = isObjective;
    }
}
