package chess.game.objects;

/**
 * Created by Fez on 11/27/14.
 */
public interface ActionCallBack {

    boolean registerAction(Piece actor, Square to);

}
