package chess.gui.slates;

import chess.game.objects.MoveStyle;
import chess.game.objects.Piece;
import chess.general.Common;
import chess.gui.metroui.MetroButton;
import chess.gui.metroui.MetroLabelledTextField;
import chess.gui.metroui.MetroList;
import chess.gui.metroui.MetroPanel;
import chess.gui.objects.AbstractSlate;
import chess.master.Runner;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Fez on 11/14/14.
 */
public class PieceBuilderSlate extends AbstractSlate {

    private MetroList piecesList, movesList;
    private MetroLabelledTextField lblTFPieceName, lblTFImgPath;
    private Vector<JSONObject> jsonPieces, jsonMoveStyles;
    private MetroButton mbtnSaveMSChanges, mbtnResetMS;
    private JComboBox cbDX, cbDY, cbFirstMove, cbColDur, cbColEnd, cbMoveObjective;
    private JLabel lblDX, lblDY, lblFirstMove, lblColDur, lblColEnd, lblMoveObjective;
    private HashMap<JSONObject, String> objToKey;

    private int indexSelectedPiece = -1, indexSelectedMovestyle = -1;
    //starting off at -1 because if zero no difference will be noticed and nothing will be rendered
    private JPanel moveStylePanel;
    private JComboBox cbDistance;
    private JLabel lblDistance;

    public PieceBuilderSlate(AbstractSlate _returnTo) {
        super("PieceBuilderSlate", _returnTo);

        jsonPieces = Runner.pieceCollection.getAllJSONObjects();
        objToKey = new HashMap<JSONObject, String>();
        for (JSONObject p : jsonPieces) {
            objToKey.put(p, p.getString("name"));
        }

        panelSetup();
    }

    @Override
    protected void setupHeaderPanel() {

    }

    @Override
    protected void setupLeftPanel() {
        piecesList = new MetroList();


        piecesList.setRequiredDimension(new Dimension(AbstractSlate.sideWidth-3, AbstractSlate.centerWidth-5));
        piecesList.setCellRenderer(new PiecesListCellRenderer());
        for (JSONObject o : jsonPieces) {
            piecesList.append(o.getString("name"));
        }

        MetroButton btnAddPiece = new MetroButton("Add Piece");
        btnAddPiece.addActionListener(new AddPieceButtonListener());
        piecesList.addButton(btnAddPiece);
        MetroButton btnRemovePiece = new MetroButton("Remove Piece");
        btnRemovePiece.addActionListener(new RemovePieceButtonListener());
        piecesList.addButton(btnRemovePiece);

        leftPanel.add(piecesList.getCanvas());

    }

    private JSONObject getJSONFromList(String name) {
        for (JSONObject j : jsonPieces) {
            if (j.has("name")) {
                if (j.getString("name").equals(name)) return j;
            }
        }
        return null;
    }

   private void setSelectedPiece(JSONObject piece) {
        jsonMoveStyles = new Vector<JSONObject>();
        lblTFPieceName.setText(piece.getString("name"));
        lblTFImgPath.setText(piece.getString("imagePath"));
        movesList.clear();
        JSONArray _ms = piece.getJSONArray("moveStyles");
        for (int i = 0; i < _ms.length(); ++i) {
            jsonMoveStyles.add((JSONObject) _ms.get(i));
            movesList.append("Movestyle " + (i + 1));
        }
    }

   private void setSelectedPieceIndex(int index) {
       setSelectedPiece(getJSONFromList(piecesList.getElementAt(index)));
       movesList.setSelectedIndex(0);
       setSelectedMoveStyleFromIndex(0);

   }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        Dimension someDim = new Dimension(AbstractSlate.centerWidth, 60);
        lblTFPieceName =
                new MetroLabelledTextField("Piece Name: ", "e.g. White Bishop",
                        someDim);

