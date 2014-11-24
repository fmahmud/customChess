package chess.baseObjects;


import chess.custom.Faction;
import chess.general.Common;
import chess.general.Loggable;
import chess.gui.objects.DrawableBoard;
import chess.master.ConfigMaster;
import chess.master.Runner;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that keeps track of the various teams, the board and the turn order.
 *
 */
public class GameMode extends Loggable {
    protected Team[] teams;
    protected int numTeams;
    protected int turnCount = 0;
    protected Player[] playerOrder;
    protected JPanel leftPanel, rightPanel, headerPanel, footerPanel, centerPanel;
    protected String modeName;
    protected History history;
    protected Board board;
    protected DrawableBoard drawBoard;
    protected JButton btnUndo;
    private Timer gameTimer;


    String[][] boardLayout;

    /**
     * Gets the next active player while incrementing the turn count.
     * This ensures that when the next player is requested the act of
     * getting the next player and incrementing turn count is atomic.
     * @return - <code>Player</code> pointer pointing to the next active
     *           player.
     */
    public Player getNextActivePlayer()  {
        return playerOrder[((turnCount++) % playerOrder.length)];
    }

    private int getCurrentTeamIndex() {
        return (turnCount-1) % numTeams;
    }

    private void addPieceToKilledList(Piece piece, Player player) {

    }

    /**
     * Gets the current <code>Player</code> based on the turn count.
     * @return - <code>Player</code> who's turn it currently is.
     */
    public Player getCurrentActivePlayer()  {
        return playerOrder[ (turnCount % playerOrder.length) ];
    }

    public int getTurnCount() {
        return turnCount;
    }

    public GameMode(String _name, int _numTeams, int[] numPlayersInTeams, Faction[][] factions,
                    JPanel[] panels) {
        super(_name);
        headerPanel = panels[0];
        centerPanel = panels[1];
        rightPanel = panels[2];
        leftPanel = panels[3];
        footerPanel = panels[4];
        modeName = _name;
        this.numTeams = _numTeams;
        teams = new Team[numTeams];
        int totalNumPlayers = 0;
        for (int i = 0; i < numTeams; i++) {
            //todo: fix team name - push to user input
            teams[i] = new Team(numPlayersInTeams[i], factions[i], "Team"+(i+1));
            totalNumPlayers += numPlayersInTeams[i];
        }
        playerOrder = new Player[totalNumPlayers];
        for(int i = 0; i < totalNumPlayers; ++i) {
            playerOrder[i] = teams[i % numTeams].getPlayer(i / numTeams);
        }
        playerOrder[0] = teams[0].getPlayer(0);
        playerOrder[1] = teams[1].getPlayer(0);

        //why is this here? push up to GameMode?
        board = new Board(8, 8, this);
        drawBoard = new DrawableBoard(board, modeName);
        initializeBoard();
        setBoardLayout(new JSONObject());
        for(int i = 0; i < boardLayout.length; ++i) {
            for(int j = 0; j < boardLayout[i].length; ++j) {
                setPieceAt(boardLayout[i][j], j, i);
            }
        }

        board.updateAllValidDestinations();
        board.setCurrentPlayer(playerOrder[0]);
        history = new History();

        setupCenterPanel();
        setupLeftPanel();
        setupRightPanel();
        setupHeaderPanel();
        setupFooterPanel();
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
        JLabel lblGameTime = new JLabel("00:00");
        lblGameTime.setFont(ConfigMaster.headerFont);
        gameTimer = new Timer("Game Time", lblGameTime);
        headerPanel.add(lblGameTime);
        gameTimer.start();
        logLine("timer initiated?", 0);
    }

    private void setupLeftPanel() {

    }

    private void setupCenterPanel() {
        logLine("centerPanel == null = "+(centerPanel==null)+", drawBoard == null = "+(drawBoard==null), 0);
        centerPanel.add(drawBoard.getCanvas());
    }

    /**
     * Sets the piece at (<code>col</code>, <code>row</code>) to the
     * correct <code>Piece</code> depending on the value of <code>c</code>.
     * Calls <code>getPieceFromChar(...)</code> to determine the piece.
     *
     * @param col - the current column the piece is in.
     * @param row - the current row the piece is in.
     */
    protected void setPieceAt(String s, int col, int row) {
        if(s.equals(" ")) return;
        Player owner;
        Piece p = new Piece(Runner.pieceLibrary.getPieceJSON(s));

        if(s.contains("white"))
            owner = playerOrder[0];
        else if(s.contains("black"))
            owner = playerOrder[1];
        else
            owner = null;

        if(p == null) {
            if(!s.equals(" "))
                logLine("Piece definition for " + s + " was null...", 0);
        } else {
            p.setOwner(owner);
            board.setPieceAt(p, col, row);
        }
    }

