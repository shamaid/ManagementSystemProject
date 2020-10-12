package AcceptanceTest;

import Domain.*;
import Service.FootballManagementSystem;
import Service.GuestSystem;
import Service.UserSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchHistoryTest {


    private FootballManagementSystem system;
    private UserSystem userSystem;
    private GuestSystem guestSystem;

    private String fanUser;

    @Before
    public void init()
    {
        system = new FootballManagementSystem();
        system.systemInit(true);
        userSystem = new UserSystem();
        guestSystem = new GuestSystem();
        system.dataReboot();
        fanUser = guestSystem.register("lironoskar@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");




    }

    @Test
    public void viewSearchHistory_20()
    {
    userSystem.search(fanUser,"team0");
    assertEquals(userSystem.viewSearchHistory(fanUser).size(),1);


    }
    @Test
    public void viewSearchHistory_21()
    {

        assertEquals(userSystem.viewSearchHistory(fanUser).size(),0);

    }
}
