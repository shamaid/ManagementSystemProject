package Presentation;

import Presentation.Records.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class OwnershipController extends GeneralController{

    private HBox mainView1;
    private String loggedUser;
    private Client m_client;
    private HashMap<String,String> teams; //<id , name>
    private GridPane mainPane;



    public OwnershipController(HBox mainView1, String loggedUser, Client m_client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.m_client = m_client;
        this.mainPane = mainPane;
        this.teams = initHashSet(m_client.sendToServer("getTeams|"+loggedUser));
    }



    public void openNewTeam(){
        int rowCount=0;
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("Please fill out this form: ");
        mainPane.add(label,0,rowCount);
        rowCount++;
        Label teamName = new Label("Team Name: ");
        TextField tf_teamName = new TextField();
        mainPane.add(teamName,0,rowCount);
        mainPane.add(tf_teamName,1,rowCount);
        rowCount++;

        List<String> players = m_client.sendToServer("viewInformationAboutPlayers|");
        List<String> coaches = m_client.sendToServer("viewInformationAboutCoaches|"+loggedUser);
        List<String> fields = m_client.sendToServer("getAllFields|"+loggedUser);

        HashMap<String, Record> playersMap = new HashMap<>();
        ObservableList<String> playerRecordArrayList = FXCollections.observableArrayList();
        for(String player: players){
            if(player.length()>0){
                PlayerRecord record = new PlayerRecord(player);
                playersMap.put(record.getId(), record);
                playerRecordArrayList.add(record.getName());
            }
        }
        ListView<String> lv_players = new ListView<>(playerRecordArrayList);
        ListView<String> lv_selectedPlayers = new ListView<>();
        linkSelectionLists(lv_players,lv_selectedPlayers);

        mainPane.add(new Label("Players - at least 11: "),0,rowCount);
        mainPane.add(new Label("Selected Players: "),1,rowCount);
        rowCount++;
        addToPane(mainPane, lv_players, lv_selectedPlayers, 0, rowCount);
        rowCount++;



        HashMap<String, Record> coachesMap = new HashMap<>();
        ObservableList<String> coachRecordArrayList = FXCollections.observableArrayList();
        for(String coach: coaches){
            if(coach.length()>0){
                CoachRecord record = new CoachRecord(coach);
                coachesMap.put(record.getId(), record);
                coachRecordArrayList.add(record.getName());
            }
        }
        ListView<String> lv_coaches = new ListView<>(coachRecordArrayList);
        ListView<String> lv_selectedCoaches = new ListView<>();
        linkSelectionLists(lv_coaches,lv_selectedCoaches );
        mainPane.add(new Label("Coaches - at least 1: "),0,rowCount);
        mainPane.add(new Label("Selected Coaches: "),1,rowCount);
        rowCount++;
        addToPane(mainPane, lv_coaches,lv_selectedCoaches,0,rowCount);
        rowCount++;


        HashMap<String, Record> fieldsMap = new HashMap<>();
        ObservableList<String> fieldRecordArrayList = FXCollections.observableArrayList();
        for(String field: fields){
            if(field.length()>0){
                FieldRecord record = new FieldRecord(field);
                fieldsMap.put(record.getId(), record);
                fieldRecordArrayList.add(record.getName());
            }
        }
        ListView<String> lv_fields = new ListView<>(fieldRecordArrayList);
        ListView<String> lv_selectedFields = new ListView<>();
        linkSelectionLists(lv_fields,lv_selectedFields );
        mainPane.add(new Label("Fields - at least 1: "),0,rowCount);
        mainPane.add(new Label("Selected Fields: "),1,rowCount);
        rowCount++;
        addToPane(mainPane, lv_fields,lv_selectedFields,0,rowCount);
        rowCount++;



        Button addBtn = new Button("Add Team");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String name = tf_teamName.getText();
                if(Checker.isValid(name)&&lv_selectedPlayers.getItems().size()>10&&lv_selectedCoaches.getItems().size()>0&&lv_selectedFields.getItems().size()>0){
                    String players = m_client.ListToString(getStringsIds(lv_selectedPlayers.getItems(), playersMap));
                    String coaches = m_client.ListToString(getStringsIds(lv_selectedCoaches.getItems(), coachesMap));
                    String fields = m_client.ListToString(getStringsIds(lv_selectedFields.getItems(), fieldsMap));
                    List<String> receive = m_client.sendToServer("createTeam|"+loggedUser+"|"+name+"|"+players+"|"+coaches+"|"+fields);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                    if(receive.get(0).contains("Succeed"))
                        FootballSpellChecker.addWord(name);
                    clearMainView(mainView1);
                    clearMainView(mainPane);
                }
                else
                    showAlert("invalid arguments - please select at least 11 players one coach and one field!", Alert.AlertType.ERROR);


            }
        });
        mainPane.add(addBtn,1,rowCount);
        mainView1.getChildren().add(mainPane);
    }

    public void appointTeamOwner(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        appoint("owner");
    }


    public void appointTeamManager(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        appoint("manager");

    }


    public void closeTeam()
    {
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("Please select team to close");
        mainPane.add(label,0,0);
        this.teams = initHashSet(m_client.sendToServer("getTeams|"+loggedUser));
        cb_teams = addTeamsChoiceBox(mainPane, 1, teams.values());
        Button addBtn = new Button("Close");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String teamName = cb_teams.getValue() , id = getIdFromName(teamName, teams);
                List<String> receive =  m_client.sendToServer("closeTeam|"+loggedUser+"|"+id);
                showAlert(receive.get(0), Alert.AlertType.INFORMATION);
            }
        });
        mainPane.add(addBtn,0,2);
        mainView1.getChildren().add(mainPane);
    }
    public void reopenTeam(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        //getAllClosedTeams()
        //let user select team
        //send request to reopen
    }



    private void addToPane(GridPane pane,Node object ,Node selected, int col, int row){
        pane.add(object ,col,row);
        pane.add(selected ,col+1,row);
    }

    private ChoiceBox<String> cb_teams;
    private ChoiceBox<String> cb_users;


    private void addUsersChoiceBox(GridPane pane, int rowIdx) {
        List<String> users = m_client.sendToServer("getAllUsers_Team|"+loggedUser);
        List<UserRecord> userRecords = new ArrayList<>();
        ObservableList<String> ol_users = FXCollections.observableArrayList();
        for(String user:users){
            UserRecord record = new UserRecord(user);
            ol_users.add(record.getName());
            userRecords.add(record);
        }
        Label l_users = new Label("Users: ");
        pane.add(l_users,0,rowIdx);
        cb_users = new ChoiceBox(ol_users);
        pane.add(cb_users,1,rowIdx);
    }

    private void appoint(String type) {
        int rowIdx= 0;
        StringBuilder request = new StringBuilder();

        Label label = new Label("");
        switch (type){
            case "manager":{
                label = new Label("please select team and user to appoint as team manager");
                request.append("appointTeamManager|");
                break;
            }
            case "owner":{
                label = new Label("please select team and user to appoint as team owner");
                request.append("appointTeamOwner|");
                break;
            }
        }
        mainPane.add(label,0,rowIdx);
        rowIdx++;
        this.teams = initHashSet(m_client.sendToServer("getTeams|"+loggedUser));
        cb_teams = addTeamsChoiceBox(mainPane, rowIdx, teams.values());
        rowIdx++;
        addUsersChoiceBox(mainPane, rowIdx);
        rowIdx++;
        Button appointBtn = new Button("Appoint");
        appointBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String team=cb_teams.getValue(), user=cb_users.getValue() , id = getIdFromName(team, teams);
                if(Checker.isValid(team)&&Checker.isValid(user)){
                    request.append(loggedUser+"|"+user+"|"+id);
                    List<String> receive = m_client.sendToServer(request.toString());
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
                else{
                    showAlert("Invalid values selection!", Alert.AlertType.ERROR);
                }
            }
        });
        mainPane.add(appointBtn,0,rowIdx);
        mainView1.getChildren().add(mainPane);
    }
}
