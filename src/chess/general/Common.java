package chess.general;

import chess.config.ConfigMaster;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * Created by Fez on 9/14/14.
 *
 */
public class Common extends Loggable {
    public static Font buttonFont = new Font("FacitWeb-Regular", Font.PLAIN, 14);

    public Common(String s) {
        super(s);
    }


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

    public static <T, V> Vector<V> removeOutliers(final Vector<T> allowed, HashMap<T, V> hashMap) {
        Vector<T> keys = new Vector<T>(hashMap.keySet());
        Vector<V> removed = new Vector<V>();
        for(T t : keys) {
            if(allowed.contains(t)) continue;
            removed.add(hashMap.remove(t));
        }
        return removed;
    }

    /**
     * Returns a <code>HashSet</code> containing n random numbers between min and max
     * inclusive. Returns whole range if n > (max - min)
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static HashSet<Integer> getNRandomUniqueNumbersBetween(int min, int max, int n) {
        HashSet<Integer> toRet = new HashSet<Integer>();
        int rangeSize = max - min;
        if(n > rangeSize) n = rangeSize;
        while(toRet.size() < n) {
            toRet.add((int)(Math.random() * rangeSize) + min + 1);
        }
        return toRet;
    }

    public static HashSet<String> getNRandomUniqueLinesInFile(int n, File f) throws IOException {
        HashSet<String> toRet = new HashSet<String>();
        int linesInFile = countLines(f);
        if(n > linesInFile) n = linesInFile;
        HashSet<Integer> numbers = Common.getNRandomUniqueNumbersBetween(0, linesInFile, n);
        FileInputStream fs = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        Integer i = 0;
        while(toRet.size() < n) {
            String s = br.readLine();
            if(numbers.contains(i)) {
                toRet.add(s);
            }
            ++i;
        }
        return toRet;
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
        b.setFont(ConfigMaster.defaultFont);
        return b;
    }

    public static JLabel labelFactory(String title) {
        JLabel l = new JLabel(title);
        l.setFont(ConfigMaster.defaultFont);
        return l;
    }

    public static JTextField textFieldFactory(int numCols) {
        JTextField toRet = new JTextField(numCols);
        toRet.setFont(ConfigMaster.textFieldFont);
        return toRet;
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
            reader.close();
            return stringBuilder.toString();
        } catch(Exception e) {

        }
        return "";
    }


    public static void overWriteFile(File file, String s) throws IOException {
        PrintWriter pw = new PrintWriter(file);
        pw.write(s);
        pw.flush();
        pw.close();
    }

    public static int countLines(File f) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }


    /**
     * Returns k samples from the file f. Assuming each sample is on a
     * separate line.
     * if k > n then null strings will be at the end of the array
     * @param f
     * @return
     */
    public static String getRandomLineInFile(File f) throws IOException {
        int numLinesInFile = countLines(f);
        int randomLineNum = (int)(Math.random() * numLinesInFile);
        FileInputStream fs = new FileInputStream(f);
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        for(int i = 0; i != randomLineNum; ++i) {
            br.readLine();
        }
        String toRet =  br.readLine();
        fs.close();
        br.close();
        return toRet;
    }

}
