package AcceptanceTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import Service.UserSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ComplaintTest {

    FootballManagementSystem system;
    User user;
    Fan fan;
    UserSystem userSystem;


    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(true);
        userSystem=new UserSystem();
        Guest guest = new Guest();
        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        fan = (Fan) user.checkUserRole("Fan");
    }

    @Test
    public void invalidComplaint_13()
    {
       assertFalse(userSystem.addComplaint(user.getID(),""));
    }

    @Test
    public void validComplaint_14()
    {
        assertTrue(userSystem.addComplaint(user.getID(),"good"));

    }
}
