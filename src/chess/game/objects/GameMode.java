package chess.game.objects;


import chess.general.Common;
import chess.general.Loggable;
import chess.gui.objects.DrawableBoard;
import chess.gui.objects.DrawableTimer;
import chess.master.Runner;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Class that keeps track of the various teams, the board and the turn order.
 */
public class GameMode extends Loggable {
    private String modeName;
    private Team[] teams;
    private String[][] startingLayout;
    private Player[] playerOrder;
    private DrawableTimer[] timers;

    protected int turnCount = 0;
    protected JPanel leftPanel, rightPanel, headerPanel, footerPanel, centerPanel;
    protected History history;
    protected Board board;
    protected DrawableBoard drawBoard;
    protected JButton btnUndo;
    private DrawableTimer gameTimer;

    public GameMode(JSONObject o) {
        super(o.getString("name"));

        // name of game type
        modeName = o.getString("name");

        // layout
        setStartingLayout(o.getJSONObject("layout"));

        // team structure
        JSONArray jsonTeams = o.getJSONArray("teams");
        teams = new Team[jsonTeams.length()];
        for(int i = 0; i < jsonTeams.length(); ++i) {
            teams[i] = new Team(jsonTeams.getJSONObject(i));
        }

        //////////////////////////////////////////////////////////
        // Turn order will be stored as tuples:
        // "0,0","1,0" means team 0's player 0 will go first
        // then team 1's player 0 will go next.
        // Stored as array of Strings.
        //////////////////////////////////////////////////////////
        JSONArray tuples = o.getJSONArray("turnOrder");
        playerOrder = new Player[tuples.length()];
        for(int i = 0; i < tuples.length(); ++i) {
            String[] tuple = tuples.getString(i).split(",");
            playerOrder[i] = teams[Integer.parseInt(tuple[0])].getPlayer(Integer.parseInt(tuple[1]));
        }

        int maxTime;
        // timers and max time
        maxTime = o.getInt("maxTime"); //max time per player
        timers = new DrawableTimer[playerOrder.length];
        for(int i = 0; i < timers.length; ++i ) {
            timers[i] = new DrawableTimer(playerOrder[i].getPlayerName(), maxTime);
            timers[i].start();
        }
        gameTimer = new DrawableTimer("Game Time");
        gameTimer.start();
        JSONArray objs = o.getJSONArray("objectives");
        Vector<String> objectives = new Vector<String>();
        for(int i = 0; i < objs.length(); ++i) {
            objectives.add(objs.getString(i));
            logLine("Objective["+i+"} = "+objs.getString(i), 0);
        }
        this.define(objectives);
        this.startGame();
    }

    private void define(Vector<String> objectives) {
        board = new Board(8, 8);
        drawBoard = new DrawableBoard(board, modeName);
        drawBoard.addKillListener(new KillActionCallBack());
        drawBoard.addMoveListener(new MoveActionCallback());
        for (int i = 0; i < startingLayout.length; ++i) {
            for (int j = 0; j < startingLayout[i].length; ++j) {
                Piece p = getPieceFromName(startingLayout[i][j]);
                board.setPieceAt(p, j, i);
                if(p == null) continue;
                if(objectives.contains(p.getPieceName()))
                    p.setObjective(true);
                else
                    p.setObjective(false);
            }
        }
        history = new History();
    }

    private void startGame() {
        gameTimer.click();
        timers[0].click();
        ++turnCount;
        board.setCurrentPlayer(getNextActivePlayer());
        board.updateAllValidDestinations();
    }


    /**
     * Gets the next active player while incrementing the turn count.
     * This ensures that when the next player is requested the act of
     * getting the next player and incrementing turn count is atomic.
     *
     * @return - <code>Player</code> pointer pointing to the next active
     * player.
     */
    public Player getNextActivePlayer() {
        //note: turnCount is not zero-indexed.
        return playerOrder[((turnCount - 1) % playerOrder.length)];
    }

    private void setupFooterPanel() {

    }

