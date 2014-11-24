package chess.baseObjects;

import java.util.HashSet;

/**
 * Created by Fez on 11/23/14.
 */
public class MoveTree {
    private class Node {
        Move move, bestNextMove;
        HashSet<Node> nextMoves;

        Node(Move m) {
            move = m;
        }

        Move getBestNextMove() {
            return bestNextMove;
        }

        void addNode(Node n) {
            if(bestNextMove == null || n.move.getIndirectValue() > bestNextMove.getIndirectValue()) {
                bestNextMove = n.move;
            }
            nextMoves.add(n);
        }
    }

    Node root;

    public MoveTree(Move first) {
        root = new Node(first);
    }
}
