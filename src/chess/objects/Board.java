package chess.objects;

import chess.general.Loggable;

import java.util.Vector;

public class Board extends Loggable {

    private int width, height;
    private Square[][] board;
    private Player currentPlayer;
    private GameMode gameMode;
    public Pathfinder pathfinder;

    public Board(int w, int h, GameMode _gm) {
        super("Board");
        logLine("Constructing Board", 2);
        board = new Square[h][w]; // IN THIS CLASS: [row][column] = [y][x]
        width = w;
        height = h;
        gameMode = _gm;
        pathfinder = new Pathfinder(this);
        logLine("Done Constructing Board", 2);
    }

    public void setSquareAt(int col, int row, Square s) {
        if(col >= width || row >= height || col < 0 || row < 0)
            return;
        board[row][col] = s;
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    private void updateValidDestinations(Piece p) {
        MoveDestinations moveDestinations = p.getMoveDestinations();
        moveDestinations.clearAll();
        Vector<MoveStyle> moveStyles = p.getMoveStyles();
        for(MoveStyle ms : moveStyles) {
            pathfinder.generatePath(p, ms, moveDestinations);
        }
    }

    public void updateAllValidDestinations() {
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Square s = getSquareAt(j, i);
                Piece p = s.getPiece();
                if(p != null)
                    updateValidDestinations(p);
            }
        }
    }

    /**
     * Checks if there is any piece that is able to kill this piece
     * and returns the first offending piece found.
     * @param target - The Piece that might be a victim of another piece
     * @return - returns a Piece that has the provided Piece in its
     *           valid kill destinations. If none is found then null.
     */
    public Piece checkForOffend(Piece target) {
        Square victimLoc = getSquareAt(target.getCurrentColumn(), target.getCurrentRow());
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                Square s = getSquareAt(j, i);
                Piece p = s.getPiece();
                if(p == null) continue;
                if(p.canGoTo(victimLoc))
                    return p;
            }
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Returns the square at the coordinates passed. Format = (x, y) = (col,
     * row). Does check for boundary conditions.
     * Rotates the axis so that (0, 0) means the bottom left instead of top right
     * This is the Chess style of numbering
     *
     * @param col the x coordinate of the required square
     * @param row the y coordinate of the required square
     * @return the square at (col, row) if valid. Otherwise null.
     */
    public Square chessGetSquareAt(int col, int row) {
        return getSquareAt(col, row);
    }

    /**
     * Returns the square at the provided coordinates based on
     * internal 2D array storage methods. (0, 0) is in the top left
     * corner, and (max, max) is in the bottom right hand corner
     * @param col - the column the square to get is in
     * @param row - the row the square to get is in
     * @return - the square that is at the requested coordinates. Null if not valid
     */
    public Square getSquareAt(int col, int row) {
        if(col >= width || col < 0 || row >= height || row < 0) return null;
        return board[row][col];
    }

    /**
     * Sets the square at the location specified to contain the piece passed.
     * Calls getSquareAt - not chessGetSquareAt!
     * @param p - the piece to place at the target square
     * @param col - the target square's x coordinate
     * @param row - the target squres's y coordinate
     */
    public void setPieceAt(Piece p, int col, int row) {
        getSquareAt(col, row).setPiece(p);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void tryMoveFromTo(Square from, Square to) {
        gameMode.tryMoveFromTo(from, to);
    }

    public void tryKillAt(Square from, Square to) {
        gameMode.tryKillAt(from, to);
    }
}
