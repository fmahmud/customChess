package chess.baseObjects;

import chess.general.Loggable;

import java.util.Vector;

/**
 * Created by Fez on 9/18/14.
 *
 */
public class Pathfinder extends Loggable {

//    public abstract Vector<Square>[] generatePath(int row, int col, Board b, MoveStyle ms);

    public Pathfinder() {
        super("PathFinder");
    }

    //why is this here?
    public Vector<Square> removeInvalidDestinations(Vector<Square> v, Piece piece) {
        return null;
    }

    private Vector<Square>[] getInfDiagPaths(int row, int col, Piece self, Board b, MoveStyle ms) {
        Vector<Square> validMoveDestinations = new Vector<Square>();
        Vector<Square> validKillDestinations = new Vector<Square>();
        int tx = col + ms.getDx();
        int ty = row + ms.getDy();
        Square target = b.getSquareAt(tx, ty);
        while (target != null) {
            target = b.getSquareAt(tx, ty);
            if (target == null)
                break;
            if (target.hasPiece()) {
                if (self.canKill(target.getPiece())) {
                    validKillDestinations.add(target);
                }
                break;
            } else {
                validMoveDestinations.add(target);
                tx += ms.getDx();
                ty += ms.getDy();
            }
        }
        return new Vector[]{validMoveDestinations, validKillDestinations};
    }

    private Vector<Square>[] getInfVertPaths(int row, int col, Piece self, Board b, MoveStyle ms) {
        Vector<Square> validMoveDestinations = new Vector<Square>();
        Vector<Square> validKillDestinations = new Vector<Square>();
        int tx = col + ms.getDx();
        int ty = row + ms.getDy();
        Square target = b.getSquareAt(tx, ty);
        while (target != null) {
            target = b.getSquareAt(tx, ty);
            if (target == null)
                break;
            if (target.hasPiece()) {
                if (self.canKill(target.getPiece())) {
                    validKillDestinations.add(target);
                }
                break;
            } else {
                validMoveDestinations.add(target);
                tx += ms.getDx();
            }
        }
        return new Vector[]{validMoveDestinations, validKillDestinations};
    }

    private Vector<Square>[] getInfHorizPaths(int row, int col, Piece self, Board b, MoveStyle ms) {
        Vector<Square> validMoveDestinations = new Vector<Square>();
        Vector<Square> validKillDestinations = new Vector<Square>();
        int tx = col + ms.getDx();
        int ty = row + ms.getDy();
        Square target = b.getSquareAt(tx, ty);
        while (target != null) {
            target = b.getSquareAt(tx, ty);
            if (target == null)
                break;
            if (target.hasPiece()) {
                if (self.canKill(target.getPiece())) {
                    validKillDestinations.add(target);
                }
                break;
            } else {
                validMoveDestinations.add(target);
                ty += ms.getDy();
            }
        }
        return new Vector[]{validMoveDestinations, validKillDestinations};
    }

    private Vector<Square>[] getDiscretePaths(int row, int col, Piece self, Board b, MoveStyle ms) {
        Vector<Square> validMoveDestinations = new Vector<Square>();
        Vector<Square> validKillDestinations = new Vector<Square>();
        int tx = col + ms.getDx();
        int ty = row + ms.getDy();
        Square targetSquare = b.getSquareAt(tx, ty);

        if (targetSquare == null)
            return new Vector[]{validMoveDestinations, validKillDestinations};

        Piece p = targetSquare.getPiece();
        if (ms.doesCollideDuring()) {
            //pawn/king
            int eigX = ms.getDx() > 0 ? 1 : ms.getDx() == 0 ? 0 : -1;
            int eigY = ms.getDy() > 0 ? 1 : ms.getDy() == 0 ? 0 : -1;

            int interX = col + eigX;
            int interY = row + eigY;

            Square tempSquare = b.getSquareAt(interX, interY);

            while (tempSquare != null) {
                tempSquare = b.getSquareAt(interX, interY);
                if (tempSquare == null)
                    break;
                if (tempSquare.hasPiece()) {
                    if (self.canKill(tempSquare.getPiece())) {
                        if (ms.isKillOnly() || ms.isBoth())
                            validKillDestinations.add(tempSquare);
                    }
                    break;
                } else {
                    if (ms.isMoveOnly() || ms.isBoth())
                        validMoveDestinations.add(tempSquare);
                    interX += eigX;
                    interY += eigY;
                }

                if (tempSquare == targetSquare) {
                    break;
                }
            }
        } else { //does not collide during
            //knight
            logLine("Doesn't collide during", 3);
            if (p == null) {
                logLine("Target square is vacant", 3);
                //if there is no piece in the resulting square
                if (ms.isKillOnly()) {
                    //if the move is kill only
                } else {
                    //the move is not kill only
                    //todo: does not cause a self check
                    validMoveDestinations.add(targetSquare);
                }
            } else {
                logLine("Target square is not vacant", 3);
                //there is a piece on the resulting square
                if (self.canKill(p)) { //if target piece can be killed
                    if (!ms.isMoveOnly()) {
                        validKillDestinations.add(targetSquare);
                    }
                } else { //if target piece cannot be killed
                    if (ms.isKillOnly()) {
                        //do nothing - something for later?
                    } else {
                        //do nothing - something for later?
                        //todo: can double occupy?
                    }
                }
            }
        }
        return new Vector[]{validMoveDestinations, validKillDestinations};
    }


    public Vector<Square>[] generatePath(int row, int col, Board b, MoveStyle ms) {
        Piece self = b.getSquareAt(col, row).getPiece();
        logLine("Finding path for: " + self.getPieceName(), 3);
        Vector[] validDestinations = new Vector[2];
        validDestinations[0] = new Vector<Square>();
        validDestinations[1] = new Vector<Square>();
        if (self.hasMoved() && ms.getFirstMoveOnly()) {
            return validDestinations;
        }
        //todo: verify current player is not in check currently
        int tx, ty;
        Vector[] temp = null;
        if (ms.canMoveInfHor() && ms.canMoveInfVer()) {
            //can move infinitely diagonally - bishop/queen
            temp = getInfDiagPaths(row, col, self, b, ms);
        } else if (ms.canMoveInfHor() && !ms.canMoveInfHor()) {
            //can move infinitely horizontally
            temp = getInfVertPaths(row, col, self, b, ms);
        } else if (!ms.canMoveInfHor() && ms.canMoveInfVer()) {
            //can move infinitely vertically
            temp = getInfHorizPaths(row, col, self, b, ms);
        } else if (!ms.canMoveInfHor() && !ms.canMoveInfHor()) {
            //cannot move infinitely - pawn/knight/king
            temp = getDiscretePaths(row, col, self, b, ms);
        }
        validDestinations[0].addAll(temp[0]);
        validDestinations[1].addAll(temp[1]);
        return validDestinations;
    }

}
