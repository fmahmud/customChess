package chess.custom;

public enum Faction {
    BLACK, WHITE /*, NEUTRAL, HOSTILE, FRIENDLY*/;

    //idea: faction stats?

    public static String getStrRepresentation(Faction f) {
        if (f == null) return "";
        switch (f) {
            case BLACK:
                return "black";
            case WHITE:
                return "white";
            default:
                return "";
        }
    }
}
