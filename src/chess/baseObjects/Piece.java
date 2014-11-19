package chess.baseObjects;

import chess.custom.Faction;
import chess.general.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class Piece extends DrawableObject {
    private Vector<MoveStyle> moveStyles;

    protected Image img;

    protected Vector[] validDestinations;
    private int currentRow, currentColumn;
    private String pieceName;
    private String imagePath;

    private Player owner;
    private boolean beenAttacked = false;
    private int numberOfMoves = 0;

    public void moved() {
        ++numberOfMoves;
    }

    public void undoMove() {
        --numberOfMoves;
    }

    public boolean hasMoved() {
        return numberOfMoves > 0;
    }

    /**
     * Constructor
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

    public void setBeenAttacked(boolean b) {
        beenAttacked = b;
    }

    public void addMoveStyle(MoveStyle ms) {
        if(!moveStyles.contains(ms))
            moveStyles.add(ms);
    }

    public void addMoveStyle(JSONObject jso) {
        addMoveStyle(new MoveStyle(jso));
    }

    private void define(String d, String _path, JSONArray _ms) {
        pieceName = d;
        currentRow = 0;
        currentColumn = 0;
        validDestinations = new Vector[2];
        validDestinations[0] = new Vector<Square>();
        validDestinations[1] = new Vector<Square>();
        moveStyles = new Vector<MoveStyle>();
        imagePath = _path;
        for(int i = 0; i < _ms.length(); ++i) {
            this.addMoveStyle(_ms.getJSONObject(i));
        }

        try {
            img = ImageIO.read(getClass().getResource(_path));
        } catch (IOException e) {
            logLine("IOException...", 0);
        } catch (Exception npe) {
            logLine("Something went wrong with" + getPieceName(), 0);
        }
    }

    public JSONObject getPieceAsJSON() {
        JSONObject toRet = new JSONObject();

        toRet.put("name", pieceName);
        toRet.put("imagePath", imagePath);
        JSONArray ms = new JSONArray();
        for(MoveStyle m : moveStyles) {
            ms.put(m.getAsJSONObject());
        }
        toRet.put("moveStyles", ms);

        return toRet;
    }

    /**
     * No validation - assumed right.
     * @param i - the row to set to
     */
    public void setCurrentRow(int i) {
        currentRow = i;
    }

    /**
     * No validation - assumed right.
     * @param i - the column to set to
     */
    public void setCurrentColumn(int i) {
        currentColumn = i;
    }

    public String getPieceName() {
        return pieceName;
    }

    private String getLocationAsString() {
        return "("+currentColumn+", "+currentRow+")]: ";
    }

    private void renderImage(Graphics g) {
        int yOff = 10;
        int xOff = 10;
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, xOff, yOff, canvas);
    }

    @Override
    public void render(Graphics g) {
        if(img != null) {
            renderImage(g);
        }
    }

    /**
     * Set the owner of this piece to the provided Player
     * @param _p - The owner of this piece
     */
    public void setOwner(Player _p) {
        owner = _p;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     *
     * @return - The Faction the owner of this piece is.
     */
    public Faction getFaction() {
        if(owner == null) {
            logLine("owner was null", 4);
            return null;
        }
        return owner.getFaction();
    }

    /**
     * Returns the Team the Piece is associated with
     * @return - Team pointer to the team the owner of the piece is on.
     */
    public Team getTeam() {
        return owner.getTeam();
    }

   /**
     * Updates the list of Squares this piece can reach.
     * @param board
     */
    public void updateValidDestinations(Board board) {
        this.updateValidDestinations(moveStyles, board);
    }



    public void updateValidDestinations(Vector<MoveStyle> moveStyles, Board board) {
        this.validDestinations[0].clear();
        this.validDestinations[1].clear();
        for(MoveStyle ms : moveStyles) {
            Vector[] vs = ms.getPossibleMoveDestinations(board, getCurrentColumn(), getCurrentRow());
            this.validDestinations[0].addAll(vs[0]);
            this.validDestinations[1].addAll(vs[1]);
        }

        // Removing duplicates because something else is
        // going wrong somewhere else and I don't know where.
        Common.removeDuplicates(validDestinations[0]);
        Common.removeDuplicates(validDestinations[1]);

        String _s = "";
        for(Square s : (Vector<Square>)validDestinations[0]) {
            _s += "("+s.getColumn()+", "+s.getRow()+") ";
        }

        logLine(_s, 4);
    }

    /**
     * Returned using top-left-zeroed method
     * @return - the current row the piece is on
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     *
     * @return - the current column the piece is in
     */
    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean canKill(Piece other) {
        if(other == null) return true;
        return other.getTeam() != this.getTeam();
    }

    public Vector<Square> getValidMoveDestinations() {
        logLine("Size of validMoveDestinations = "+validDestinations[0].size(), 4);
        return validDestinations[0];
    }

    public Vector<Square> getValidKillDestinations() {
        logLine("Size of validKillDestinations = "+validDestinations[1].size(), 4);
        return validDestinations[1];
    }
}
