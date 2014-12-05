package chess.gui.slates;

import chess.automata.AIActor;
import chess.config.ConfigMaster;
import chess.game.objects.GameMode;
import chess.game.objects.Player;
import chess.game.objects.Team;
import chess.general.Common;
import chess.gui.metroui.*;
import chess.gui.objects.AbstractSlate;
import chess.master.Runner;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * This Slate is the pre-game menu. Players choose game mode
 * and input player names etc.
 *
 * Created by Fez on 11/24/14.
 */
public class PreGameSlate extends AbstractSlate {

    private MetroButton buttonStartGame, buttonBack;
    Vector<MetroLabelledTextField[]> playerNames;
    Vector<MetroCheckBox> computerCheckBoxes;
    Vector<JLabel> teamNames;
    MetroList gameModeList;
    Vector<JSONObject> gameModes;

    int indexSelectedGame = -1;

    public PreGameSlate(AbstractSlate _returnTo) {
        super("PreGameSlate", _returnTo);
        gameModes = Runner.gameCollection.getAllJSONObjects();
        playerNames = new Vector<MetroLabelledTextField[]>();
        teamNames = new Vector<JLabel>();
        computerCheckBoxes = new Vector<MetroCheckBox>();
        panelSetup();
    }

    @Override
    protected void setupHeaderPanel() {
        JLabel label = new JLabel("Setup your game");
        label.setFont(ConfigMaster.titleFont);
        label.setForeground(Color.white);
    }

    @Override
    protected void setupLeftPanel() {
        gameModeList = new MetroList();
        gameModeList.setRequiredDimension(
                new Dimension(
                        AbstractSlate.sideWidth,
                        AbstractSlate.centerWidth - 10
                )
        );
        for(JSONObject object : gameModes) {
            gameModeList.append(object.getString("name"));
        }
        gameModeList.setCellRenderer(new GameModeListRenderer());
        gameModeList.setSelectedIndex(0);
        leftPanel.add(gameModeList.getCanvas());
    }

    @Override
    protected void setupCenterPanel() {
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    }

    @Override
    protected void setupRightPanel() {

    }

    @Override
    protected void setupFooterPanel() {
        buttonStartGame = new MetroButton("Start Game");
        Dimension dim = new Dimension(200, 70);
        buttonStartGame.setRequiredDimension(dim);
        buttonBack = new MetroButton("Back");
        buttonBack.setRequiredDimension(dim);
        footerPanel.add(buttonBack.getCanvas());
        footerPanel.add(buttonStartGame.getCanvas());
        buttonStartGame.addActionListener(new StartGameButtonListener());
    }

