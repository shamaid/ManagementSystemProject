
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.api.FxAssert;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import static junit.framework.TestCase.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GuiTestClass extends UITests.InitiationClass {

    private final String testMail = "mailmailmail@gggg.com";
    private final String testPass = "Aa123456";


    @Test
    public void A_registerTest()
    {
        clickOn("#register");
        sleep(1000);
        clickOn("#mail").write(testMail);
        clickOn("#pass").write(testPass);
        clickOn("#passVerify").write(testPass);
        clickOn("#firstName").write("Test First Name");
        clickOn("#lastName").write("Test Last Name");
        clickOn("#phone").write("1123456789");
        clickOn("#address").write("Beer Sheba Sadly street 100");
        sleep(1000);
        clickOn("#registerButton");
        sleep(2000);
        press(KeyCode.ENTER);
        sleep(1000);

        Scene currScene = stage.getScene();
        Node logout = currScene.lookup("#logout");
        assertNotNull(logout);

    }

    @Test
    public void B_logoutTest()
    {
        Scene currScene = stage.getScene();
        Node logout = currScene.lookup("#logout");
        assertNotNull(logout);

        clickOn("#logout");
        sleep(2000);

        currScene = stage.getScene();
        Node login = currScene.lookup("#login");

        assertNotNull(login);

    }

    @Test
    public void C_viewInformations()
    {
        clickOn("#viewInformation");
        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);

        clickOn("#selectLabel");
        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);

        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);

        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);

        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);

        clickOn("#selectSubjects");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(2000);
    }

    @Test
    public void D_loginReferee()
    {
        clickOn("#tf_email").write("to0@gmail.com");
        clickOn("#tf_password").write("spK4zv");

        clickOn("#login");
        sleep(2000);
        press(KeyCode.ENTER);

        Scene currScene = stage.getScene();
        Node logout = currScene.lookup("#logout");
        assertNotNull(logout);
    }



}
