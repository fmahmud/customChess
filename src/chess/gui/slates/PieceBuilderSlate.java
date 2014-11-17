package chess.gui.slates;

import chess.baseObjects.Piece;
import chess.general.MyUtils;
import chess.gui.objects.AbstractSlate;
import chess.master.ConfigHandler;
import chess.master.GUIMaster;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/**
 * Created by Fez on 11/14/14.
 */
public class PieceBuilderSlate extends AbstractSlate {

    private JList pieceList, movesList;
    private DefaultListModel pieces, moveStyles;
    private JScrollPane scrollPanePieces, scrollPaneMoveStyles;
    private AbstractSlate returnTo;
    private Vector<Piece>  thePieces;
    private Vector<JSONObject> jsonPieces, jsonMoveStyles;
    private JButton btnAccept;
    private JButton addPiece, removePiece, btnAddMS, btnRemoveMS, btnSaveMSChanges, btnResetMS;
    private JComboBox cbDX, cbDY, cbInfMoveX, cbInfMoveY, cbFirstMove, cbColDur, cbColEnd, cbMoveObjective;
    private JLabel lblDX, lblDY, lblInfMoveX, lblInfMoveY, lblFirstMove, lblColDur, lblColEnd, lblMoveObjective;
    private JTextArea taShowJSON;
    private JScrollPane taTemp;

    public PieceBuilderSlate(AbstractSlate _returnTo) {
        super("PieceBuilderSlate");
        panelSetup();
//        setSelectedPiece(getJSONFromList((String) pieces.get(0)));

        returnTo = _returnTo;
    }

    @Override
    protected void setupHeaderPanel() {

    }

    @Override
    protected void setupLeftPanel() {
        pieces = new DefaultListModel();
        pieceList = new JList(pieces);
        pieceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pieceList.setFont(ConfigHandler.defaultFont);
        pieceList.setCellRenderer(new PiecesListCellRenderer());
        scrollPanePieces = new JScrollPane(pieceList);
        scrollPanePieces.setPreferredSize(new Dimension(AbstractSlate.sideWidth, AbstractSlate.centerWidth-100));

        thePieces = new Vector<Piece>();
        jsonPieces = new Vector<JSONObject>();
        Vector<File> filesInDirectory = MyUtils.getFilesInDir(new File(ConfigHandler.piecesLocation));
        logLine("Num Files in directory = "+filesInDirectory.size(), 0);
        for(File f : filesInDirectory) {
            JSONObject temp = MyUtils.getJSONObjFromFile(f);
            logLine(temp.toString(), 4);
            Piece p = new Piece(temp);
            thePieces.add(p);
            jsonPieces.add(temp);
            pieces.addElement(p.getPieceName());
        }
        if(pieces.size()!=0)
            pieceList.setSelectedIndex(0);

        addPiece = MyUtils.buttonFactory("Add Piece", "addPiece", new AddPieceButtonListener());
        addPiece.setPreferredSize(new Dimension((AbstractSlate.sideWidth)-20, 40));
        removePiece = MyUtils.buttonFactory("Remove Piece", "removePiece", new RemovePieceButtonListener());
        removePiece.setPreferredSize(new Dimension((AbstractSlate.sideWidth)-20, 40));

        leftPanel.add(scrollPanePieces);
        leftPanel.add(addPiece);
        leftPanel.add(removePiece);

    }

    private JSONObject getJSONFromList(String name) {
        for(JSONObject j : jsonPieces) {
            if(j.has("name")) {
                if(j.getString("name").equals(name)) return j;
            }
        }
        return null;
    }

    private String makePretty(String s) {
        String toRet = s.replace("{", "{\n")
                        .replace("[", "[\n")
                        .replace(",\"", ",\n\"")
                        .replace("]", "\n]")
                        .replace("}", "\n}");
        return toRet;
    }

    private void setSelectedPiece(JSONObject piece) {
        jsonMoveStyles = new Vector<JSONObject>();
        taShowJSON.setText(makePretty(piece.toString()));
        moveStyles.clear();
        JSONArray _ms = piece.getJSONArray("moveStyles");
        for(int i = 0; i < _ms.length(); ++i) {
            jsonMoveStyles.add((JSONObject)_ms.get(i));
            moveStyles.addElement("Movestyle "+(i+1));
        }
    }

    private void setSelectedPieceIndex(int index) {
        setSelectedPiece(getJSONFromList((String) pieces.get(index)));
        movesList.setSelectedIndex(0);
        setSelectedMoveStyleFromIndex(0);
    }