    private MetroLabelledTextField[] setupTeamPanel(final int numPlayers, final MetroPanel mPanel) {
        JPanel panel = mPanel.getCanvas();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        MetroLabelledTextField[] toRet = new MetroLabelledTextField[numPlayers];
        Dimension dim = new Dimension(AbstractSlate.centerWidth, 50);
        for(int i = 0 ; i < toRet.length; ++i) {
            toRet[i] = new MetroLabelledTextField(
                   "Player's Name: ",
                    "e.g. "+getName(),
                    dim
            );
            panel.add(toRet[i].getCanvas());
        }
        mPanel.setRequiredDimension(new Dimension(AbstractSlate.centerWidth-20, 50*toRet.length));
        panel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white));
        return toRet;
    }

    private void setSelectedGame(int index) {
        logLine("Selected index = " + index, 0);
        playerNames.clear();
        teamNames.clear();
        computerCheckBoxes.clear();
        centerPanel.removeAll();
        JSONObject gameMode = gameModes.get(index);
        JSONArray teamsArray = gameMode.getJSONArray("teams");
        MetroPanel[] teamPanel = new MetroPanel[teamsArray.length()];
        for(int i = 0; i < teamPanel.length; ++i) {
            JSONObject team = teamsArray.getJSONObject(i);
            teamPanel[i] = new MetroPanel(team.getString("teamName"));
            playerNames.add(setupTeamPanel(team.getInt("numberOfPlayers"), teamPanel[i]));
            JLabel teamName = new JLabel(team.getString("teamName"));
            teamName.setFont(ConfigMaster.headerThreeFont);
            teamName.setForeground(Color.white);
            teamName.setAlignmentX(0.5f);
            teamName.setPreferredSize(new Dimension(AbstractSlate.centerWidth, 40));
            MetroCheckBox isComputer = new MetroCheckBox("IsAI"+team.getString("teamName"));
            computerCheckBoxes.add(isComputer);
            MetroPanel checkBoxPanel = new MetroPanel("CheckBoxPanel"+i);
            JPanel somePanel = checkBoxPanel.getCanvas();
            JLabel lblIsAI = Common.labelFactory("Is Computer?");
            somePanel.add(lblIsAI);
            somePanel.add(isComputer.getCanvas());
            teamNames.add(teamName);
            centerPanel.add(teamName);
            centerPanel.add(teamPanel[i].getCanvas());
            centerPanel.add(somePanel);
            centerPanel.add(Box.createVerticalGlue());
        }
        centerPanel.updateUI();
    }

    class GameModeListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if(isSelected) {
                if(indexSelectedGame != index) {
                    indexSelectedGame = index;
                    setSelectedGame(index);
                }
            }
            return this;
        }
    }

    class StartGameButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Team[] teams = new Team[playerNames.size()];
            JSONObject chosenGameMode = gameModes.get(indexSelectedGame);
            boolean allComputer = true;
            for(MetroCheckBox mcb : computerCheckBoxes) {
                if(!mcb.isChecked()) allComputer = false;
            }

//            if(allComputer) {
//                JOptionPane.showMessageDialog(null, "You cannot have all players being computers!");
//                return;
//            }

            for(int i = 0; i < teams.length; ++i) {
                teams[i] = new Team( teamNames.get(i).getText());
                for(MetroLabelledTextField textField : playerNames.get(i)) {
                    if(computerCheckBoxes.get(i).isChecked()) {
                        teams[i].addPlayer(new AIActor(textField.getText().replace("e.g. ", "")));
                    } else {
                        teams[i].addPlayer(new Player(textField.getText().replace("e.g. ", "")));
                    }
                }
            }
            // finished creating teams.
            JSONObject layout = chosenGameMode.getJSONObject("layout");
            String[][] startingLayout = new String[8][8];
            JSONArray rows = layout.getJSONArray("rows");
            for(int i = 0; i < rows.length(); ++i) {
                JSONArray row = rows.getJSONArray(i);
                for(int j = 0; j < row.length(); ++j) {
                    startingLayout[i][j] = row.getString(j);
                }
            }
            // finished getting starting Layout


            //////////////////////////////////////////////////////////
            // Turn order will be stored as tuples:
            // "0,0","1,0" means team 0's player 0 will go first
            // then team 1's player 0 will go next.
            // Stored as array of Strings.
            //////////////////////////////////////////////////////////
            JSONArray turnOrder = chosenGameMode.getJSONArray("turnOrder");
            Player[] playerOrder = new Player[turnOrder.length()];
            for(int i = 0; i < turnOrder.length(); ++i) {
                String[] tuple = turnOrder.getString(i).split(",");
                playerOrder[i] = teams[Integer.parseInt(tuple[0])].getPlayer(Integer.parseInt(tuple[1]));
            }
            // finished creating turn order

            int maxTime = chosenGameMode.getInt("maxTime");

            Vector<String> objectives = new Vector<String>();
            JSONArray objs = chosenGameMode.getJSONArray("objectives");
            for(int i = 0; i < objs.length(); ++i) {
                objectives.add(objs.getString(i));
            }

            GameMode gameMode = new GameMode(
                    chosenGameMode.getString("name"),
                    startingLayout,
                    playerOrder,
                    maxTime,
                    objectives
            );

            Runner.guiMaster.setCurrentSlate(new PlayGameSlate(PreGameSlate.this, gameMode));
        }
    }
}
