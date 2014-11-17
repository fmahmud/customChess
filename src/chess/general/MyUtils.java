package chess.general;

import chess.master.ConfigHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Fez on 9/14/14.
 *
 */
public class MyUtils {
    public static Font buttonFont = new Font("FacitWeb-Regular", Font.PLAIN, 14);


    public static <T> Vector<T> intersect(Vector<T> a, Vector<T> b) {
        Vector<T> c = new Vector<T>();
        for(int i = 0; i < a.size(); i++) {
            if(b.contains(a.get(i))) {
                c.add(a.get(i));
            }
        }
        return c;
    }

    public static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        Set<T> c = new HashSet<T>();
        for(T o : a) {
            if(b.contains(o)) {
                c.add(o);
            }
        }
        return c;
    }

    public static JButton buttonFactory(String name, String action, Font f) {
        JButton b = new JButton(name);
        b.setActionCommand(action);
        b.setFont(f);
        return b;
    }

    public static JButton buttonFactory(String name, String action, Font f, ActionListener al) {
        JButton b = new JButton(name);
        b.setActionCommand(action);
        b.setFont(f);
        b.addActionListener(al);
        return b;
    }

    public static JButton buttonFactory(String name, String action, ActionListener al) {
        JButton b = new JButton(name);
        b.setActionCommand(action);
        b.addActionListener(al);
        b.setFont(ConfigHandler.defaultFont);
        return b;
    }

    public static <T> Vector<T> removeDuplicates(Vector<T> a) {
        Vector<T> toRet = new Vector<T>();
        for(T t : a) {
            if(toRet.contains(t))
                continue;
            toRet.add(t);
        }
        return toRet;
    }

    public static Vector<File> getFilesInDir(File directory) {
        File[] temp = directory.listFiles();
        if(temp != null)
            return new Vector<File>(Arrays.asList(temp));
        else return new Vector<File>();
    }

    public static JSONObject getJSONObjFromFile(File f) {
        return new JSONObject(readWholeFile(f));
    }


    public static String readWholeFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } catch(Exception e) {

        }
        return "";
    }

    public static void overWriteFile(File file, String s) {

    }

}
