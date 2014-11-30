package chess.game.objects;

import chess.config.ConfigMaster;
import chess.general.Loggable;

import java.util.HashMap;
import java.util.Vector;

public class Board extends Loggable {

    private Pathfinder pathfinder;
    private int width, height;
    private Square[][] board;
    private Player currentPlayer;
    private boolean isUnderCheck = false;

    public Board(int w, int h) {
        super("Board");
        logLine("Constructing Board", 2);
        board = new Square[h][w]; // IN THIS CLASS: [row][column] = [y][x]
        width = w;
        height = h;
        pathfinder = new Pathfinder(this);
        initializeBoard();
        logLine("Done Constructing Board", 2);
    }

    /**
     * Sets the <code>Board</code> object up to contain the correct
     * number <code>Square</code>s at the right places.
     */
    private void initializeBoard() {
        boolean white = true;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (white) {
                    setSquareAt(j, i, new Square(j, i, ConfigMaster.whiteSquareColor));
                } else {
                    setSquareAt(j, i, new Square(j, i, ConfigMaster.blackSquareColor));
                }
                white = !white;
            }
            white = !white;
        }
    }

    public void setSquareAt(int col, int row, Square s) {
        if (col >= width || row >= height || col < 0 || row < 0)
            return;
        board[row][col] = s;
    }

    private void updateValidDestinations(Piece p) {
        MoveDestinations moveDestinations = p.getMoveDestinations();
        moveDestinations.clearAll();
        Vector<MoveStyle> moveStyles = p.getMoveStyles();
        for (MoveStyle ms : moveStyles) {
            pathfinder.generatePath(p, ms, moveDestinations);
        }
    }

    private void dealWithChecking(Piece attacker) {
        Vector<Piece> currentPlayerPieces = currentPlayer.getPieces();

        Vector<Square> pathToObjective = new Vector<Square>();

        MoveDestinations amd = attacker.getMoveDestinations();
        pathToObjective.addAll(amd.getPathToObjective());
        pathToObjective.add(getSquareAt(attacker.getCurrentColumn(), attacker.getCurrentRow()));
        for(Piece p : currentPlayerPieces) {
            MoveDestinations moveDestinations = p.getMoveDestinations();
            if(!p.isObjective()) {
                moveDestinations.intersectWith(pathToObjective);
            }
        }
    }

    private void dealWithObjective(Piece p) {
        Vector<Piece> objectives = currentPlayer.getObjectives();
        for(Piece objective : objectives) {
            objective.getMoveDestinations().subtractWith(p.getMoveDestinations().getAllAsOne());
        }
    }

    public void dealWithPinning(Piece pinner) {
        HashMap<Piece, Vector<Square>> pieceVectorHashMap = pinner.getMoveDestinations().getPinnedPieces();
        Vector<Piece> pinnedPieces = new Vector<Piece>(pieceVectorHashMap.keySet());
        for(Piece p : pinnedPieces) {
            Vector<Square> pinnerLocations = pieceVectorHashMap.get(p);
            pinnerLocations.add(getSquareAt(pinner.getCurrentColumn(), pinner.getCurrentRow()));
            p.getMoveDestinations().intersectWith(pinnerLocations);
        }
    }

    public void updateAllValidDestinations() {
        Vector<Piece> objectiveAttackers = new Vector<Piece>();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Square s = getSquareAt(j, i);
                Piece p = s.getPiece();
                if (p != null) {
                    updateValidDestinations(p);
                    if(p.getMoveDestinations().containsObjective())
                        objectiveAttackers.add(p);
                }
            }
        }

        for(Piece p : objectiveAttackers) {
            dealWithChecking(p);
            isUnderCheck = true;
        }

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Square s = getSquareAt(j, i);
                Piece p = s.getPiece();
                if (p != null && p.getOwner() != currentPlayer) {
                    dealWithPinning(p);
                    dealWithObjective(p);
                }
            }
        }


    }

    /**
     * Checks if there is any piece that is able to kill this piece
     * and returns the first offending piece found.
     *
     * @param target - The Piece that might be a victim of another piece
     * @return - returns a Piece that has the provided Piece in its
     * valid kill destinations. If none is found then null.
     */
    public Piece checkForOffend(Piece target) {
        Square victimLoc = getSquareAt(target.getCurrentColumn(), target.getCurrentRow());
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Square s = getSquareAt(j, i);
                Piece p = s.getPiece();
                if (p!= null && p.canKill(target) && p.canKillAt(victimLoc))
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
     * Returns the square at the provided coordinates based on
     * internal 2D array storage methods. (0, 0) is in the top left
     * corner, and (max, max) is in the bottom right hand corner
     *
     * @param col - the column the square to get is in
     * @param row - the row the square to get is in
     * @return - the square that is at the requested coordinates. Null if not valid
     */
    public Square getSquareAt(int col, int row) {
        if (col >= width || col < 0 || row >= height || row < 0) return null;
        return board[row][col];
    }

    /**
     * Sets the square at the location specified to contain the piece passed.
     * Calls getSquareAt - not chessGetSquareAt!
     *
     * @param p   - the piece to place at the target square
     * @param col - the target square's x coordinate
     * @param row - the target squres's y coordinate
     */
    public void setPieceAt(Piece p, int col, int row) {
        getSquareAt(col, row).setPiece(p);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

}
