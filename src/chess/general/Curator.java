package chess.general;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Fez on 11/24/14.
 */
public class Curator extends Loggable {
    private HashMap<String, JSONObject> jsonObjectHashMap = new HashMap<String, JSONObject>();
    private HashMap<String, File> fileHashMap = new HashMap<String, File>();
    private String keyName;

    public Curator(String s, String _kn) {
        super(s);
        keyName = _kn;
    }

    private Vector<String> getKeys() {
        return new Vector<String>(jsonObjectHashMap.keySet());
    }

    private void addFile(File f) {
        if (!f.getName().endsWith(".json")) return;
        JSONObject jsonObject = Common.getJSONObjFromFile(f);
        jsonObjectHashMap.put(jsonObject.getString(keyName), jsonObject);
        fileHashMap.put(jsonObject.getString(keyName), f);
    }

    public void addDirectory(File file) {
        if (file.isDirectory()) {
            Vector<File> files = Common.getFilesInDir(file);
            for (File f : files) {
                addFile(f);
            }
        } else {
            addFile(file);
        }
    }

    public JSONObject getJSONObject(String key) {
        return jsonObjectHashMap.get(key);
    }

    public boolean addJSONObject(JSONObject obj) {
        if (jsonObjectHashMap.containsKey(obj.getString(keyName)))
            return false;
        jsonObjectHashMap.put(obj.getString(keyName), obj);
        return true;
    }

    private boolean moveFile(File oldFile, File newFile) throws IOException {
        return oldFile.delete() && newFile.createNewFile();
    }

    private void deleteAllFiles(Vector<File> files) {
        for (File f : files) {
            f.delete();
        }
    }

    private void prune() {
        Vector<String> jsonKeys = new Vector<String>(jsonObjectHashMap.keySet());
        Vector<File> removedFiles = Common.removeOutliers(jsonKeys, fileHashMap);
        deleteAllFiles(removedFiles);
        Vector<String> fileKeys = new Vector<String>(fileHashMap.keySet());
        Vector<JSONObject> removedJSONObjects = Common.removeOutliers(fileKeys, jsonObjectHashMap);
    }

    public void saveAllItems() {
        prune();
        Vector<String> keys = getKeys();
        for (String s : keys) {
            File f = fileHashMap.get(s);
            if (f == null) continue;
            try {
                Common.overWriteFile(f, jsonObjectHashMap.get(s).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector<JSONObject> getAllJSONObjects() {
        return new Vector<JSONObject>(jsonObjectHashMap.values());
    }

    public void updateJSONObject(String key, JSONObject newObj) {
        jsonObjectHashMap.put(key, newObj);
    }
}
