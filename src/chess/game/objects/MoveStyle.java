package chess.game.objects;

import org.json.JSONObject;

/**
 * Each piece of the same type has the same (static) set of
 * <code>MoveStyle</code>s. This ensures that reduandant copies
 * are not present. However, each piece has it's own set of valid
 * destinations. These can be calculated by it's various
 * <code>MoveStyles</code>
 */
public class MoveStyle {

    private int dx, dy;
    private int distance;
    private boolean collidesDuring;
    private boolean collidesAtEnd;
    private MoveObjective mvObj;
    private String specialMove; //castling
    private boolean firstMoveOnly = false;
    public MoveStyle(int _dx, int _dy,
                     boolean _collidesDuring, boolean _collidesAtEnd, MoveObjective _mvObj,
                     boolean _firstOnly, int _distance) {
        define(_dx, _dy, _collidesDuring, _collidesAtEnd, _mvObj, _firstOnly, _distance);
    }

    public MoveStyle(JSONObject _ms) {
        define(
                _ms.getInt("dx"),
                _ms.getInt("dy"),
                _ms.getBoolean("collidesDuring"),
                _ms.getBoolean("collidesAtEnd"),
                getMoveObjFromString(_ms.getString("moveObjective")),
                _ms.getBoolean("firstMoveOnly"),
                _ms.getInt("distance")
        );
    }

    private void define(int _dx, int _dy,
                        boolean _collidesDuring, boolean _collidesAtEnd, MoveObjective _mvObj,
                        boolean _fOnly, int _distance) {
        this.setDx(_dx);
        this.setDy(_dy);
        this.setCollidesDuring(_collidesDuring);
        this.setCollidesAtEnd(_collidesAtEnd);
        this.setMoveObjective(_mvObj);
        this.firstMoveOnly = _fOnly;
        this.distance = _distance;
    }

    private MoveObjective getMoveObjFromString(String s) {
        if (s.equals("KILL_ONLY")) return MoveObjective.KILL_ONLY;
        if (s.equals("MOVE_ONLY")) return MoveObjective.MOVE_ONLY;
        if (s.equals("BOTH")) return MoveObjective.BOTH;
        else return MoveObjective.BOTH;
    }

    private String getStringFromMoveObj(MoveObjective mo) {
        switch (mo) {
            case MOVE_ONLY:
                return "MOVE_ONLY";
            case KILL_ONLY:
                return "KILL_ONLY";
            case BOTH:
                return "BOTH";
            default:
                return "BOTH";
        }
    }

    public JSONObject getAsJSONObject() {
        JSONObject toRet = new JSONObject();
        toRet.put("dx", dx);
        toRet.put("dy", dy);
        toRet.put("collidesDuring", collidesDuring);
        toRet.put("collidesAtEnd", collidesAtEnd);
        toRet.put("moveObjective", getStringFromMoveObj(mvObj));
        toRet.put("firstMoveOnly", firstMoveOnly);
        toRet.put("distance", distance);
        return toRet;
    }

    public boolean getFirstMoveOnly() {
        return firstMoveOnly;
    }

    public void setMoveObjective(MoveObjective moveObjective) {
        this.mvObj = moveObjective;
    }

    public int getDx() {
        return dx;
    }

    private void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    private void setDy(int dy) {
        this.dy = dy;
    }

    public boolean doesCollideDuring() {
        return collidesDuring;
    }

    private void setCollidesDuring(boolean collides) {
        this.collidesDuring = collides;
    }

    public boolean doesCollideAtEnd() {
        return collidesAtEnd;
    }

    private void setCollidesAtEnd(boolean collidesAtEnd) {
        this.collidesAtEnd = collidesAtEnd;
    }

    public boolean isKillOnly() {
        return mvObj == MoveObjective.KILL_ONLY;
    }

    public boolean isMoveOnly() {
        return mvObj == MoveObjective.MOVE_ONLY;
    }

    public boolean isBoth() {
        return mvObj == MoveObjective.BOTH;
    }

    @Override
    public String toString() {
        return getAsJSONObject().toString();
    }

    public int getDistance() {
        return distance;
    }

    public enum MoveObjective {
        KILL_ONLY("KILL_ONLY"),
        MOVE_ONLY("MOVE_ONLY"),
        BOTH("BOTH");

        private final String name;

        private MoveObjective(String s) {
            name = s;
        }

        public static MoveObjective getFromName(String s) {
            if (s.equals("MOVE_ONLY"))
                return MOVE_ONLY;
            if (s.equals("KILL_ONLY"))
                return KILL_ONLY;
            if (s.equals("BOTH"))
                return BOTH;
            return BOTH;
        }

        public String toString() {
            return name;
        }
    }
}