    private void setBoardLayout(JSONObject layout) {
        boardLayout = new String[][] {
                {"whiteRook", "whiteKnight", "whiteBishop", "whiteKing", "whiteQueen", "whiteBishop", "whiteKnight", "whiteRook"},
                {"whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn"},
                {"blackRook", "blackKnight", "blackBishop", "blackKing", "blackQueen", "blackBishop", "blackKnight", "blackRook"}
        };
    }

    public GameMode(JSONObject o) {
        super(o.getString("title"));
    }

    /**
     * When a move is completed the turn order is incremented.
     * Also, the possibility of a player being under check is
     * tested and dealt with.
     *
     * why is this so simple?
     * idea modify/update the panels.
     */
    private void incrememntTurnOrder() {
        board.setCurrentPlayer(getNextActivePlayer());
//        dealWithCheck();
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
     *
     * why is this boolean? it always returns true.
     * why is this not so clean?
     * @param from - <code>Square</code> from which the <code>Piece</code> is
     *             moving
     * @param to - <code>Square</code> to which the <code>Piece</code> is
     *           moving
     * @return - true.
     */
    public boolean tryMoveFromTo(Square from, Square to) {
        Piece p = from.getPiece();
        p.moved();
        to.setPiece(p);
        from.setPiece(null);
        board.updateAllValidDestinations();
        Event e = new Event(from, to,
                null, to.getPiece(),
                1, 0,//todo: temp (check for check, mate and stale)
                turnCount,
                0l,
                0l
        );
        history.push(e);
        btnUndo.setEnabled(true);
        incrememntTurnOrder();
        return true;
    }

    /**
     * After user input has provided a starting <code>Square</code>
     * and a destination <code>Square</code> from and to which a their
     * <code>Piece</code> is moving, this function is called if and only
     * if there is an enemy piece in the destination.
     *
     * why is this boolean? it always returns true.
     * why is this not so clean?
     * @param from - <code>Square</code> from which the <code>Piece</code> is
     *             moving
     * @param to - <code>Square</code> to which the <code>Piece</code> is
     *           moving
     * @return - true.
     */
    public boolean tryKillAt(Square from, Square to) {
        Piece killedPiece = to.getPiece();
        killedPiece.getOwner().addKilledPiece(killedPiece);
        to.setPiece(null);
        Piece p = from.getPiece();
        p.moved();
        to.setPiece(p);
        from.setPiece(null);
        board.updateAllValidDestinations();
        Event e = new Event(from, to,
                killedPiece, to.getPiece(),
                2, 0, //todo: temp (check for check and mate or stale)
                turnCount,
                0l,
                0l
        );
        history.push(e);
        btnUndo.setEnabled(true);
        incrememntTurnOrder();
        return true;
    }

    /**
     * Nothing currently. Plugged in.
     *
     */
    public void undo() {
        Event e = history.pop();
        if(e == null) return;
        Square origin = e.getOrigin();
        Square destination = e.getDestination();
        Piece victim = e.getVictim();
        Piece offender = e.getOffender();

        origin.setPiece(null);
        destination.setPiece(null);
        origin.setPiece(offender);
        offender.undoMove();
        destination.setPiece(victim);
        turnCount -= 2;
        board.setCurrentPlayer(getNextActivePlayer());
        board.updateAllValidDestinations();
        if(victim != null) {
            //why is this like this?
//            killedWhitePieces.remove(victim);
//            killedBlackPieces.remove(victim);
        }
        dealWithCheck();
        drawBoard.tryRepaint();
    }

    public void onEndGame() {
        //do something later?
        gameTimer.stop();
    }


    /**
     * At the end of every move this function is called to deal with the
     * possibility of a player being under check.
     *
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


    /**
     * Sets the <code>Board</code> object up to contain the correct
     * number <code>Square</code>s at the right places.
     */
    private void initializeBoard() {
        boolean white = true;
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                if(white) {
                    board.setSquareAt(j, i, new Square(j, i, ConfigMaster.whiteSquare));
                } else {
                    board.setSquareAt(j, i, new Square(j, i, ConfigMaster.blackSquare));
                }
                white = !white;
            }
            white = !white;
        }
    }
}