        lblTFImgPath =
                new MetroLabelledTextField(" Image Path: ", "e.g. /path/to/image.png",
                        someDim);
        lblTFImgPath.setEditable(false);

        MetroPanel mPnlInfo = new MetroPanel("Info Panel");
        mPnlInfo.setRequiredDimension(new Dimension(AbstractSlate.centerWidth, 120));

        JPanel pnlInfo = mPnlInfo.getCanvas();
        pnlInfo.add(lblTFPieceName.getCanvas());
        pnlInfo.add(lblTFImgPath.getCanvas());


        MetroPanel mPnlMiddle = new MetroPanel("Middle Panel");
        mPnlMiddle.setRequiredDimension(new Dimension(
                AbstractSlate.centerWidth,
                AbstractSlate.centerWidth - 160)
        );
        JPanel pnlMiddle = mPnlMiddle.getCanvas();

        MetroPanel mPnlLower = new MetroPanel("Lower Panel");
        mPnlLower.setRequiredDimension(new Dimension(AbstractSlate.centerWidth, 40));
        JPanel pnlLower = mPnlLower.getCanvas();

        MetroButton mbtnAcceptPiece = new MetroButton("Accept Piece");
        mbtnAcceptPiece.addActionListener(new AcceptPieceBtnListener());
        pnlLower.add(mbtnAcceptPiece.getCanvas());
        MetroButton mbtnResetPiece = new MetroButton("Reset Piece");
        mbtnResetPiece.addActionListener(new ResetPieceBtnListener());
        pnlLower.add(mbtnResetPiece.getCanvas());

