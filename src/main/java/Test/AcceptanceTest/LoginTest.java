package AcceptanceTest;

import Domain.Guest;
import Domain.User;
import Service.FootballManagementSystem;
import Service.GuestSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest {

    private FootballManagementSystem system;
    private GuestSystem guestSystem;

    @Before
    public void init()
    {
        guestSystem=new GuestSystem();
        system = new FootballManagementSystem();
        system.systemInit(true);
        Guest guest = new Guest();
        User user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
    }


    @Test
    public void loginSuccess_15()
    {

        assertNotNull(guestSystem.logIn("fan@gmail.com","Aa1234"));

    }

    @Test
    public void loginFail_16()
    {
        assertNull(guestSystem.logIn("fan@gmail.com","A234"));

    }

    @Test
    public void loginFail_17()
    {
        assertNull(guestSystem.logIn("fa@gmail.com","A234"));

    }
}
