package Presentation;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;

import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.*;

public class UserController extends GeneralController implements Initializable {
    
    @FXML private HBox mainView1;
    @FXML private MenuBar mb_mainMenu1;
    @FXML private Label l_systemName;
    @FXML private ImageView iv_systemLogo;


    private Client m_client;
    private String loggedUser;
    private GridPane mainPane1;


    private AdminController admin;
    private FanController fan;
    private HasPageController page;
    private ManagementController management;
    private OwnershipController ownership;
    private RefereeController referee;
    private UnionController union;



    public void editPersonalInfoButtonPushed(ActionEvent action){
        clearMainView(mainView1);
        clearMainView(mainPane1);
        List<String> userInfo = m_client.sendToServer("getUserInfo|"+loggedUser);
        String[]split = userInfo.get(0).split(",");

        //show users info
        //let user select what to change - name, password etr
        //send request to change info
        //return ack to user - success or failure
    }

    public void searchButtonPushed(ActionEvent actionEvent){
        clearMainView(mainView1);
        clearMainView(mainPane1);
        buildSearchView(mainPane1, mainView1, m_client, loggedUser);
        //find a way to reuse code in guest controller
    }

    public void viewInfoButtonPushed(ActionEvent actionEvent){
        clearMainView(mainView1);
        clearMainView(mainPane1);
        buildViewInfoScene(mainPane1, mainView1, m_client);
        //find a way to reuse code in guest controller
    }

    public void viewSearchHistoryButtonPushed(ActionEvent actionEvent){
        clearMainView(mainView1);
        clearMainView(mainPane1);
        List<String> history = m_client.sendToServer("viewSearchHistory|"+loggedUser);

        mainView1.getChildren().add(mainPane1);
        showListOnScreen("Search History",history, mainPane1,0);
    }

    public void logoutButtonPushed(ActionEvent actionEvent) {
        if(loggedUser!=null &&loggedUser.length()>0){
            m_client.stopNotifications();
            m_client.sendToServer("logOut|"+loggedUser);
            loggedUser = "";
            setSceneByFXMLPath("GuestView.fxml", null, "", m_client, mainPane1);
        }
    }

    public void setUser(String user){
        this.loggedUser = user;
    }

    public void setClient(Client client) {
        this.m_client = client;
    }

    public void setMainPane1(GridPane mainPane1) {
        this.mainPane1 = mainPane1;
    }

