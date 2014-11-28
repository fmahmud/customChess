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

        boolean lookingForPin = false;
        boolean foundPin = false;
        Piece possiblePinnedPiece = null;
        Piece victim;
        Vector<Square> currentPath = new Vector<Square>();
        while ((target = board.getSquareAt(tx, ty)) != null) {
            if ((victim = target.getPiece()) != null) {
                if (self.canKill(victim)) {
                    if(!lookingForPin) {
                        // when not looking for pin add the victim's location
                        // as a valid kill destination
                        moveDestinations.addKillLocation(target);
                        if(victim.isObjective()) {
                            // if the victim is an objective... (when not looking for pin)
                            // must be check...
                            moveDestinations.addPathToObjective(currentPath);
                        } else {
                            // if the victim is not an objective
                            // start looking for pin
                            lookingForPin = true;
                            possiblePinnedPiece = victim;
                        }

                    } else {
                        if(victim.isObjective()) {
                            // while looking for pin if the current victim is
                            // an objective, pin has been found.
                            foundPin = true;
                        }
                        break;
                    }
                } else {
                    break;
                }
            } else {
                // when there is no piece on target square,
                if(!lookingForPin)
                    moveDestinations.addMoveLocation(target);
                currentPath.add(target);
            }
            tx += ms.getDx();
            ty += ms.getDy();
        }
        if(foundPin)
            moveDestinations.addPinnedPiece(possiblePinnedPiece, currentPath);
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
