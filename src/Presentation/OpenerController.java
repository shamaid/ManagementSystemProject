package Presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenerController extends GeneralController implements Initializable  {


    @FXML private ImageView iv_opener;
    @FXML private Label l_systemName;
    @FXML private ImageView iv_systemLogo;

    private MediaPlayer mediaPlayer;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        l_systemName.setText(ClientMain.SYSTEM_NAME);
        setImage(iv_systemLogo, "resources/logo.png");
        setImage(iv_opener, "resources/openingGif.gif");
        startMusic();
    }

    public void enter(){
        setSceneByFXMLPath("GuestView.fxml", null, null, null, null);
    }


    private void initMusic()
    {
        if (mediaPlayer == null)
        {
            String path = "resources/1.mp3";
            File tmpFile = new File(path);
            path = tmpFile.toURI().toASCIIString();
            Media file = new Media(path);
            mediaPlayer = new MediaPlayer(file);
        }

    }
    private void startMusic()
    {
        initMusic();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                enter();
            }
        });
        mediaPlayer.play();
    }
}
