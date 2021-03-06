package chess.game.objects;


import chess.automata.AIActor;
import chess.config.ConfigMaster;
import chess.general.Loggable;
import chess.gui.metroui.MetroButton;
import chess.gui.metroui.MetroPanel;
import chess.gui.objects.AbstractSlate;
import chess.gui.objects.DrawableBoard;
import chess.gui.objects.DrawableTimer;
import chess.master.Runner;
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
    private String[][] startingLayout;
    private Player[] playerOrder;
    private DrawableTimer[] timers;

    protected int turnCount = 0;
    protected JPanel leftPanel, rightPanel, headerPanel, footerPanel, centerPanel;
    protected JLabel inCheckLabel;
    protected History history;
    protected Board board;
    protected DrawableBoard drawBoard;
    private DrawableTimer gameTimer;

    public GameMode(
            String _modeName,
            String[][] _startingLayout,
            Player[] _playerOrder,
            int maxTime,
            Vector<String> objectives
    ) {
        super(_modeName);

        modeName = _modeName;
        startingLayout = _startingLayout;
        playerOrder = _playerOrder;
        timers = new DrawableTimer[playerOrder.length];
        for(int i = 0; i < timers.length; ++i ) {
            timers[i] = new DrawableTimer(playerOrder[i].getPlayerName(), maxTime);
            timers[i].start();
        }
        gameTimer = new DrawableTimer("Game Time");
        gameTimer.start();
        this.define(objectives);
    }

    private void define(Vector<String> objectives) {
        board = new Board(8, 8);
        drawBoard = new DrawableBoard(board, modeName);
        drawBoard.addKillListener(new KillActionCallBack());
        drawBoard.addMoveListener(new MoveActionCallback());
        for (int i = 0; i < startingLayout.length; ++i) {
            for (int j = 0; j < startingLayout[i].length; ++j) {
                Piece p = getPieceFromName(startingLayout[i][j], objectives);
                board.setPieceAt(p, j, i);
            }
        }
        history = new History();
    }

    public void startGame() {
        gameTimer.click();
        timers[0].click();
        ++turnCount;
        board.setCurrentPlayer(getCurrentPlayer());
        board.updateAllValidDestinations();
        drawBoard.tryRepaint();
        dealWithComputer();
    }



    /**
     * Gets the next active player while incrementing the turn count.
     * This ensures that when the next player is requested the act of
     * getting the next player and incrementing turn count is atomic.
     *
     * @return - <code>Player</code> pointer pointing to the next active
     * player.
     */
    public Player getCurrentPlayer() {
        //note: turnCount is not zero-indexed.
        return playerOrder[((turnCount - 1) % playerOrder.length)];
    }

    private void setupFooterPanel() {

    }

    private void setupRightPanel() {
        rightPanel.add(history.getList());
        final MetroButton mbtnUndo = new MetroButton("Undo");
        mbtnUndo.setRequiredDimension(new Dimension(AbstractSlate.sideWidth, 50));
        mbtnUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                undo();
            }
        });
        rightPanel.add(mbtnUndo.getCanvas());