    private void setupRightPanel() {
        rightPanel.add(history.getList());
        btnUndo = Common.buttonFactory("Undo", "undo", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                undo();
                if (history.getDepth() == 0)
                    btnUndo.setEnabled(false);
            }
        });
        btnUndo.setEnabled(false);
        rightPanel.add(btnUndo);
    }

    private void setupHeaderPanel() {
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 10));
        for(DrawableTimer timer : timers) {
            headerPanel.add(timer.getCanvas());
        }
    }

    private void setupLeftPanel() {

    }

    private void setupCenterPanel() {
        centerPanel.add(drawBoard.getCanvas());
    }


    /**
     *
     * @param s
     */
    protected Piece getPieceFromName(String s) {
        if (s.equals(" ")) return null;
        Player owner;
        Piece p = new Piece(Runner.pieceCollection.getJSONObject(s));

        //todo: change this when pieces are in GameMode...
        //idea: "white1_Rook" (team)(playerNo)_(PieceName)
        if (s.contains("white"))
            owner = playerOrder[0];
        else if (s.contains("black"))
            owner = playerOrder[1];
        else
            owner = null;

       p.setOwner(owner);
       return p;
    }

    private void setStartingLayout(JSONObject layout) {
        startingLayout = new String[8][8];
        JSONArray rows = layout.getJSONArray("rows");
        for(int i = 0; i < rows.length(); ++i) {
            JSONArray row = rows.getJSONArray(i);
            for(int j = 0; j < row.length(); ++j) {
                startingLayout[i][j] = row.getString(j);
            }
        }
    }

    /**
     * When a move is completed the turn order is incremented.
     * Also, the possibility of a player being under check is
     * tested and dealt with.
     * <p/>
     */
    private void incrementTurnOrder() {
        timers[turnCount % timers.length].click();
        ++turnCount;
        board.setCurrentPlayer(getNextActivePlayer());
        board.updateAllValidDestinations();
        timers[turnCount % timers.length].click();
        dealWithCheck();
    }

    public void setLeftPanel(JPanel pnl) {
        leftPanel = pnl;
    }

    public void setRightPanel(JPanel pnl) {
        rightPanel = pnl;
    }

    public void setHeaderPanel(JPanel pnl) {
        headerPanel = pnl;
    }

    public void setFooterPanel(JPanel pnl) {
        footerPanel = pnl;
    }

    public void setCenterPanel(JPanel pnl) {
        centerPanel = pnl;
    }

    private class MoveActionCallback implements ActionCallBack {

        /**
         * After user input has provided a starting <code>Square</code>
         * and a destination <code>Square</code> from and to which a their
         * <code>Piece</code> is moving, this function is called.
         * <p/>
         * why is this boolean? it always returns true.
         * why is this not so clean?
         *
         * @param from - <code>Square</code> from which the <code>Piece</code> is
         *             moving
         * @param to   - <code>Square</code> to which the <code>Piece</code> is
         *             moving
         * @return - true.
         */
        @Override
        public boolean registerAction(Square from, Square to) {
            Piece p = from.getPiece();
            p.moved();
            to.setPiece(p);
            from.setPiece(null);
            Event e = new Event(from, to,
                    null, to.getPiece(),
                    1, 0,//todo: temp (check for check, mate and stale)
                    turnCount,
                    DrawableTimer.getFormattedTime(gameTimer.getMinutes(), gameTimer.getSeconds())
            );
            history.push(e);
            btnUndo.setEnabled(true);
            incrementTurnOrder();
            return true;
        }
    }

    private class KillActionCallBack implements ActionCallBack {

        /**
         * After user input has provided a starting <code>Square</code>
         * and a destination <code>Square</code> from and to which a their
         * <code>Piece</code> is moving, this function is called if and only
         * if there is an enemy piece in the destination.
         * <p/>
         * why is this boolean? it always returns true.
         * why is this not so clean?
         *
         * @param from - <code>Square</code> from which the <code>Piece</code> is
         *             moving
         * @param to   - <code>Square</code> to which the <code>Piece</code> is
         *             moving
         * @return - true.
         */
        @Override
        public boolean registerAction(Square from, Square to) {
            Piece killedPiece = to.getPiece();
            killedPiece.getOwner().addKilledPiece(killedPiece);
            to.setPiece(null);
            Piece p = from.getPiece();
            p.moved();
            to.setPiece(p);
            from.setPiece(null);
            Event e = new Event(from, to,
                    killedPiece, to.getPiece(),
                    2, 0, //todo: temp (check for check and mate or stale)
                    turnCount,
                    DrawableTimer.getFormattedTime(gameTimer.getMinutes(), gameTimer.getSeconds())
            );
            history.push(e);
            btnUndo.setEnabled(true);
            incrementTurnOrder();
            return true;
        }
    }

    public void undo() {
        Event e = history.pop();
        if (e == null) return;
        // valid event popped. should never be null.
        // grab required pointers
        Square origin = e.getOrigin();
        Square destination = e.getDestination();
        Piece victim = e.getVictim();
        Piece offender = e.getOffender();

        // modify affected pieces/squares
        origin.setPiece(null);
        destination.setPiece(null);
        origin.setPiece(offender);
        offender.undoMove();
        destination.setPiece(victim);
        turnCount -= 1;
        board.setCurrentPlayer(getNextActivePlayer());
        board.updateAllValidDestinations();
        dealWithCheck();
        drawBoard.tryRepaint();
    }

    public void endGame() {
        //do something later?
        gameTimer.stop();
        for(DrawableTimer t : timers) {
            t.stop();
        }
    }

    /**
     * At the end of every move this function is called to deal with the
     * possibility of a player being under check.
     * <p/>
     * todo: figure this out.
     */
    private void dealWithCheck() {
//        if(board.checkForOffend(whiteKing) != null) {
//            whiteKing.setBeenAttacked(true);
//            inCheck = whitePlayer;
//        } else if(board.checkForOffend(blackKing) != null) {
//            blackKing.setBeenAttacked(true);
//            inCheck = blackPlayer;
//        } else {
//            inCheck = null;
//        }
    }

    public void setupPanels() {
        setupCenterPanel();
        setupLeftPanel();
        setupRightPanel();
        setupHeaderPanel();
        setupFooterPanel();
    }
}
