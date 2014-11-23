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
public class PieceMaster extends Loggable {
    private HashMap<String, JSONObject> pieceNameMap = new HashMap<String, JSONObject>();
    private HashMap<String, File> nameFileMap = new HashMap<String, File>();

    public PieceMaster(File directory) {
        super("PieceLibrary");
        logLine("Enumerating Library from directory: "+directory.getAbsolutePath(), 1);
        Vector<File> files = Common.getFilesInDir(directory);
        logLine("Found "+files.size()+" files in directory.", 4);
        for(File f : files) {
            if(!f.getName().endsWith(".json")) continue;
            JSONObject piece = Common.getJSONObjFromFile(f);
            pieceNameMap.put(piece.getString("name"), piece);
            nameFileMap.put(piece.getString("name"), f);
        }
        logLine("Done loading pieces.", 1);
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

    private boolean renameFile(File oldFile, File newFile) throws IOException{
        return oldFile.delete() && newFile.createNewFile();
    }

    public void updatePiece(JSONObject newValue, String currentKey) {
        String newName = newValue.getString("name");
        logLine("New name = "+newName+", old name = "+currentKey, 0);
        if (!newName.equals(currentKey)) {
            File newFile = new File(ConfigMaster.piecesLocation.getAbsolutePath()+"/"+newName+".json");
            File oldFile = nameFileMap.remove(currentKey);
            logLine("New Filename = "+newFile.getName()+", old filename = "+oldFile.getName(), 0);
            try {
                if(!renameFile(oldFile, newFile)) {
                    logLine("Couldn't rename file...", 0);
                } else {
                    logLine("Renamed file: "+oldFile.getName(), 0);
                    nameFileMap.put(newName, newFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pieceNameMap.put(newName, newValue);
    }

    public void saveAllPieces() {
        Vector<String> keys = new Vector<String>(pieceNameMap.keySet());
        logLine("Num keys = "+keys.size(), 4);

        for(String name : keys) {
            logLine(name+": "+pieceNameMap.get(name), 4);
            try {
                File f = nameFileMap.get(name);
                if(f == null) continue;
                logLine("Writing to file: "+f.getName(), 0);
                Common.overWriteFile(f, pieceNameMap.get(name).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector<JSONObject> getAllPiecesAsVector() {
        return new Vector<JSONObject>(pieceNameMap.values());
    }

}
