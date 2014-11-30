package chess.game.objects;

import chess.gui.objects.AbstractSlate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Fez on 11/13/14.
 */
public class History {
    MouseListener listMouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                //todo: double click logic goes here.
            } else {
                //?
            }
        }
    };
    private Vector<Event> future;
    private JList listHistory;
    private DefaultListModel listData;
    private JScrollPane scrPaneHistory;

    public History() {
        future = new Stack<Event>();
        initJList();
    }

    public int getDepth() {
        return listData.size();
    }

    public JScrollPane getList() {
        return scrPaneHistory;
    }

    private void initJList() {
        listData = new DefaultListModel();
        listHistory = new JList(listData);
        listHistory.setEnabled(false);
        listHistory.addMouseListener(listMouseListener);
        listHistory.setLayoutOrientation(JList.VERTICAL);
        listHistory.setCellRenderer(new HistoryListCellRenderer());
        listHistory.setBackground(Color.black);
        scrPaneHistory = new JScrollPane(listHistory);
        scrPaneHistory.setPreferredSize(new Dimension(AbstractSlate.sideWidth, AbstractSlate.centerWidth - 40));
    }

    public void push(Event e) {
        future.clear();
        listData.add(0, e);
        listHistory.updateUI();
    }

    public Event pop() {
        //todo: fix get some meaningful way to deal with reverting board.
        if (listData.size() == 0) return null;
        Event toRet = (Event) listData.remove(0);
        future.add(0, toRet);
        listHistory.updateUI();
        return toRet;
    }

    public class HistoryListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            Event e = (Event) listData.elementAt(index);
            if (isSelected && cellHasFocus) {
                e.setSelectedColors();
            } else {
                e.setUnselectedColors();
            }
            return e.getCanvas();

        }
    }
}
