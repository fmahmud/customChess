package chess.master;

import chess.general.Common;
import chess.general.Loggable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Fez on 11/18/14.
 */
public class PieceLibrary extends Loggable {
    private HashMap<String, JSONObject> pieceNameMap = new HashMap<String, JSONObject>();
    private HashMap<String, File> nameFileMap = new HashMap<String, File>();

    public PieceLibrary(File directory) {
        super("PieceLibrary");
        logLine("Enumerating Library from directory: "+directory.getAbsolutePath(), 0);
        Vector<File> files = Common.getFilesInDir(directory);
        logLine("Found "+files.size()+" files in directory.", 0);
        for(File f : files) {
            if(!f.getName().endsWith(".json")) continue;
            JSONObject piece = Common.getJSONObjFromFile(f);
            pieceNameMap.put(piece.getString("name"), piece);
            nameFileMap.put(piece.getString("name"), f);
        }
        logLine("Done loading pieces.", 0);
    }

    public JSONObject getPieceJSON(String name) {
        return pieceNameMap.get(name);
    }

    public boolean addPiece(JSONObject p) {
        if(pieceNameMap.containsKey(p.getString("name")))
            return false;
        pieceNameMap.put(p.getString("name"), p);
        return true;
    }

    public void updatePiece(JSONObject newValue, String currentKey) {
        String newName = newValue.getString("name");
        if (!newName.equals(currentKey)) {
            File newFile = new File(newName+".png");
            File oldFile = nameFileMap.remove(currentKey);
            if (!oldFile.renameTo(newFile)) { //renamed file
                //handle?
            } else {
                nameFileMap.put(newName, newFile);
            }
        }
        pieceNameMap.put(newValue.getString("name"), newValue);
    }

    public void saveAllPieces() {
        Vector<String> keys = new Vector<String>(pieceNameMap.keySet());
        logLine("Num keys = "+keys.size(), 0);
        /*
        for(String name : keys) {
            try {
                Common.overWriteFile(nameFileMap.get(name), pieceNameMap.get(name).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public Vector<JSONObject> getAllPiecesAsVector() {
        return new Vector<JSONObject>(pieceNameMap.values());
    }

}
