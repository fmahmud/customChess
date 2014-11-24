package chess.objects;

import chess.general.Loggable;

/**
 * Created by Fez on 9/18/14.
 */
public class Pathfinder extends Loggable {
    private Board board;

//    public abstract Vector<Square>[] generatePath(int row, int col, Board b, MoveStyle ms);

    public Pathfinder(Board b) {
        super("PathFinder");
        board = b;
    }

    private void getInfPaths(Piece self, MoveStyle ms,
                             MoveDestinations moveDestinations) {
        int tx = self.getCurrentColumn() + ms.getDx();
        int ty = self.getCurrentRow() + ms.getDy();
        Square target = board.getSquareAt(tx, ty);
        while (target != null) {
            target = board.getSquareAt(tx, ty);
            if (target == null)
                break;
            if (target.hasPiece()) {
                if (self.canKill(target.getPiece())) {
                    moveDestinations.addKillLocation(target);
                }
                break;
            } else {
                moveDestinations.addMoveLocation(target);
                tx += ms.getDx();
                ty += ms.getDy();
            }
        }
    }


//    private void getInfDiagPaths(Piece self, MoveStyle ms,
//                                 MoveDestinations moveDestinations) {
//        int tx = self.getCurrentColumn() + ms.getDx();
//        int ty = self.getCurrentRow() + ms.getDy();
//        Square target = board.getSquareAt(tx, ty);
//        while (target != null) {
//            target = board.getSquareAt(tx, ty);
//            if (target == null)
//                break;
//            if (target.hasPiece()) {
//                if (self.canKill(target.getPiece())) {
//                    moveDestinations.addKillLocation(target);
//                }
//                break;
//            } else {
//                moveDestinations.addMoveLocation(target);
//                tx += ms.getDx();
//                ty += ms.getDy();
//            }
//        }
//    }
//
//    private Vector<Square>[] getInfVertPaths(int row, int col, Piece self, Board b, MoveStyle ms) {
//        Vector<Square> validMoveDestinations = new Vector<Square>();
//        Vector<Square> validKillDestinations = new Vector<Square>();
//        int tx = col + ms.getDx();
//        int ty = row + ms.getDy();
//        Square target = b.getSquareAt(tx, ty);
//        while (target != null) {
//            target = b.getSquareAt(tx, ty);
//            if (target == null)
//                break;
//            if (target.hasPiece()) {
//                if (self.canKill(target.getPiece())) {
//                    validKillDestinations.add(target);
//                }
//                break;
//            } else {
//                validMoveDestinations.add(target);
//                tx += ms.getDx();
//            }
//        }
//        return new Vector[]{validMoveDestinations, validKillDestinations};
//    }
//
//    private Vector<Square>[] getInfHorizPaths(int row, int col, Piece self, Board b, MoveStyle ms) {
//        Vector<Square> validMoveDestinations = new Vector<Square>();
//        Vector<Square> validKillDestinations = new Vector<Square>();
//        int tx = col + ms.getDx();
//        int ty = row + ms.getDy();
//        Square target = b.getSquareAt(tx, ty);
//        while (target != null) {
//            target = b.getSquareAt(tx, ty);
//            if (target == null)
//                break;
//            if (target.hasPiece()) {
//                if (self.canKill(target.getPiece())) {
//                    validKillDestinations.add(target);
//                }
//                break;
//            } else {
//                validMoveDestinations.add(target);
//                ty += ms.getDy();
//            }
//        }
//        return new Vector[]{validMoveDestinations, validKillDestinations};
//    }

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
//            logLine("Doesn't collide during", 3);
            if (victim == null) {
//                logLine("Target square is vacant", 3);
                //if there is no piece in the resulting square
                if (ms.isKillOnly()) {
                    //if the move is kill only
                } else {
                    //the move is not kill only
                    moveDestinations.addMoveLocation(targetSquare);
                }
            } else {
//                logLine("Target square is not vacant", 3);
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
                        //idea: can double occupy?
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
        //todo: verify current player is not in check currently
        if (ms.canMoveInfHor() || ms.canMoveInfVer()) {
            //can move infinitely - bishop/queen/rook
            getInfPaths(self, ms, moveDestinations);
        } else if (!ms.canMoveInfHor() && !ms.canMoveInfHor()) {
            //cannot move infinitely - pawn/knight/king
            getFinitePaths(self, ms, moveDestinations);
        }
    }

}