    public void buildPresentation(List<String> roles) {
        clearMainView(mainView1);
        clearMainView(mainPane1);
        for(String role : roles){
            switch(role){
                case("Fan"):{
                    fan = new FanController(mainView1, loggedUser, m_client, mainPane1);
                    Menu fanMenu = new Menu("Fan Actions");

                    MenuItem followGames = new MenuItem("Follow Games");
                    followGames.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            fan.followGames();
                        }
                    });
                    fanMenu.getItems().add(followGames);
                    MenuItem submitComplaint = new MenuItem("Submit Complaint");
                    submitComplaint.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            fan.submitComplaint();
                        }
                    });
                    fanMenu.getItems().add(submitComplaint);
                    mb_mainMenu1.getMenus().add(fanMenu);
                    break;
                }
                case("Admin"):{
                    admin = new AdminController(mainView1, loggedUser, m_client, mainPane1);
                    Menu adminMenu = new Menu("Admin Actions");
                    MenuItem closeTeamPermanently = new MenuItem("Close Team Permanently");
                    closeTeamPermanently.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.closeTeamPermanently();
                        }
                    });
                    adminMenu.getItems().add(closeTeamPermanently);
                    MenuItem addNewPlayer = new MenuItem("Add New Player");
                    addNewPlayer.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewPlayer();
                        }
                    });
                    adminMenu.getItems().add(addNewPlayer);
                    MenuItem addNewCoach = new MenuItem("Add New Coach");
                    addNewCoach.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewCoach();
                        }
                    });
                    adminMenu.getItems().add(addNewCoach);
                    MenuItem addNewTeamOwner = new MenuItem("Add New TeamOwner");
                    addNewTeamOwner.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewTeamOwner();
                        }
                    });
                    adminMenu.getItems().add(addNewTeamOwner);
                    MenuItem addNewTeamManager = new MenuItem("Add New TeamManager");
                    addNewTeamManager.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewTeamManager();
                        }
                    });
                    adminMenu.getItems().add(addNewTeamManager);
                    MenuItem addNewUnionRepresentative = new MenuItem("Add New UnionRepresentative");
                    addNewUnionRepresentative.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewUnionRepresentative();
                        }
                    });
                    adminMenu.getItems().add(addNewUnionRepresentative);
                    MenuItem addNewAdmin = new MenuItem("Add New Admin");
                    addNewAdmin.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.addNewAdmin();
                        }
                    });
                    adminMenu.getItems().add(addNewAdmin);
                    MenuItem removeUser = new MenuItem("Remove User");
                    removeUser.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.removeUser();
                        }
                    });
                    adminMenu.getItems().add(removeUser);
                    MenuItem viewLog = new MenuItem("View Log");
                    viewLog.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.viewLog();
                        }
                    });
                    adminMenu.getItems().add(viewLog);
                    MenuItem responseToComplaint = new MenuItem("Response To Complaint");
                    responseToComplaint.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            admin.responseToComplaint();
                        }
                    });
                    adminMenu.getItems().add(responseToComplaint);
                    mb_mainMenu1.getMenus().add(adminMenu);
                    break;
                }
                case("TeamOwner"):{
                    ownership = new OwnershipController(mainView1, loggedUser, m_client, mainPane1);
                    Menu ownerMenu = new Menu("Ownership Actions");
                    MenuItem addTeam = new MenuItem("Open New Team");
                    addTeam.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ownership.openNewTeam();
                        }
                    });
                    ownerMenu.getItems().add(addTeam);
                    MenuItem appointTeamOwner = new MenuItem("Appoint TeamOwner");
                    appointTeamOwner.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ownership.appointTeamOwner();
                        }
                    });
                    ownerMenu.getItems().add(appointTeamOwner);
                    MenuItem appointTeamManager = new MenuItem("Appoint TeamManager");
                    appointTeamManager.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ownership.appointTeamManager();
                        }
                    });
                    ownerMenu.getItems().add(appointTeamManager);
                    MenuItem closeTeam = new MenuItem("Close Team");
                    closeTeam.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ownership.closeTeam();
                        }
                    });
                    ownerMenu.getItems().add(closeTeam);
                    MenuItem reopenTeam = new MenuItem("Reopen Team");
                    reopenTeam.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ownership.reopenTeam();
                        }
                    });
                    ownerMenu.getItems().add(reopenTeam);
                    mb_mainMenu1.getMenus().add(ownerMenu);
                    //manage:
                    addManagement();
                    break;
                }
                case("TeamManager"):{
                    addManagement();
                    break;
                }
                case("UnionRepresentative"):{
                    union = new UnionController(mainView1, loggedUser, m_client, mainPane1);
                    Menu unionMenu = new Menu("Union Actions");
                    MenuItem configureNewLeague = new MenuItem("Configure New League");
                    configureNewLeague.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.configureNewLeague();
                        }
                    });
                    unionMenu.getItems().add(configureNewLeague);
                    MenuItem configureNewSeason = new MenuItem("Configure New Season");
                    configureNewSeason.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.configureNewSeason();
                        }
                    });
                    unionMenu.getItems().add(configureNewSeason);
                    MenuItem configureLeagueInSeason = new MenuItem("Configure LeagueInSeason");
                    configureLeagueInSeason.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.configureLeagueInSeason();
                        }
                    });
                    unionMenu.getItems().add(configureLeagueInSeason);
                    MenuItem addTeamToLeague = new MenuItem("Add Team To League");
                    addTeamToLeague.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.addTeamToLeague();
                        }
                    });
                    unionMenu.getItems().add(addTeamToLeague);
                    MenuItem assignGames = new MenuItem("Assign Games");
                    assignGames.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.assignGames();
                        }
                    });
                    unionMenu.getItems().add(assignGames);
                    MenuItem appointReferee = new MenuItem("Appoint Referee");
                    appointReferee.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.appointReferee();
                        }
                    });
                    unionMenu.getItems().add(appointReferee);
                    MenuItem addRefereeToLeague = new MenuItem("Add Referee To League");
                    addRefereeToLeague.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.addRefereeToLeague();
                        }
                    });
                    unionMenu.getItems().add(addRefereeToLeague);
                    MenuItem changeScorePolicy = new MenuItem("Change Score Policy");
                    changeScorePolicy.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.changeScorePolicy();
                        }
                    });
                    unionMenu.getItems().add(changeScorePolicy);
                    MenuItem changeAssignmentPolicy = new MenuItem("Change Assignment Policy");
                    changeAssignmentPolicy.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.changeAssignmentPolicy();
                        }
                    });
                    unionMenu.getItems().add(changeAssignmentPolicy);
                    MenuItem addTUTUPaymentToTeam = new MenuItem("Add TUTU Payment To Team");
                    addTUTUPaymentToTeam.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.addTUTUPaymentToTeam();
                        }
                    });
                    unionMenu.getItems().add(addTUTUPaymentToTeam);
                    MenuItem addPaymentsFromTheTUTU = new MenuItem("Add Payments From The TUTU");
                    addPaymentsFromTheTUTU.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.addPaymentsFromTheTUTU();
                        }
                    });
                    unionMenu.getItems().add(addPaymentsFromTheTUTU);
                    MenuItem calculateLeagueScore = new MenuItem("Calculate League Score");
                    calculateLeagueScore.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.calculateLeagueScore();
                        }
                    });
                    unionMenu.getItems().add(calculateLeagueScore);
                    MenuItem calculateGameScore = new MenuItem("Calculate Game Score");
                    calculateGameScore.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.calculateGameScore();
                        }
                    });
                    unionMenu.getItems().add(calculateGameScore);
                    MenuItem changeRegistrationFee = new MenuItem("Change Registration Fee");
                    changeRegistrationFee.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            union.changeRegistrationFee();
                        }
                    });
                    unionMenu.getItems().add(changeRegistrationFee);
                    mb_mainMenu1.getMenus().add(unionMenu);
                    break;
                }
                case("Referee"):{
                    referee = new RefereeController(mainView1, loggedUser, m_client, mainPane1);
                    Menu refMenu = new Menu("Referee Actions");
                    MenuItem addEventToGame = new MenuItem("Add Event To Game");
                    addEventToGame.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            referee.addEventToGame();
                        }
                    });
                    refMenu.getItems().add(addEventToGame);
                    //MenuItem setScoreInGame = new MenuItem("Set Score In Game");
                    //setScoreInGame.setOnAction(new EventHandler<ActionEvent>() {
                    //    @Override
                    //    public void handle(ActionEvent event) {
                    //        referee.setScoreInGame();
                    //    }
                    //});
                    //refMenu.getItems().add(setScoreInGame);
                    MenuItem getEventReport = new MenuItem("Create Game Report");
                    getEventReport.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            referee.getGameReport();
                        }
                    });
                    refMenu.getItems().add(getEventReport);
                    MenuItem changeEvent = new MenuItem("Change Event");
                    changeEvent.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            referee.changeEvent();
                        }
                    });
                    refMenu.getItems().add(changeEvent);
                    mb_mainMenu1.getMenus().add(refMenu);
                    break;
                }
                case("HasPage"):{
                    page = new HasPageController(mainView1, loggedUser, m_client, mainPane1);
                    Menu pageMenu = new Menu("Page Management");
                    MenuItem viewPage = new MenuItem("View Personal Page");
                    viewPage.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            page.viewPage();
                        }
                    });
                    pageMenu.getItems().add(viewPage);
                    MenuItem uploadToPage = new MenuItem("Upload To Page");
                    uploadToPage.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            page.uploadToPage();
                        }
                    });
                    pageMenu.getItems().add(uploadToPage);
                    mb_mainMenu1.getMenus().add(pageMenu);
                    break;
                }

            }
        }
    }

    private void addManagement() {
        management = new ManagementController(mainView1, loggedUser, m_client, mainPane1);
        Menu manageMenu = new Menu("Management Actions");
        MenuItem addPlayerToTeam = new MenuItem("Add Player To Team");
        addPlayerToTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.addPlayerToTeam();
            }
        });
        manageMenu.getItems().add(addPlayerToTeam);
        MenuItem addCoachToTeam = new MenuItem("Add Coach To Team");
        addCoachToTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.addCoachToTeam();
            }
        });
        manageMenu.getItems().add(addCoachToTeam);
        MenuItem addFieldToTeam = new MenuItem("Add Field To Team");
        addFieldToTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.addFieldToTeam();
            }
        });
        manageMenu.getItems().add(addFieldToTeam);
        MenuItem updateAsset = new MenuItem("Update Asset");
        updateAsset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.updateAsset();
            }
        });
        manageMenu.getItems().add(updateAsset);
        MenuItem reportIncome = new MenuItem("Report Income");
        reportIncome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.reportIncome();
            }
        });
        manageMenu.getItems().add(reportIncome);
        MenuItem reportExpanse = new MenuItem("Report Expanse");
        reportExpanse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.reportExpanse();
            }
        });
        manageMenu.getItems().add(reportExpanse);
        MenuItem getBalance = new MenuItem("Show Team Balance");
        getBalance.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                management.getBalance();
            }
        });
        manageMenu.getItems().add(getBalance);
        mb_mainMenu1.getMenus().add(manageMenu);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        l_systemName.setText(ClientMain.SYSTEM_NAME);
        setImage(iv_systemLogo,"resources/logo.png");

        ClientMain.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you wish to exit?");
                alert.setHeaderText("Exit");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)){
                    logoutButtonPushed(null);
                    //closeSockets(m_client);
                } else {
                    event.consume();
                }
            }
        });

    }
    public void startGettingNotifications(){
        m_client.startNotifications(loggedUser);
    }

}