//        rightPanel.add(btnUndo);
    }

    private void setupHeaderPanel() {
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 10));
        headerPanel.add(timers[0].getCanvas());
        MetroPanel inCheckPanel = new MetroPanel("Check Panel");
        inCheckLabel = new JLabel("");
        inCheckLabel.setFont(ConfigMaster.titleFont);
        inCheckLabel.setForeground(Color.white);
        inCheckPanel.setRequiredDimension(new Dimension(500, AbstractSlate.headFootHeight - 40));
        inCheckPanel.getCanvas().add(inCheckLabel);
        headerPanel.add(inCheckPanel.getCanvas());
        headerPanel.add(timers[1].getCanvas());

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
    protected Piece getPieceFromName(String s, Vector<String> objectives) {
        if (s.equals(" ")) return null;
        Player owner;
        JSONObject pieceDefinition = Runner.pieceCollection.getJSONObject(s);
        Piece p = null;
        if(pieceDefinition!= null)
           p = new Piece(pieceDefinition);
        if(p!=null) {
            if(objectives.contains(p.getPieceName()))
                p.setObjective(true);
            else
                p.setObjective(false);
        }
        //todo: change this when pieces are in GameMode...
        //idea: "white1_Rook" (team)(playerNo)_(PieceName)
        if (s.contains("white"))
            owner = playerOrder[0];
        else if (s.contains("black"))
            owner = playerOrder[1];
        else
            owner = null;

        if(owner != null)
            owner.addPiece(p);

        return p;
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
        Player p = getCurrentPlayer();
//        logLine("TC = "+turnCount+", player = "+p.getPlayerName(), 0);
        board.setCurrentPlayer(p);
        board.updateAllValidDestinations();
        timers[turnCount % timers.length].click();
        drawBoard.tryRepaint();
    }

    private void dealWithComputer() {
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player p = getCurrentPlayer();
        if(p.isComputer()) {
            Move m = ((AIActor)p).getMove();
            if(m == null || turnCount > 200) return;
//            logLine(m.toString(), 0);
            if(m.isKill()) {
                tryKill(m.getActor(), m.getTo());
            } else {
                tryMove(m.getActor(), m.getTo());
            }
        }
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
    private boolean tryMove(Piece actor, Square to) {
        if(actor == null) logLine("ACTOR WAS NULL!", 0);
        Square from = board.getSquareAt(actor.getCurrentColumn(), actor.getCurrentRow());
        actor.moved();
        to.setPiece(actor);
        from.setPiece(null);
        incrementTurnOrder();
        int effect = dealWithCheck();
        Event e = new Event(from, to,
                null, to.getPiece(),
                1, effect,
                turnCount - 1,
                DrawableTimer.getFormattedTime(gameTimer.getMinutes(), gameTimer.getSeconds())
        );
        history.push(e);
        dealWithComputer();
        return true;
    }

    private class MoveActionCallback implements ActionCallBack {

        @Override
        public boolean registerAction(Piece actor, Square to) {
            return tryMove(actor, to);
        }
    }

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
    private boolean tryKill(Piece actor, Square to) {
        if(actor == null) logLine("ACTOR WAS NULL!", 0);
        Square from = board.getSquareAt(actor.getCurrentColumn(), actor.getCurrentRow());
        Piece killedPiece = to.getPiece();
        killedPiece.getOwner().addKilledPiece(killedPiece);
        to.setPiece(null);
        actor.moved();
        to.setPiece(actor);
        from.setPiece(null);
        incrementTurnOrder();
        int effect = dealWithCheck();
        Event e = new Event(from, to,
                killedPiece, to.getPiece(),
                2, effect,
                turnCount - 1,
                DrawableTimer.getFormattedTime(gameTimer.getMinutes(), gameTimer.getSeconds())
        );
        history.push(e);
        dealWithComputer();
        return true;
    }

    private class KillActionCallBack implements ActionCallBack {

        @Override
        public boolean registerAction(Piece actor, Square to) {
            return tryKill(actor, to);
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
        if(victim != null) {
            victim.getOwner().resurrectPiece(victim);
        }
        // modify affected pieces/squares
        origin.setPiece(null);
        destination.setPiece(null);
        origin.setPiece(offender);
        offender.undoMove();
        destination.setPiece(victim);
        turnCount -= 1;
        board.setCurrentPlayer(getCurrentPlayer());
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
     * This is called AFTER turn order is incremented... so current player
     * will be in check by the player who just moved
     */
    private int dealWithCheck() {
        //attacked player's objectives
        Player attackedPlayer = getCurrentPlayer();
        Vector<Piece> pieces = attackedPlayer.getPieces();
        int totalNumMoves = 0;
        int effect;
        Player player = null;
        for(Piece p : pieces) {
            totalNumMoves += p.getMoveDestinations().getTotalNumMoves();
            if(p.isObjective()) {
                Piece attacker = board.checkForOffend(p);
                if(attacker != null) {
                    p.setBeenAttacked(true);
                    player = getCurrentPlayer();
                }
            }
        }

        if(totalNumMoves == 0) {
            if(player == null)
                effect = 2; //stalemate
            else
                effect = 3; //checkmate
        } else {
            if(player == null)
                effect = 0; //nothing
            else
                effect = 1; //check
        }

        inCheckLabel.setText(Event.getStringFromEffect(effect));
        if(effect == 2 || effect == 3)
            endGame();
        return effect;
    }

    public void setupPanels() {
        setupCenterPanel();
        setupLeftPanel();
        setupRightPanel();
        setupHeaderPanel();
        setupFooterPanel();
    }
}
