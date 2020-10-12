package Presentation;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class AdminController extends GeneralController{

    private HBox mainView1;
    private String loggedUser;
    private Client client;
    private GridPane mainPain;


    public AdminController(HBox mainView1, String loggedUser, Client m_client, GridPane mainPain) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = m_client;
        this.mainPain = mainPain;
    }


    public void closeTeamPermanently(){
        clearMainView(mainView1);
        clearMainView(mainPain);
        //show all teams with selection option maybe choicebox
        String request = "getAllOpenTeams_Admin|"+loggedUser;

        //let user select a team to close
        //sent request to close team
        //return ack - success or failure
    }
    public void addNewPlayer(){
        clearMainView(mainPain);
        //show registration form for player
        buildForm("player", mainView1, client,loggedUser, mainPain);

    }

    public void addNewCoach(){
        clearMainView(mainPain);
        //show registration form for coach
        buildForm("coach", mainView1, client,loggedUser, mainPain);

    }
    public void addNewTeamOwner(){
        clearMainView(mainPain);
        //show registration form for teamOwner
        buildForm("teamOwner", mainView1, client,loggedUser, mainPain);

    }
    public void addNewTeamManager(){
        clearMainView(mainPain);
        //show registration form for teamManager
        buildForm("teamManager", mainView1, client,loggedUser, mainPain);
    }
    public void addNewUnionRepresentative(){
        clearMainView(mainPain);
        //show registration form for representative
        buildForm("representative", mainView1, client,loggedUser, mainPain);

    }
    public void addNewAdmin(){
        clearMainView(mainPain);
        //show registration form for admin
        buildForm("admin", mainView1, client,loggedUser, mainPain);
    }
    public void removeUser(){
        clearMainView(mainPain);
        clearMainView(mainView1);
        String request = "getAllUsers_Admin|"+loggedUser;
        //show all users?? - table view?
        //let user select a user to remove
        //send request to remove user
        //return ack - success or failure
    }
    public void viewLog(){
        clearMainView(mainView1);
        clearMainView(mainPain);
        Label label = new Label("Please select which log to view");
        ChoiceBox<String> cb_logTypes = new ChoiceBox<>(FXCollections.observableArrayList("Events", "Errors","Server"));
        cb_logTypes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String type = cb_logTypes.getValue();
                if(Checker.isValid(type)){
                    List<String> log = client.sendToServer("viewLog|"+loggedUser+"|"+type);
                    ListView<String> lv_logs = new ListView<>(FXCollections.observableArrayList(log));
                    Label l_log = new Label("Log:");
                    mainPain.add(l_log, 0,2);
                    mainPain.add(lv_logs, 0,3);
                }
            }
        });
        mainPain.add(label, 0, 0);
        mainPain.add(cb_logTypes, 0, 1);
        mainView1.getChildren().add(mainPain);
    }
    public void responseToComplaint(){
        clearMainView(mainView1);
        clearMainView(mainPain);
        List<String> activeComplaints = client.sendToServer("getAllActiveComplaints|"+loggedUser);
        //maybe do something about the presentation of this list
        ChoiceBox<String> cb_complaints = new ChoiceBox<>(FXCollections.observableArrayList(activeComplaints));
        Label label = new Label("Please select a complaint:");
        mainPain.add(label, 0,0);
        mainPain.add(cb_complaints, 0,1);
        TextField tf_response = new TextField("write your response here...");
        mainPain.add(tf_response,0,3);
        Button responseBtn = new Button("Response");
        responseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String id = cb_complaints.getValue(), response = tf_response.getText();
                if(Checker.isValid(id)&&!response.equals("write your response here...")){
                    List<String> receive = client.sendToServer("responseToComplaint|"+loggedUser+"|"+id+"|"+response);
                }
            }
        });
        mainPain.add(responseBtn, 0,4);
        //show all active complaints
        //let user select a complaint to response to
        //create text area for the user to write his response
        //send response to server
    }


}
