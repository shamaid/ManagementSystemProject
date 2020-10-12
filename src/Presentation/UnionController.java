package Presentation;

import Presentation.Records.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class UnionController extends GeneralController {

    private HBox mainView1;
    private String loggedUser;
    private Client client;
    private GridPane mainPane;
    private HashMap<String,String> leagues;//<id,name>
    private HashMap<String,String> seasons;//<id,name>
    private HashMap<String,String> leagueInSeasons;//<id,name>

    public UnionController(HBox mainView1, String loggedUser, Client client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = client;
        this.mainPane = mainPane;
        this.leagues = new HashMap<>();
        this.seasons = new HashMap<>();
        this.leagueInSeasons = new HashMap<>();
    }

    public void configureNewLeague()
    {
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label name = new Label("League Name:");
        mainPane.add(name, 0, 0);
        TextField tf_name = new TextField();
        mainPane.add(tf_name, 2, 0);
        Label level = new Label("League Level:");
        mainPane.add(level, 0, 1);
        ChoiceBox<String> cb_level = new ChoiceBox<>();
        mainPane.add(cb_level, 2, 1);
        ObservableList<String> levels = FXCollections.observableArrayList("level1","level2", "level3","level4");
        cb_level.setItems(levels);
        Button b_add = new Button("Add");
        b_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf_name.getText(),level = cb_level.getValue();
                if(Checker.isValid(name)&& level.length()>0){
                    String request = "configureNewLeague|"+loggedUser+"|"+name+"|"+level;
                    List<String> response = client.sendToServer(request);
                    showAlert(response.get(0), Alert.AlertType.INFORMATION);
                    clearMainView(mainView1);
                }
                else
                    showAlert("Invalid name or level", Alert.AlertType.ERROR);
            }
        });
        mainPane.add(b_add, 2,2);
        mainView1.getChildren().add(mainPane);
    }
    public void configureNewSeason(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("Please select year and start date:");
        mainPane.add(label, 0,0);
        Label year = new Label("Season Year:");
        mainPane.add(year, 0,1);
        Label date = new Label("Season Start Date:");
        mainPane.add(date, 0,2);
        TextField tf_year = new TextField();
        mainPane.add(tf_year, 1,1);
        DatePicker dp_start = new DatePicker();
        mainPane.add(dp_start, 1,2);
        Button addBtn = new Button("Add");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String year = tf_year.getText(), date = dp_start.getValue().toString().replace("-", ".");
                if(Checker.isValidNumber(year) && date.length()>0){
                    String request = "configureNewSeason|"+loggedUser+"|"+year+"|"+date;
                    List<String> receive = client.sendToServer(request);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                    clearMainView(mainView1);
                }
            }
        });
        mainPane.add(addBtn, 1,3);
        mainView1.getChildren().add(mainPane);
    }
    public void configureLeagueInSeason(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        int rowInx=0;
        Label label = new Label("Please select League and Season");
        mainPane.add(label, 0,rowInx);
        rowInx++;
        Label l_leagues = new Label("Leagues:");
        mainPane.add(l_leagues, 0,rowInx);
        List<String> c_leagues = client.sendToServer("viewInformationAboutLeagues|");
        fillLeaguesMap(c_leagues);
        ChoiceBox<String> cb_leagues = new ChoiceBox<>(FXCollections.observableArrayList(this.leagues.values()));
        mainPane.add(cb_leagues, 1,rowInx);
        rowInx++;
        Label l_seasons = new Label("Seasons:");
        mainPane.add(l_seasons, 0,rowInx);
        List<String> c_seasons = client.sendToServer("viewInformationAboutSeasons|");
        fillSeasonsMap(c_seasons);
        ChoiceBox<String> cb_seasons = new ChoiceBox<>(FXCollections.observableArrayList(this.seasons.values()));
        mainPane.add(cb_seasons, 1,rowInx);
        rowInx++;
        //add assignment and score policies
        Label l_assignment = new Label("Game Assignment Policy:");
        mainPane.add(l_assignment, 0,rowInx);
        List<String> c_assignment = client.sendToServer("getAllAssignmentsPolicies|"+loggedUser);
        ChoiceBox<String> cb_assignment  = new ChoiceBox<>(FXCollections.observableArrayList(c_assignment));
        mainPane.add(cb_assignment, 1,rowInx);
        rowInx++;
        Label l_score = new Label("Game Score Policy:");
        mainPane.add(l_score, 0,rowInx);
        List<String> c_score = client.sendToServer("getAllScorePolicies|"+loggedUser);
        ChoiceBox<String> cb_score = new ChoiceBox<>(FXCollections.observableArrayList(c_score));
        mainPane.add(cb_score, 1,rowInx);
        rowInx++;
        //add registration fee
        Label l_fee = new Label("Registration fee:");
        mainPane.add(l_fee, 0,rowInx);
        TextField tf_fee = new TextField();
        mainPane.add(tf_fee, 1,rowInx);
        rowInx++;
        Button configureBtn = new Button("Configure");
        configureBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String league =cb_leagues.getValue() , season = cb_seasons.getValue(), assignment = cb_assignment.getValue(), score = cb_score.getValue(), fee = tf_fee.getText();
                if(Checker.isValid(league)&&Checker.isValid(season) &&Checker.isValid(assignment)&&Checker.isValid(score)&&Checker.isValidNumber(fee)){
                    String leagueId = getIdFromName(league, leagues);
                    String seasonId = getIdFromName(season, seasons);
                    List<String> receive = client.sendToServer("configureLeagueInSeason|"+loggedUser+"|"+leagueId+"|"+seasonId+"|"+assignment+"|"+score+"|"+fee);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                    clearMainView(mainView1);
                }
            }
        });
        mainPane.add(configureBtn, 0, rowInx);
        mainView1.getChildren().add(mainPane);
    }//need to test this with DB



    public void assignGames(){///////fix!!!
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("please select a league to assign games to it:");
        mainPane.add(label, 0,0);
        addLeaguesInSeasonToPane(mainPane);
        Button assignBtn = new Button("Assign");
        assignBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String league = cb_leagueInSeasons.getValue();
                if(Checker.isValid(league)){
                    List<String> receive = client.sendToServer("assignGames|"+loggedUser+"|"+league);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
            }
        });
        //let user select a league in season - from leagues without games
        //send request to assign games
        //show ack to user
    }

    public void appointReferee()
    {
        buildForm("referee", mainView1, client, loggedUser, mainPane);
    }

    public void addRefereeToLeague(){
        clearMainView(mainView1);
        Label label = new Label("please select League and Referee:");
        clearMainView(mainPane);
        mainPane.add(label, 0,0);
        addLeaguesInSeasonToPane(mainPane);
        List<String> referees = client.sendToServer("getAllReferees|"+loggedUser);
        ObservableList<String> refList = FXCollections.observableArrayList();
        List<RefereeRecord> refRecords = new LinkedList<>();
        for(String ref: referees){
            if(ref.length()>0){
                RefereeRecord record = new RefereeRecord(ref);
                refList.add(record.getName());
                refRecords.add(record);
            }
        }
        ChoiceBox<String> cb_referees = new ChoiceBox<>(refList);
        Label ref = new Label("Referee:");
        mainPane.add(ref, 0, 2);
        mainPane.add(cb_referees, 1, 2);
        Button addBtn = new Button("add");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ref = cb_referees.getValue(), league = cb_leagueInSeasons.getValue(), refId="";
                if(Checker.isValid(ref)&&Checker.isValid(league)){

                    for(RefereeRecord record : refRecords){
                        if(record.getName().equals(ref)) {
                            refId = record.getId();
                        }
                    }
                    String leagueId = getIdFromName(league, leagueInSeasons);
                    List<String> receive = client.sendToServer("addRefereeToLeague|"+loggedUser+"|"+refId+"|"+leagueId);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
                else
                    showAlert("Invalid selection of values", Alert.AlertType.ERROR);
            }
        });
        mainPane.add(addBtn, 0, 3);
        mainView1.getChildren().add(mainPane);

    }

    private ChoiceBox<String> cb_leagueInSeasons;
    private ChoiceBox<String> cb_policies;

    public void changeScorePolicy(){
        changePolicy("score");
    }

    public void changeAssignmentPolicy(){
        changePolicy("assignment");
    }

    public void addTUTUPaymentToTeam(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        //let user select a team and amount
        //send request to add payment to team
    }
    public void addPaymentsFromTheTUTU(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        //let user enter a double that represents the amount to add to union accounting system
        //show ack to user
    }

    public void addTeamToLeague(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label label = new Label("please select League and a team");
        mainPane.add(label, 0,0);
        addLeaguesInSeasonToPane(mainPane);
        List<String> teams = client.sendToServer("getAllOpenTeams_Union|"+loggedUser);
        ObservableList<String> ol_teams = FXCollections.observableArrayList();
        List<TeamRecord> teamRecords = new LinkedList<>();
        for(String team : teams){
            if(team.length()>0){
                TeamRecord record = new TeamRecord(team);
                ol_teams.add(record.getName());
                teamRecords.add(record);
            }
        }
        ChoiceBox<String> cb_teams = new ChoiceBox<>(ol_teams);
        Label l_teams = new Label("Teams: ");
        mainPane.add(l_teams, 0, 2);
        mainPane.add(cb_teams, 1, 2);
        Button addBtn = new Button("Add");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String league = cb_leagueInSeasons.getValue(), team = cb_teams.getValue(), teamId="";
                if(Checker.isValid(league)&&Checker.isValid(team)){
                    for(TeamRecord record : teamRecords){
                        if(record.getName().equals(team))
                            teamId = record.getId();
                    }
                    String leagueId = getIdFromName(league, leagueInSeasons);
                    List<String> receive = client.sendToServer("addTeamToLeague|"+loggedUser+"|"+leagueId+"|"+teamId);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }
                else
                    showAlert("Invalid selection of values", Alert.AlertType.ERROR);
            }
        });
        mainPane.add(addBtn, 0, 3);
        mainView1.getChildren().add(mainPane);
    }
    public void calculateLeagueScore()
    {

        calculate("league");
        //send request to server
        //show league score table

    }

    public void calculateGameScore()
    {
        calculate("game");
        //select league
        //select game/all games/some games?
        //send request to server to calculate score
    }
    public void changeRegistrationFee(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        //select league
        //show fee
        //les user enter new fee
        //check fee
        //send request to change fee
    }

    private void calculate(String type){///////fix!!!
        clearMainView(mainView1);
        clearMainView(mainPane);
        addLeaguesInSeasonToPane(mainPane);
        Label label =null;
        StringBuilder request = new StringBuilder();
        switch (type){
            case "league":{
                label = new Label("select league to calculate its total score");
                request.append("calculateLeagueScore|");
                break;
            }
            case "game":{
                label = new Label("select league to calculate its game's score");
                request.append("calculateGameScore|");
                List<String> games = client.sendToServer("getAllPastGames_UR|"+loggedUser);
                ChoiceBox<String> cb_games = new ChoiceBox<>(FXCollections.observableArrayList(games));
                
                break;
            }
        }
        mainPane.add(label,0,0);
        mainView1.getChildren().add(mainPane);

    }

    //make getAllLeagues()return a list of <League.name:season.year> of all LeagueInSeason
    private void addLeaguesInSeasonToPane(GridPane pane) {
        List<String> receiveLeagues = client.sendToServer("allLeaguesInSeasons|"+loggedUser);
        for(String league:receiveLeagues){
            LeagueInSeasonRecord record = new LeagueInSeasonRecord(league);
            this.leagueInSeasons.put(record.getId(), record.getLeague() + " " + record.getSeason());
        }
        Label league = new Label("League:");
        pane.add(league, 0, 1);
        ObservableList<String> leagues = FXCollections.observableArrayList(this.leagueInSeasons.values());
        cb_leagueInSeasons = new ChoiceBox<>(leagues);
        pane.add(cb_leagueInSeasons, 1, 1);
    }

    private void addPolicyToPane(String type, GridPane pane, String label1, String policy1) {
        List<String> receivePolicies = client.sendToServer(type+"|"+loggedUser);
        Label label = new Label(label1);
        Label policy = new Label(policy1);
        pane.add(label, 0, 0);
        pane.add(policy, 0, 2);
        ObservableList<String> policies = FXCollections.observableArrayList(receivePolicies);
        cb_policies = new ChoiceBox<>(policies);
        pane.add(cb_policies, 1, 2);
    }
    private void addChangeButton(String request, GridPane pane) {
        Button changeBtn = new Button("Change");
        changeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cb_leagueInSeasons.getValue().length()<1|| cb_policies.getValue().length()<1){
                    showAlert("No league or policy was selected!", Alert.AlertType.ERROR);
                    return;
                }
                String leagueId = getIdFromName(cb_leagueInSeasons.getValue(), leagueInSeasons);
                List<String> receive = client.sendToServer(request+"|"+loggedUser+"|"+leagueId+"|"+cb_policies.getValue());
                if(receive.get(0).contains("Succeed")){
                    showAlert("Policy has changed to"+cb_policies.getValue(), Alert.AlertType.INFORMATION);
                }
                else
                    showAlert("Couldn't change policy", Alert.AlertType.ERROR);
            }
        });
        pane.add(changeBtn, 0, 3);
    }

    private void changePolicy(String type) {
        clearMainView(mainView1);
        GridPane pane = new GridPane();
        addLeaguesInSeasonToPane(pane);
        switch (type){
            case "score":{
                addPolicyToPane("getAllScorePolicies", pane, "Please select league and score policy", "Score Policy:");
                addChangeButton("changeScorePolicy", pane);
                break;
            }
            case "assignment":{
                addPolicyToPane("getAllAssignmentsPolicies", pane, "Please select league and assignment policy", "Assignment Policy:");
                addChangeButton("changeAssignmentPolicy", pane);
                break;
            }
        }


        mainView1.getChildren().add(pane);
    }

    private void fillSeasonsMap(List<String> seasons) {
        for(String season: seasons){
            SeasonRecord record = new SeasonRecord(season);
            this.seasons.put(record.getId(), record.getYear());
        }

    }

    private void fillLeaguesMap(List<String> leagues) {

        for(String league: leagues){
            LeagueRecord record = new LeagueRecord(league);
            this.leagues.put(record.getId(), record.getName());
        }
    }
}
