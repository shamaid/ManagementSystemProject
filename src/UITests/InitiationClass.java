package UITests;

import Presentation.ClientMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.api.FxAssert;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;
import javafx.stage.Stage;


public class InitiationClass extends ApplicationTest {

    protected Stage stage;

    @BeforeClass
    public static void startApp() {

        try {
            ApplicationTest.launch(ClientMain.class);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage stage)
    {
        this.stage = stage;
        stage.show();

    }

    protected void Sleep(int millis)
    {
        sleep(millis);
    }

}
