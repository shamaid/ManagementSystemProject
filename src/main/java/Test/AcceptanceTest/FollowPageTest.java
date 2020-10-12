package AcceptanceTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import Service.UserSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class FollowPageTest {

    FootballManagementSystem system;
    User user;
    User mesi;
    PersonalPage mesiPage;
    Fan fan;
    UserSystem userSystem;

    @Before
    public void init() {
        userSystem=new UserSystem();

        system = new FootballManagementSystem();
        system.systemInit(true);
        Admin admin = (Admin) system.getAdmin();
        Guest guest = new Guest();
        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        mesi = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesi.checkUserRole("HasPage");
        mesiPage = ((HasPage) pageRole).getPage();
        fan = (Fan) user.checkUserRole("Fan");
    }

    @Test
    public void followPageSuccess_9()
    {
        assertTrue(userSystem.registrationToFollowUp(user.getID(),mesiPage.getId()));
        assertEquals(userSystem.getFanPages(user.getID()).size(),1);
    }

    @Test
    public void followPageFail_10()
    {
        userSystem.registrationToFollowUp(user.getID(),mesiPage.getId());
        assertFalse(userSystem.registrationToFollowUp(user.getID(),mesiPage.getId()));
        assertEquals(userSystem.getFanPages(user.getID()).size(),1);

    }



}
