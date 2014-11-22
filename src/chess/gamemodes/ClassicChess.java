package chess.gamemodes;

import chess.baseObjects.*;
import chess.baseObjects.Event;
import chess.baseObjects.Timer;
import chess.custom.Faction;
import chess.general.Common;
import chess.gui.objects.DrawableBoard;
import chess.master.ConfigHandler;
import chess.master.Runner;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/**
 * Particular type of <code>GameMode</code>. This class sets the game up
 * to play a standard 1v1 game of chess.
 */
public class ClassicChess extends GameMode {
    protected History history;
    private Player inCheck = null;
    private Player currentPlayer = null;
    private Player whitePlayer, blackPlayer;
    private long whiteTime, blackTime;
    private Vector<Piece> killedWhitePieces, killedBlackPieces;
    private DrawableBoard drawBoard;
    private Board board;
    private File directory;
    private JButton btnUndo;

    public static Color COLOR_BLACK_PIECE     = new Color(120,  90,  30, 255);
    public static Color COLOR_WHITE_PIECE     = new Color(252, 224, 188, 255);


    /**
     * The initial layout of the board. Capital letters represent a white piece, while
     * black pieces are represented by the lower case letters.
     * Note: Each piece is represented by the letter in square brackets:
     *      [P]awn, [R]ook, [Q]ueen, [K]ing, K[n]ight, [B]ishop
     */
    String[][] boardLayout = new String[][] {
            {"whiteRook", "whiteKnight", "whiteBishop", "whiteKing", "whiteQueen", "whiteBishop", "whiteKnight", "whiteRook"},
            {"whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn", "whitePawn"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn", "blackPawn"},
            {"blackRook", "blackKnight", "blackBishop", "blackKing", "blackQueen", "blackBishop", "blackKnight", "blackRook"}
    };

    /**
     * Constructor for the <code>ClassicChess</code> <code>GameMode</code>.
     * Instantiates the two factions: black and white, the <code>Pathfinder</code>,
     * the <code>Board</code> object, and passes that to the instance of <code>DrawableBoard</code>.
     */
    public ClassicChess() {
        super("Classic Chess", 2, new int[] {1, 1},
                new Faction[][] {
                    new Faction[] { Faction.WHITE },
                    new Faction[] { Faction.BLACK }
                }
        );
        whitePlayer = teams[0].getPlayerByNumber(0);
        blackPlayer = teams[1].getPlayerByNumber(0);
        playerOrder[0] = whitePlayer;
        playerOrder[1] = blackPlayer;
        whiteTime = blackTime = 0l;

        //why is this here? push up to GameMode?
        board = new Board(8, 8, this);
        drawBoard = new DrawableBoard(board, modeName);
        initializeBoard();
        for(int i = 0; i < boardLayout.length; ++i) {
            for(int j = 0; j < boardLayout[i].length; ++j) {
                setPieceAt(boardLayout[i][j], j, i);
            }
        }

        board.updateAllValidDestinations();
        board.setCurrentPlayer(playerOrder[0]);
        killedBlackPieces = new Vector<Piece>();
        killedWhitePieces = new Vector<Piece>();
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
        btnUndo = makeButton("Undo", "undo", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                undo();
                if(history.getDepth() == 0)
                    btnUndo.setEnabled(false);
            }
        });
        btnUndo.setEnabled(false);
        rightPanel.add(btnUndo);
    }

    private JButton makeButton(String name, String action, ActionListener al) {
        JButton toRet = new JButton(name);
        toRet.setActionCommand(action);
        toRet.addActionListener(al);
        return toRet;
    }

    private void setupHeaderPanel() {
        JLabel lblGameTime = new JLabel("00:00");
        lblGameTime.setFont(ConfigHandler.headerFont);
        Timer t = new Timer("Game Time", lblGameTime);
        headerPanel.add(lblGameTime);
        t.start();
        logLine("timer initiated?", 0);
    }

    private void setupLeftPanel() {

    }

    private void setupCenterPanel() {
        centerPanel.add(drawBoard.getCanvas());
    }

    /**
     * Gets the next active player while incrementing the turn count.
     * This ensures that when the next player is requested the act of
     * getting the next player and incrementing turn count is atomic.
     * @return - <code>Player</code> pointer pointing to the next active
     *           player.
     */
    @Override
    public Player getNextActivePlayer()  {
        return playerOrder[((turnCount++) % playerOrder.length)];
    }

    /**
     * Gets the current <code>Player</code> based on the turn count.
     * @return - <code>Player</code> who's turn it currently is.
     */
    @Override
    public Player getCurrentActivePlayer()  {
        return playerOrder[ (turnCount % playerOrder.length) ];
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
                    board.setSquareAt(j, i, new Square(j, i, ConfigHandler.whiteSquare));
                } else {
                    board.setSquareAt(j, i, new Square(j, i, ConfigHandler.blackSquare));
                }
                white = !white;
            }
            white = !white;
        }
    }

    private JSONObject getPieceDefFromName(String pName) {
        return Runner.pieceLibrary.getPieceJSON(pName);
    }

    /**
     * Basic switch statement to return the correct <code>Piece</code>
     * that the letter passed corresponds to. This piece requires a few
     * parameters: <code>col</code>, <code>row</code> that need to be
     * to be passed into this function. Also, at this point if the piece
     * is a king, a pointer to that object is saved for future reference.
     * @param pName - <code>char</code> representing the piece.
     * @return - A specific <code>Piece</code> object, depending on which
     *              type the piece is.
     */
    private Piece getPieceFromName(String pName) {
        JSONObject def = getPieceDefFromName(pName);
        if(def != null)
            return new Piece(def);
        else
            return null;
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

        Player owner;
        Piece p = getPieceFromName(s);

        if(s.contains("white"))
            owner = whitePlayer;
        else if(s.contains("black"))
            owner = blackPlayer;
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
     * When a move is completed the turn order is incremented.
     * Also, the possibility of a player being under check is
     * tested and dealt with.
     *
     * why is this so simple?
     * idea modify/update the panels.
     */
    private void incrememntTurnOrder() {
        board.setCurrentPlayer(getNextActivePlayer());
        dealWithCheck();
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
    @Override
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
                playerOrder[1].getTimeTaken(),
                playerOrder[0].getTimeTaken()
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
    @Override
    public boolean tryKillAt(Square from, Square to) {
        Piece killedPiece = to.getPiece();
        if(killedPiece.getFaction() == Faction.BLACK) {
            killedBlackPieces.add(killedPiece);
        } else {
            killedWhitePieces.add(killedPiece);
        }
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
                playerOrder[1].getTimeTaken(),
                playerOrder[0].getTimeTaken()
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
            killedWhitePieces.remove(victim);
            killedBlackPieces.remove(victim);
        }
        dealWithCheck();
        drawBoard.tryRepaint();
    }

    @Override
    public void onEndGame() {

    }


    /**
     * Don't even know.
     * why is this even here?
     * @param b
     * @return
     */
    private Player whosInCheck(Board b) {
//        Piece whiteInCheck = b.checkForOffend(whiteKing);
//        Piece blackInCheck = b.checkForOffend(blackKing);

//        if(whiteInCheck != null) return whitePlayer;
//        else if(blackInCheck != null) return blackPlayer;
        return null;
    }
}