    @Override
    protected void setupCenterPanel() {
        taShowJSON = new JTextArea();
        taTemp = new JScrollPane(taShowJSON);
        taTemp.setPreferredSize(new Dimension(AbstractSlate.centerWidth-20, AbstractSlate.centerWidth-20));
        centerPanel.add(taTemp);
    }

    private JPanel moveStylePanel;

    private void setSelectedMoveStyle(JSONObject ms) {
        cbDX.setSelectedItem(""+ms.getInt("dx"));
        cbDY.setSelectedItem("" + ms.getInt("dy"));
        cbColDur.setSelectedItem("" + ms.getBoolean("collidesDuring"));
        cbColEnd.setSelectedItem(""+ms.getBoolean("collidesAtEnd"));
    }

    private void setSelectedMoveStyleFromIndex(int index) {
        setSelectedMoveStyle(getMSJSONFromList(index));
    }

    private JSONObject getMSJSONFromList(int i) {
        return jsonMoveStyles.get(i);
    }

    @Override
    protected void setupRightPanel() {
        moveStyles = new DefaultListModel();
        movesList = new JList(moveStyles);
        movesList.setCellRenderer(new MoveStylesListCellRenderer());
        scrollPaneMoveStyles = new JScrollPane(movesList);
        scrollPaneMoveStyles.setPreferredSize(new Dimension(AbstractSlate.sideWidth, AbstractSlate.centerWidth/3));
        rightPanel.add(scrollPaneMoveStyles);

        btnAddMS = MyUtils.buttonFactory("Add Movestyle", "addMS", new AddMSButtonListener());
        btnRemoveMS = MyUtils.buttonFactory("Remove Movestyle", "remMS", new RemoveMSButtonListener());
        rightPanel.add(btnAddMS);
        rightPanel.add(btnRemoveMS);

        String[] nums = new String[15];
        for(int i = -7; i < 8; ++i) {
            nums[i+7] = ""+i;
        }

        String[] bools = {"true", "false"};
        moveStylePanel = new JPanel();
        moveStylePanel.setLayout(new BoxLayout(moveStylePanel, BoxLayout.Y_AXIS));
        moveStylePanel.setPreferredSize(new Dimension(AbstractSlate.sideWidth, 2*AbstractSlate.centerWidth/3));
        lblDX = new JLabel("Horizontal Motion Eigenvector: ");
        lblDY = new JLabel("Vertical Motion Eigenvector: ");
        cbDX = new JComboBox(nums);
        cbDY = new JComboBox(nums);
        moveStylePanel.add(lblDX);
        moveStylePanel.add(cbDX);
        moveStylePanel.add(lblDY);
        moveStylePanel.add(cbDY);

        lblColDur = new JLabel("Collides During Motion: ");
        lblColEnd = new JLabel("Collides at End of Motion: ");
        cbColDur = new JComboBox(bools);
        cbColEnd = new JComboBox(bools);

        moveStylePanel.add(lblColDur);
        moveStylePanel.add(cbColDur);
        moveStylePanel.add(lblColEnd);
        moveStylePanel.add(cbColEnd);
        cbColEnd.setEnabled(false);

        lblInfMoveX = new JLabel("Infinite Horizontal Motion: ");
        lblInfMoveY = new JLabel("Infinite Vertical Motion: ");
        cbInfMoveX = new JComboBox(bools);
        cbInfMoveY = new JComboBox(bools);
        moveStylePanel.add(lblInfMoveX);
        moveStylePanel.add(cbInfMoveX);
        moveStylePanel.add(lblInfMoveY);
        moveStylePanel.add(cbInfMoveY);



        rightPanel.add(moveStylePanel);
    }

    @Override
    protected void setupFooterPanel() {
        btnAccept = MyUtils.buttonFactory("Accept", "accept", ConfigHandler.defaultFont, new AcceptBtnListener());
        footerPanel.add(btnAccept);
    }

    class AcceptBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            GUIMaster.setCurrentSlate(returnTo);
        }
    }

    class AddPieceButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    class RemovePieceButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    class AddMSButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    private class RemoveMSButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    class PiecesListCellRenderer extends DefaultListCellRenderer {
        int lastSelIndex = -1;
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(isSelected) {
                if(lastSelIndex != index) {
                    lastSelIndex = index;
                    setSelectedPieceIndex(index);
                }
            }
            return this;
        }
    }

    class MoveStylesListCellRenderer extends DefaultListCellRenderer {
        int lastSelIndex = -1;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(isSelected) {
                if(lastSelIndex != index) {
                    lastSelIndex = index; //todo might need to not always change the last sel index.
                    logLine("Selected index = "+index, 0);
                    setSelectedMoveStyleFromIndex(index);
                }
            }
            return this;
        }
    }
}
