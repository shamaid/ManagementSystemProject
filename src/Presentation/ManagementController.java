package Presentation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.HashMap;


public class ManagementController extends GeneralController{

    private HBox mainView1;
    private String loggedUser;
    private Client client;
    private HashMap<String,String> teams; //<id , name>
    private GridPane mainPane;


    public ManagementController(HBox mainView1, String loggedUser, Client client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = client;
        this.teams = initHashSet(client.sendToServer("getTeams|"+loggedUser));
        this.mainPane = mainPane;
    }

    private ChoiceBox<String> cb_teams;

    public void addPlayerToTeam(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        //show all authorized teams
        //show all players
        //let user select team and player

        String request = "addAssetPlayer|";
    }
    public void addCoachToTeam()
    {
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        //show all authorized teams
        //show all coaches
        //let user select team and coach
    }
    public void addFieldToTeam(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        //show all authorized teams
        //show all fields
        //let user select team and field
    }
    public void updateAsset(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        //show all authorized teams
        //let user select team
        //show list of assets of team
        //let user select what asset to update
        //update user

    }
    public void reportIncome(){
        clearMainView(mainView1);
        report("income");

    }
    public void reportExpanse(){
        clearMainView(mainView1);
        report("expanse");

    }
    public void getBalance(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        rowIdx++;
        Button getBalanceBtn = new Button("Get Balance");
        getBalanceBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String teamName = cb_teams.getValue(), id = getIdFromName(teamName, teams);
                String request = "getBalance|"+loggedUser+"|"+id;
                List<String> receive = client.sendToServer(request);
                showAlert(receive.get(0), Alert.AlertType.INFORMATION);
            }
        });
        mainPane.add(getBalanceBtn, 0, rowIdx);
        mainView1.getChildren().add(mainPane);
    }

    private void report(String type){
        clearMainView(mainPane);
        int rowIdx =0;
        teamSelection(rowIdx);
        rowIdx++;
        rowIdx++;
        Label l_type = null;
        StringBuilder request = new StringBuilder();
        switch (type){
            case"income":{
                l_type = new Label("Income :");
                request.append("reportIncome|");
                break;
            }
            case"expanse":{
                l_type = new Label("Expanse :");
                request.append("reportExpanse|");
                break;
            }
        }
        TextField tf_input = new TextField();
        mainPane.add(l_type, 0, rowIdx);
        mainPane.add(tf_input, 1, rowIdx);
        rowIdx++;
        Button reportBtn = new Button("Report");
        reportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = tf_input.getText();
                String teamName = cb_teams.getValue();
                if(Checker.isValidNumber(input)&& Checker.isValid(teamName)){
                    String id = getIdFromName(teamName, teams);
                    request.append(loggedUser+"|"+id+"|"+input);
                    List<String> receive = client.sendToServer(request.toString());
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
                else{
                    showAlert("Invalid team or input - enter numbers only!", Alert.AlertType.ERROR);
                }
            }
        });
        mainPane.add(reportBtn, 0, rowIdx);
        mainView1.getChildren().add(mainPane);
    }

    private void teamSelection(int index){
        Label label = new Label("Please select team:");
        mainPane.add(label, 0, index);
        index++;
        cb_teams = addTeamsChoiceBox(mainPane, index, teams.values());
    }

}
