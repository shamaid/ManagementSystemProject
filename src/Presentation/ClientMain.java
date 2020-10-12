package Presentation;

import Domain.UserFactory;
import Service.FootballManagementSystem;
import Service.Server;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientMain extends Application {


    final static String SYSTEM_NAME = "Football Five";

    private static Stage stage;


    public static Stage getStage(){return stage;}

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Opener.fxml"));

        primaryStage.setTitle(SYSTEM_NAME);
        primaryStage.setScene(new Scene(root, 850,800));
        primaryStage.show();

    }


    public static void main(String[] args) {

        //FootballManagementSystem.systemInit(true);
        //FootballManagementSystem.dataReboot();
        //UserFactory.getNewAdmin("aA123456", "adminush", "ush", "m@m.com");
        launch(args);


    }
}