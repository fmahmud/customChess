package chess.gui.objects;

import chess.config.ConfigMaster;
import chess.game.objects.*;
import chess.general.Loggable;
import chess.gui.GUIMaster;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * Created by Fez on 9/30/14.
 */

public class DrawableBoard extends Loggable {
    /**
     * Board is within DrawableBoard as a Board stores the state of the board
     * so it can be used as a class to store a possible future move etc.
     * without drawing it. Only a DrawableBoard can be drawn.
     */
    private Board board;
    private Square selectedSquare = null;
    private Piece selectedPiece = null;
    private Vector<ActionCallBack> onKillListeners, onMoveListeners;

    MoveDestinations defaultMoveDestinations = new MoveDestinations("default");
    MoveDestinations moveDestinations = defaultMoveDestinations;
    private JTable table;
    private JPanel canvas;

    public DrawableBoard(Board b, String prefix) {
        super(prefix);
        board = b;

        onKillListeners = new Vector<ActionCallBack>();
        onMoveListeners = new Vector<ActionCallBack>();

        initializeBoard();
    }

    private void initializeBoard() {
        logLine("Initializing Board", 2);
        int squareWidth = GUIMaster.SQUARE_WIDTH;
        Square.setWidth(squareWidth);
        canvas = new JPanel();
        canvas.setLayout(new BoxLayout(canvas, BoxLayout.Y_AXIS));
        DefaultTableModel tblModel = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        final int numCols = board.getWidth();
        final int numRows = board.getHeight();
        Dimension boardDim = new Dimension(squareWidth * numCols, squareWidth * numRows);
        table = new JTable(tblModel);
        table.setRowHeight(squareWidth);
        table.setPreferredSize(boardDim);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        String[] colTitles = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String s : colTitles) {
            tblModel.addColumn(s);
        }
        tblModel.setColumnCount(numCols);
        tblModel.setRowCount(numRows);
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                tblModel.setValueAt(board.getSquareAt(col, row), row, col);
            }
        }
        table.setDefaultRenderer(table.getColumnClass(0), new ChessBoardCellRenderer());
        table.addMouseListener(new TableMouseListener());
        canvas.add(table);
        logLine("Done Initializing Board", 2);
    }

    public void addKillListener(ActionCallBack acb) {
        if(!onKillListeners.contains(acb))
            onKillListeners.add(acb);
    }

    public void addMoveListener(ActionCallBack acb) {
        if(!onMoveListeners.contains(acb))
            onMoveListeners.add(acb);
    }

    public JPanel getCanvas() {
        return canvas;
    }

    private void onRightClick(Point locationOnScreen) {
        int targRow = table.rowAtPoint(locationOnScreen);
        int targCol = table.columnAtPoint(locationOnScreen);
        Square targetSquare = board.getSquareAt(targCol, targRow);
//        logLine("Right clicked on (" + targCol + ", " + targRow + ")", 3);
        if (selectedPiece != null && selectedPiece.getOwner() == board.getCurrentPlayer()) {
//            logLine("Sel piece is not null and owned by current player", 3);
            if (moveDestinations.canMoveTo(targetSquare)) {
//                logLine("Target square is a valid move destination", 3);
                tryMoveFromTo(selectedSquare, targetSquare);
            } else if (moveDestinations.canKillAt(targetSquare)) {
                tryKillAt(selectedSquare, targetSquare);
            }
        }
    }

    private void tryMoveFromTo(Square from, Square to) {
        for(ActionCallBack acb : onMoveListeners)
            acb.registerAction(from.getPiece(), to);

        clearTempVars();
        table.repaint();
    }

    private void tryKillAt(Square from, Square to) {
        for(ActionCallBack acb : onKillListeners)
            acb.registerAction(from.getPiece(), to);

        clearTempVars();
        table.repaint();
    }

    public void tryRepaint() {
        for(int i = 0 ; i < board.getHeight(); ++i ) {
            for(int j = 0; j < board.getWidth(); ++j) {
                board.getSquareAt(j, i).repaint();
            }
        }
        table.repaint();
    }

    private void clearTempVars() {
        selectedPiece = null;
        selectedSquare = null;
        moveDestinations = defaultMoveDestinations;
        table.clearSelection();
    }

    private boolean updateSelectedSquare(Square s) {
        if (s != selectedSquare) {
            if (selectedSquare != null)
                logLine("Changing selected square from ("
                        + selectedSquare.getColumn() + ", " + selectedSquare.getRow() + ") to "
                        + s.getColumn() + ", " + s.getRow() + ")", 1);
            selectedSquare = s;
            return true;
        }
        return false;
    }


    private boolean updateSelectedPiece(Piece p) {
        if (p != selectedPiece) {
            selectedPiece = p;

            if (selectedPiece != null && selectedPiece.getOwner() == board.getCurrentPlayer()) {
                moveDestinations = p.getMoveDestinations();
            } else {
                moveDestinations = defaultMoveDestinations;
            }
            return true;
        }
        return false;
    }

    public class ChessBoardCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean selected, boolean focused,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            Square currentSquare = board.getSquareAt(column, row);
            boolean repaint = false;
            if (updateSelectedSquare(board.getSquareAt(table.getSelectedColumn(), table.getSelectedRow()))) {
                repaint = updateSelectedPiece(selectedSquare.getPiece());
            }

            if (currentSquare == selectedSquare) {
                currentSquare.setOverlayColor(ConfigMaster.selectedItemColor);
                return currentSquare.getCanvas();
            }

            if (moveDestinations.canKillAt(currentSquare))
                currentSquare.setOverlayColor(ConfigMaster.offendLocationColor);
            else if (moveDestinations.canMoveTo(currentSquare))
                currentSquare.setOverlayColor(ConfigMaster.moveLocationColor);
            else
                currentSquare.setOverlayColor(ConfigMaster.transparentColor);

            if (repaint)
                table.repaint();

            return currentSquare.getCanvas();
        }
    }

    private class TableMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            switch (mouseEvent.getButton()) {
                case MouseEvent.BUTTON3:
                    onRightClick(mouseEvent.getPoint());
                    break;
                default:
                    break;

            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    }
}