        centerPanel.add(pnlInfo);
        centerPanel.add(pnlMiddle);
        centerPanel.add(pnlLower);
    }

    private String getMoveStyleAsStringUgly(String s) {
        if (s.equals("Move Only"))
            return "MOVE_ONLY";
        if (s.equals("Kill Only"))
            return "KILL_ONLY";
        if (s.equals("Both"))
            return "BOTH";
        return "BOTH";
    }

    private String getMoveStyleAsStringPretty(String s) {
        if (s.equals("MOVE_ONLY"))
            return "Move Only";
        if (s.equals("KILL_ONLY"))
            return "Kill Only";
        if (s.equals("BOTH"))
            return "Both";
        return "Both";
    }

    private void setSelectedMoveStyle(JSONObject ms) {
        if (ms == null) return;
        cbDX.setSelectedItem("" + ms.getInt("dx"));
        cbDY.setSelectedItem("" + ms.getInt("dy"));
        cbColDur.setSelectedItem("" + ms.getBoolean("collidesDuring"));
        cbColEnd.setSelectedItem("" + ms.getBoolean("collidesAtEnd"));
        cbFirstMove.setSelectedItem("" + ms.getBoolean("firstMoveOnly"));
        cbMoveObjective.setSelectedItem(getMoveStyleAsStringPretty(ms.getString("moveObjective")));
        cbDistance.setSelectedItem("" + ms.getInt("distance"));
    }

    private JSONObject getSelectedMoveStyleAsJSON() {
        int dx = Integer.parseInt((String) cbDX.getSelectedItem());
        int dy = Integer.parseInt((String) cbDY.getSelectedItem());
        int moveDistance = Integer.parseInt((String) cbDistance.getSelectedItem());
        boolean collidesDuring = Boolean.parseBoolean((String) cbColDur.getSelectedItem());
        boolean collidesAtEnd = Boolean.parseBoolean((String) cbColEnd.getSelectedItem());
        boolean firstMove = Boolean.parseBoolean((String) cbFirstMove.getSelectedItem());
        MoveStyle.MoveObjective moveObjective = MoveStyle.MoveObjective.getFromName(
                getMoveStyleAsStringUgly((String) cbMoveObjective.getSelectedItem())
        );
        MoveStyle msTemp = new MoveStyle(
                dx, dy, collidesDuring, collidesAtEnd,
                moveObjective,
                firstMove, moveDistance
        );
        return msTemp.getAsJSONObject();
    }

    private void setSelectedMoveStyleFromIndex(int index) {
        setSelectedMoveStyle(getMSJSONFromList(index));
    }

    private JSONObject getMSJSONFromList(int i) {
        if (i >= jsonMoveStyles.size()) return null;
        return jsonMoveStyles.get(i);
    }

    @Override
    protected void setupRightPanel() {
        movesList = new MetroList();
        movesList.setCellRenderer(new MoveStylesListCellRenderer());
        movesList.setRequiredDimension(new Dimension(AbstractSlate.sideWidth - 3, AbstractSlate.centerWidth / 4));

        MetroButton btnAddMS = new MetroButton("Add Movestyle");
        btnAddMS.addActionListener(new AddMSButtonListener());
        movesList.addButton(btnAddMS);
        MetroButton btnRemoveMS = new MetroButton("Remove Movestyle");
        btnRemoveMS.addActionListener(new RemoveMSButtonListener());
        movesList.addButton(btnRemoveMS);


        rightPanel.add(movesList.getCanvas());

        String[] nums = new String[15];
        for (int i = -7; i < 8; ++i) {
            nums[i + 7] = "" + i;
        }

        String[] distanceNums = new String[9];
        for(int i = -1; i < 8; ++i) {
            distanceNums[i + 1] = ""+i;
        }

        String[] bools = {"true", "false"};
        String[] objectives = {"Move Only", "Kill Only", "Both"};
        MetroPanel moveStyleMPanel = new MetroPanel("Move Style Options Panel");
        moveStyleMPanel.setRequiredDimension(new Dimension(AbstractSlate.sideWidth, 3 * AbstractSlate.centerWidth / 4));
        moveStylePanel = moveStyleMPanel.getCanvas();
        moveStylePanel.setLayout(new BoxLayout(moveStylePanel, BoxLayout.Y_AXIS));
        lblDX = Common.labelFactory("Horizontal Motion Eigenvector");
        lblDY = Common.labelFactory("Vertical Motion Eigenvector");
        cbDX = new JComboBox(nums);
        cbDY = new JComboBox(nums);
        moveStylePanel.add(lblDX);
        moveStylePanel.add(cbDX);
        moveStylePanel.add(lblDY);
        moveStylePanel.add(cbDY);
        lblDX.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDY.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDistance = Common.labelFactory("Max Movement Distance (Manhattan)");
        cbDistance = new JComboBox(distanceNums);
        moveStylePanel.add(lblDistance);
        moveStylePanel.add(cbDistance);
        lblDistance.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblColDur = Common.labelFactory("Collides During Motion");
        lblColEnd = Common.labelFactory("Collides at End of Motion");
        cbColDur = new JComboBox(bools);
        cbColEnd = new JComboBox(bools);

        moveStylePanel.add(lblColDur);
        moveStylePanel.add(cbColDur);
        moveStylePanel.add(lblColEnd);
        moveStylePanel.add(cbColEnd);
        lblColDur.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblColEnd.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbColEnd.setEnabled(false);

        lblFirstMove = Common.labelFactory("First Move Only");
        cbFirstMove = new JComboBox(bools);

        moveStylePanel.add(lblFirstMove);
        moveStylePanel.add(cbFirstMove);
        lblFirstMove.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMoveObjective = Common.labelFactory("Move Objective");
        cbMoveObjective = new JComboBox(objectives);
        moveStylePanel.add(lblMoveObjective);
        moveStylePanel.add(cbMoveObjective);
        lblMoveObjective.setAlignmentX(Component.CENTER_ALIGNMENT);

        MetroPanel saveMPanel = new MetroPanel("Save Movestyle Panel");
        JPanel testPnl = saveMPanel.getCanvas();
        testPnl.setPreferredSize(new Dimension(AbstractSlate.sideWidth, 40));

        mbtnResetMS = new MetroButton("Reset Movestyle");
        mbtnResetMS.addActionListener(new ResetMSButtonListener());
        mbtnSaveMSChanges = new MetroButton("Save Movestyle");
        mbtnSaveMSChanges.addActionListener(new SaveMSButtonListener());
        testPnl.add(mbtnSaveMSChanges.getCanvas());
        testPnl.add(mbtnResetMS.getCanvas());
        moveStylePanel.add(testPnl);

        rightPanel.add(moveStylePanel);
    }

    @Override
    protected void setupFooterPanel() {
        MetroButton mbtnAccept = new MetroButton("Accept All Changes");
        mbtnAccept.addActionListener(new AcceptBtnListener());
        footerPanel.add(mbtnAccept.getCanvas());
    }

    class AcceptBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (JSONObject o : jsonPieces) {
                Runner.pieceCollection.updateJSONObject(objToKey.get(o), o);
            }
            returnToPreviousSlate();
        }
    }

    class AddPieceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            piecesList.append("NewPiece" + jsonPieces.size());
            jsonPieces.add(new Piece("NewPiece" + jsonPieces.size(), "../images/NewPiece" + jsonPieces.size() + ".png",
                    new JSONArray()).getPieceAsJSON());
            piecesList.setSelectedIndex(jsonPieces.size() - 1);
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
            if(indexSelectedPiece >= 0) {
                movesList.append("Movestyle " + (jsonMoveStyles.size() + 1));
                jsonMoveStyles.add(new MoveStyle(0, 0, false, true, MoveStyle.MoveObjective.BOTH,
                        false, -1).getAsJSONObject());
                movesList.setSelectedIndex(jsonMoveStyles.size() - 1);
            }
        }
    }

    private class RemoveMSButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(indexSelectedPiece >= 0) {
                jsonMoveStyles.remove(movesList.getSelectedIndex());
                movesList.removeElementAt(movesList.getSelectedIndex());
                indexSelectedMovestyle = -1;
                movesList.setSelectedIndex(0);
            }
        }
    }

    private class SaveMSButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            logLine("Saving MoveStyle", 0);
            JSONObject temp = getSelectedMoveStyleAsJSON();
            jsonMoveStyles.remove(indexSelectedMovestyle);
            jsonMoveStyles.insertElementAt(temp, indexSelectedMovestyle);
            JOptionPane.showMessageDialog(null,
                    "This MoveStyle has been saved, however, the changes will not take place till after you save the piece.");
            setSelectedMoveStyleFromIndex(indexSelectedMovestyle);
            movesList.setSelectedIndex(indexSelectedMovestyle);
        }
    }

    private class ResetMSButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setSelectedMoveStyleFromIndex(indexSelectedMovestyle);
        }
    }

    class PiecesListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                if (indexSelectedPiece != index) {
                    indexSelectedPiece = index;
                    setSelectedPieceIndex(index);
                }
            }
            return this;
        }
    }

    class MoveStylesListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                if (indexSelectedMovestyle != index) {
                    //todo might need to not always change the last sel index.
                    indexSelectedMovestyle = index;
                    setSelectedMoveStyleFromIndex(index);
                }
            }
            return this;
        }
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

    private class AcceptPieceBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Piece p = new Piece(lblTFPieceName.getText(), lblTFImgPath.getText(), new JSONArray());
            for (JSONObject ms : jsonMoveStyles) {
                p.addMoveStyle(ms);
            }
            String key = objToKey.remove(jsonPieces.remove(indexSelectedPiece));
            JSONObject temp = p.getPieceAsJSON();
            jsonPieces.add(indexSelectedPiece, temp);
            objToKey.put(temp, key);
            JOptionPane.showMessageDialog(null, "Saved piece " + p.getPieceName() + "!");
        }

    }

    private class ResetPieceBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setSelectedPieceIndex(indexSelectedPiece);
        }
    }
}
