package chess.game.objects;

import chess.general.Loggable;

import java.util.Vector;

/**
 * Created by Fez on 9/18/14.
 */
public class Pathfinder extends Loggable {
    private Board board;

    public Pathfinder(Board b) {
        super("PathFinder");
        board = b;
    }

    private void getInfPaths(Piece self, MoveStyle ms,
                             MoveDestinations moveDestinations) {
        int tx = self.getCurrentColumn() + ms.getDx();
        int ty = self.getCurrentRow() + ms.getDy();
        Square target;

        Piece firstPiece = null;
        Piece secondPiece = null;
        Piece victim;
        Vector<Square> currentPath = new Vector<Square>();
        while ((target = board.getSquareAt(tx, ty)) != null) {
            if ((victim = target.getPiece()) != null) {
                if(firstPiece == null) {
                    firstPiece = victim;
                } else if(secondPiece == null) {
                    secondPiece = victim;
                } else {
                    break;
                }
            } else {
                if(firstPiece == null)
                    moveDestinations.addMoveLocation(target);
            }
            currentPath.add(target);
            tx += ms.getDx();
            ty += ms.getDy();
        }

        if(firstPiece != null) {
            if(self.canKill(firstPiece)) {
                if(firstPiece.isObjective()) {
                    //check
                    logLine("Check!", 0);
                    moveDestinations.addPathToObjective(currentPath);
                } else if(secondPiece != null && self.canKill(secondPiece) && secondPiece.isObjective()) {
                    //pin
                    logLine("Pin!", 0);
                    moveDestinations.addPinnedPiece(firstPiece, currentPath);
                }
                moveDestinations.addKillLocation(
                        board.getSquareAt(
                        firstPiece.getCurrentColumn(),
                        firstPiece.getCurrentRow())
                );
            }
        }
    }

    private void getFinitePaths(Piece self, MoveStyle ms, MoveDestinations moveDestinations) {
        int col = self.getCurrentColumn();
        int row = self.getCurrentRow();
        int tx = col + ms.getDx();
        int ty = row + ms.getDy();
        Square targetSquare = board.getSquareAt(tx, ty);

        if (targetSquare == null)
            return;

        Piece victim = targetSquare.getPiece();
        if (ms.doesCollideDuring()) {
            //pawn/king
            int eigX = ms.getDx() > 0 ? 1 : ms.getDx() == 0 ? 0 : -1;
            int eigY = ms.getDy() > 0 ? 1 : ms.getDy() == 0 ? 0 : -1;

            int interX = col + eigX;
            int interY = row + eigY;

            Square tempSquare = board.getSquareAt(interX, interY);

            while (tempSquare != null) {
                tempSquare = board.getSquareAt(interX, interY);
                if (tempSquare == null)
                    break;
                if (tempSquare.hasPiece()) {
                    if (self.canKill(tempSquare.getPiece())) {
                        if (ms.isKillOnly() || ms.isBoth())
                            moveDestinations.addKillLocation(tempSquare);
                    }
                    break;
                } else {
                    if (ms.isMoveOnly() || ms.isBoth())
                        moveDestinations.addMoveLocation(tempSquare);
                    interX += eigX;
                    interY += eigY;
                }

                if (tempSquare == targetSquare) {
                    break;
                }
            }
        } else { //does not collide during
            //knight
            if (victim == null) {
                //if there is no piece in the resulting square
                if (ms.isKillOnly()) {
                    //if the move is kill only
                } else {
                    //the move is not kill only
                    moveDestinations.addMoveLocation(targetSquare);
                }
            } else {
                //there is a piece on the resulting square
                if (self.canKill(victim)) { //if target piece can be killed
                    if (!ms.isMoveOnly()) {
                        moveDestinations.addKillLocation(targetSquare);
                        if(victim.isObjective()) {
                            //check
                            Vector<Square> path = new Vector<Square>();
                            path.add(board.getSquareAt(
                                    self.getCurrentColumn(),
                                    self.getCurrentRow()));
                            moveDestinations.addPathToObjective(path);
                        }
                    }
                } else { //if target piece cannot be killed
                    if (ms.isKillOnly()) {
                        //do nothing - something for later?
                    } else {
                        //do nothing - something for later?
                    }
                }
            }
        }
    }


    public void generatePath(Piece self, MoveStyle ms,
                             MoveDestinations moveDestinations) {
        logLine("Finding path for: " + self.getPieceName(), 3);
        if (self.hasMoved() && ms.getFirstMoveOnly()) {
            return;
        }
        if (ms.canMoveInfHor() || ms.canMoveInfVer()) {
            //can move infinitely - bishop/queen/rook
            getInfPaths(self, ms, moveDestinations);
        } else if (!ms.canMoveInfHor() && !ms.canMoveInfHor()) {
            //cannot move infinitely - pawn/knight/king
            getFinitePaths(self, ms, moveDestinations);
        }
    }

}
