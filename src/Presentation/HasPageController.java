package Presentation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.List;

public class HasPageController extends GeneralController{


    private HBox mainView1;
    private String loggedUser;
    private Client client;
    private GridPane mainPane;

    public HasPageController(HBox mainView1, String loggedUser, Client client, GridPane mainPane) {
        this.mainView1 = mainView1;
        this.loggedUser = loggedUser;
        this.client = client;
        this.mainPane = mainPane;
    }

    public void viewPage(){
        clearMainView(mainView1);
        List<String> page = client.sendToServer("viewPage|"+loggedUser);
        Label l_page = new Label(page.get(0));
        mainPane.add(l_page, 0,0);
        mainView1.getChildren().add(mainPane);

    }
    public void uploadToPage(){
        clearMainView(mainView1);
        clearMainView(mainPane);
        Label l_upload = new Label("Please enter text to upload:");
        mainPane.add(l_upload, 0, 0);
        TextArea uploadArea = new TextArea();
        mainPane.add(uploadArea, 0, 1);
        Button uploadBtn = new Button("Upload");
        uploadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String upload = uploadArea.getText();
                if(Checker.isValid(upload)){
                    List<String> receive = client.sendToServer("uploadToPage|"+loggedUser+"|"+upload);
                    showAlert(receive.get(0), Alert.AlertType.INFORMATION);
                }

            }
        });
        mainPane.add(uploadBtn, 0, 2);
        mainView1.getChildren().add(mainPane);
    }
}
