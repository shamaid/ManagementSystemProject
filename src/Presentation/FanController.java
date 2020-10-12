package Presentation;

import Presentation.Records.GameRecord;
import Presentation.Records.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.HashMap;

public class FanController extends GeneralController{

    private HBox mainView1;
    private String loggedUser;
    private Client client;

    private GridPane mainPane;

    public FanController(HBox mainView1, String loggedUser, Client client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = client;
        this.mainPane = mainPane;
    }

    public void followGames(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        List<String> games = client.sendToServer("getAllFutureGames|"+loggedUser);
        int rowIdx=0;
        Label label = new Label("Please select games to follow:");
        mainPane.add(label,0,rowIdx);
        rowIdx++;
        Label l_games = new Label("Games:");
        Label l_selectedGames = new Label("Selected Games:");
        mainPane.add(l_games, 0, rowIdx);
        mainPane.add(l_selectedGames, 1, rowIdx);
        rowIdx++;

        ObservableList<String> ol_games = FXCollections.observableArrayList();
        HashMap<String, Record> gamesMap = new HashMap<>();

        for(String game: games){
            if(game.length()>0){
                GameRecord record = new GameRecord(game);
                ol_games.add(record.getName() + record.getFollows());
                gamesMap.put(record.getId(), record);
            }
        }



        ListView<String> lv_games = new ListView<>(ol_games);
        ListView<String> lv_selectedGames = new ListView<>();
        linkSelectionLists(lv_games, lv_selectedGames);

        mainPane.add(lv_games, 0, rowIdx);
        mainPane.add(lv_selectedGames, 1, rowIdx);
        rowIdx++;
        CheckBox email = new CheckBox("send notification to your Email");
        mainPane.add(email, 0, rowIdx);
        rowIdx++;
        Button followBtn = new Button("Follow");
        followBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<String> selected = lv_selectedGames.getItems();
                if(selected.size()>0){
                    String cb_email;
                    if(email.isSelected())
                        cb_email = "true";
                    else
                        cb_email= "false";
                    String strGames = client.ListToString(getStringsIds(lv_selectedGames.getItems(), gamesMap));
                    List<String> receive = client.sendToServer("followGames|"+loggedUser+"|"+strGames+"|"+cb_email);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
                else
                    showAlert("Action canceled - No game was selected", Alert.AlertType.ERROR);
            }
        });
        mainPane.add(followBtn, 0, rowIdx);
        mainView1.getChildren().add(mainPane);
    }

    public void submitComplaint(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("Submit A Complaint");
        mainPane.add(label, 0, 0);
        TextArea ta_complaint = new TextArea("type your complaint here...");
        mainPane.add(ta_complaint, 0, 1);
        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = ta_complaint.getText();
                if(!text.equals("type your complaint here...")){
                    List<String> receive = client.sendToServer("addComplaint|"+loggedUser+"|"+text);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
            }
        });
        mainPane.add(submitBtn, 0, 2);
        mainView1.getChildren().add(mainPane);
    }
}
