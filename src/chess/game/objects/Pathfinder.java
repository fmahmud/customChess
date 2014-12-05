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

    private void getInfPaths(Piece self, MoveStyle ms) {
        MoveDestinations moveDestinations = self.getMoveDestinations();
        int eigX = ms.getDx();
        int eigY = ms.getDy();
        int tx = self.getCurrentColumn() + eigX;
        int ty = self.getCurrentRow() + eigY;
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
                if(firstPiece == null && (ms.isMoveOnly() || ms.isBoth()))
                    moveDestinations.addMoveLocation(target);
            }
            currentPath.add(target);
            tx += eigX;
            ty += eigY;
        }

        if(firstPiece != null) {
            if(self.canKill(firstPiece) && (ms.isBoth() || ms.isKillOnly())) {
                if(firstPiece.isObjective()) {
                    //check
                    moveDestinations.addPathToObjective(currentPath);
                } else if(secondPiece != null && self.canKill(secondPiece) && secondPiece.isObjective()) {
                    //pin
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

    private void getFinitePaths(Piece self, MoveStyle ms) {
        MoveDestinations moveDestinations = self.getMoveDestinations();
        int tx = self.getCurrentColumn() + ms.getDx();
        int ty = self.getCurrentRow() + ms.getDy();
        Square targetSquare = board.getSquareAt(tx, ty);

        if (targetSquare == null) //out of bounds check
            return;
        Piece victim = targetSquare.getPiece();

        if (ms.doesCollideDuring()) {
            //pawn/king
            Vector<Square> currentPath = new Vector<Square>();
            Piece firstPiece = null;
            Piece secondPiece = null;

            int eigX = ms.getDx();
            int eigY = ms.getDy();

            int ftx = self.getCurrentColumn() + eigX;
            int fty = self.getCurrentRow() + eigY;
            Square target;
//            int omega = ms.getDistance() > 0 ? 1:-1; //converge or diverge?
            int distanceLeft = ms.getDistance();
            while ((target = board.getSquareAt(ftx, fty)) != null && distanceLeft > 0) {
                if((victim = target.getPiece()) != null) {
                    if(firstPiece == null) {
                        firstPiece = victim;
                    } else if(secondPiece == null) {
                        secondPiece = victim;
                    } else {
                        break;
                    }
                } else {
                    if(firstPiece == null && (ms.isMoveOnly() || ms.isBoth()))
                        moveDestinations.addMoveLocation(target);
                }
                currentPath.add(target);
                ftx += eigX;
                fty += eigY;
                distanceLeft -= (Math.abs(eigX) + Math.abs(eigY));
            }
            if(firstPiece != null) {
                if(self.canKill(firstPiece) && (ms.isBoth() || ms.isKillOnly())) {
                    if(firstPiece.isObjective()) {
                        //check
                        moveDestinations.addPathToObjective(currentPath);
                    } else if(secondPiece != null && self.canKill(secondPiece) && secondPiece.isObjective()) {
                        //pin
                        moveDestinations.addPinnedPiece(firstPiece, currentPath);
                    }
                    moveDestinations.addKillLocation(
                            board.getSquareAt(
                                    firstPiece.getCurrentColumn(),
                                    firstPiece.getCurrentRow())
                    );
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


    public void generatePath(Piece self, MoveStyle ms) {
//        logLine("Finding path for: " + self.getPieceName(), 3);
        if (self.hasMoved() && ms.getFirstMoveOnly()) {
            return;
        }
        if (ms.getDistance() < 0) {
            //can move infinitely - bishop/queen/rook
            getInfPaths(self, ms);
        } else {
            //cannot move infinitely - pawn/knight/king
            getFinitePaths(self, ms);
        }
    }

}
