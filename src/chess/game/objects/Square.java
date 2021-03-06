package chess.game.objects;

import chess.gui.objects.DrawableObject;

import java.awt.*;

public class Square extends DrawableObject {
    private static int width;
    private int row, column;
    private Color color, overlay;
    private Piece piece;

    public Square(int _col, int _row, Color _color) {
        super("Square(" + _row + ", " + _col + ")");
        logLine("Constructing Square", 2);
        setRow(_row);
        setColumn(_col);
        color = _color;
        overlay = new Color(0, 0, 0, 0);
        logLine("Done Constructing Square", 2);
    }

    public static void setWidth(int w) {
        width = w;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }


    public boolean setPiece(Piece p) {
        if (this.hasPiece() && p != null) return false;
        piece = p;
        if (p != null) {
            piece.setCurrentColumn(column);
            piece.setCurrentRow(row);
        }
        return true;
    }

    public void setOverlayColor(Color c) {
        overlay = c;
    }

    @Override
    public void render(Graphics g) {
        logLine("Rendering Square", 2);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRect(2, 2, width, width);
        if (hasPiece()) {
            piece.render(g);
        }
        g2.setColor(overlay);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.75f));
        g2.fillRect(2, 2, width, width);

        logLine("Done Rendering Square", 2);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getCoordinatesAsString() {
        return "(" + column + ", " + row + ")";
    }
}
