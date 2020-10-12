package Presentation;

import Presentation.Records.EventRecord;
import Presentation.Records.GameRecord;
import Presentation.Records.PlayerRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.HashMap;


public class RefereeController extends GeneralController{

    private HBox mainView1;
    private String loggedUser;
    private Client client;
    private GridPane mainPane;

    public RefereeController(HBox mainView1, String loggedUser, Client client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = client;
        this.mainPane = mainPane;
    }

    private HashMap<String, PlayerRecord> playersMap;
    private ChoiceBox<String> cb_players;

    public void  addEventToGame(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        String game = getOccurringGame();
        if(game.length()>0){
            int row =0;
            GameRecord gameRecord = new GameRecord(game);
            Label labelGame = new Label(""+gameRecord.getName()+", "+ gameRecord.getDate());
            mainPane.add(labelGame, 0,row);
            row++;
            Label evenType = new Label("Event Type:");
            mainPane.add(evenType, 0,row);
            ChoiceBox<String> cb_types = new ChoiceBox<>(FXCollections.observableArrayList("Goal", "Offside", "Foul", "RedCard", "YellowCard","Injury", "Replacement"));
            mainPane.add(cb_types, 1,row);
            row++;
            String[] split = gameRecord.getName().split("VS");
            cb_players = new ChoiceBox<>(FXCollections.observableArrayList());
            ChoiceBox<String> cb_teams = new ChoiceBox<>(FXCollections.observableArrayList(split[0],split[1].substring(0, split[1].indexOf(","))));
            cb_teams.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    List<String> teamAssets;
                    if(gameRecord.getTeam1Name().equals(cb_teams.getValue())){
                        teamAssets = client.sendToServer("getAllTeamAssets_R|"+loggedUser+"|"+gameRecord.getTeam1Id());
                    }
                    else{
                        teamAssets = client.sendToServer("getAllTeamAssets_R|"+loggedUser+"|"+gameRecord.getTeam2Id());
                    }
                    playersMap = new HashMap<>();
                    for(String asset : teamAssets){
                        if(!asset.contains("Failed")){
                            if(asset.contains("ID=P")){
                                PlayerRecord record = new PlayerRecord(asset);
                                cb_players.getItems().add(record.getName());
                                playersMap.put(record.getId(), record);
                            }
                        }
                        else
                            cb_players.getItems().add("");
                    }
                }
            });
            Label team = new Label("Team:");
            mainPane.add(team, 0,row);
            mainPane.add(cb_teams, 1,row);
            row++;
            Label player = new Label("Player:");
            mainPane.add(player, 0,row);
            mainPane.add(cb_players, 1,row);
            row++;
            Button addBtn = new Button("Add");
            addBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if(cb_types.getValue().length()>0 && cb_teams.getValue().length()>0 && cb_players.getValue().length()>0){
                        String teamId, playerId = "";
                        if(gameRecord.getTeam1Name().equals(cb_teams.getValue()))
                            teamId = gameRecord.getTeam1Id();

                        else
                            teamId = gameRecord.getTeam2Id();

                        for(String id : playersMap.keySet()){
                            if(cb_players.getValue().equals(playersMap.get(id).getName()))
                                playerId = id;
                        }
                        List<String> receive = client.sendToServer("addEventToGame|"+loggedUser+"|"+gameRecord.getId()+"|"+cb_types.getValue()+"|"+playerId+"|"+teamId);
                        showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                        if(receive.get(0).contains("Succeed")){
                            clearMainView(mainView1);
                            clearMainView(mainPane);
                        }
                    }
                    else
                        showAlert("Invalid arguments! please fill in all fields", Alert.AlertType.ERROR);
                }
            });
            mainPane.add(addBtn,0,row);
            mainView1.getChildren().add(mainPane);
        }


    }

    //main referee only?
    public void  setScoreInGame(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        String game = getOccurringGame();
        if(game.length()>0){
            GameRecord gameRecord = new GameRecord(game);
            Label labelGame = new Label(gameRecord.getName());
            mainPane.add(labelGame, 0,0);
            Label hostScore = new Label("Host Score:");
            mainPane.add(hostScore, 0,1);
            ChoiceBox<String> h_score = new ChoiceBox<>(addScores());
            mainPane.add(h_score, 1,1);

            Label guestScore = new Label("Guest Score:");
            mainPane.add(guestScore, 0,2);
            ChoiceBox<String> g_score = new ChoiceBox<>(addScores());
            mainPane.add(g_score, 1,2);

            Button addBtn = new Button("Add");
            addBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String host =h_score.getValue(),guest = g_score.getValue();
                    if(Checker.isValidNumber(host)&& Checker.isValidNumber(guest)){
                        List<String> receive = client.sendToServer("setScoreInGame|"+loggedUser+"|"+gameRecord.getId()+"|"+host+"|"+guest);
                        showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                    }
                }
            });
            mainPane.add(addBtn, 0,3);
            mainView1.getChildren().add(mainPane);
        }
    }

    public void getGameReport(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        String game = getOccurringGame();
        if(game.length()>0){
            GameRecord gameRecord = new GameRecord(game);
            List<String> receive = client.sendToServer("getGameReport|"+loggedUser+"|"+gameRecord.getId());
            receive.add(gameRecord.getName());
            showListOnScreen("Game Report", receive, mainPane, 2);
            mainView1.getChildren().add(mainPane);
        }

    }

    //main referee only
    private ChoiceBox<String> events;
    public void  changeEvent(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        String game = getOccurringGame();
        if(game.length()>0){
            GameRecord gameRecord = new GameRecord(game);
            Label label = new Label("Please select an event to change:");
            mainPane.add(label, 0,0);
            Label lb_events = new Label("Events:");
            mainPane.add(lb_events, 0,1);
            events = new ChoiceBox<>();

            ObservableList<String> listEvents = FXCollections.observableArrayList();
            HashMap<String, EventRecord> eventsMap = new HashMap<>();
            mainPane.setAlignment(Pos.CENTER);
            List<String> gameReport = client.sendToServer("getGameReport|"+loggedUser+"|"+gameRecord.getId());
            for(String report :gameReport){
                if(report.length()>0){
                    EventRecord record = new EventRecord(report);
                    listEvents.add(record.getType() + " " + record.getName());
                    eventsMap.put(record.getType() + " " + record.getName(), record);

                }
                else{
                    showAlert("Can't get events", Alert.AlertType.ERROR);
                    return;
                }
            }
            events.setItems(listEvents);
            mainPane.add(events, 1,1);
            mainView1.getChildren().add(mainPane);
            events.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clearMainView(mainPane);
                    String value = events.getValue();
                    EventRecord eventRecord = eventsMap.get(value);
                    //changeEvent();
                    showEventDetails(mainPane,eventRecord,gameRecord.getId(),2);
                }
            });
        }
    }

    private void showEventDetails(GridPane pane, EventRecord eventString ,String game, int startRow) {
        //open event form?
        Label l_events = new Label(eventString.getType() + " " + eventString.getName());
        pane.add(l_events, 0, startRow);
        Label description = new Label("Description:");
        TextField tf_description = new TextField();
        pane.add(description,0,startRow+1);
        pane.add(tf_description,1,startRow+1);
        Button change = new Button("Change");
        change.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String request = "changeEvent|"+loggedUser+"|"+game+"|"+eventString.getId()+"|"+tf_description.getText();
                List<String> receive =client.sendToServer(request);
                showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                clearMainView(mainView1);
                clearMainView(mainPane);
            }
        });
        mainPane.add(change, 0 ,startRow+2);
    }

    private ObservableList<String> addScores() {
        ObservableList<String> scores = FXCollections.observableArrayList();
        for (int i = 0; i <11 ; i++) {
            scores.add(""+i);
        }
        return scores;
    }
    private String getOccurringGame() {
        List<String> occurringGame = client.sendToServer("getOccurringGame|"+loggedUser);
        String game = occurringGame.get(0);
        if(game.length()<1) {
            showAlert("Illegal action! - None of your games is occurring now", Alert.AlertType.ERROR);
            return "";
        }
        return game;

    }

}
