package UnitTest;

import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UserTest {
    FootballManagementSystem system;
    User mesi;
    User user;
    Admin admin;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(false);
        admin = system.getAdmin();
        mesi = admin.addNewPlayer("mesi", "mesi", "mesi2@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Guest guest = new Guest();
       // user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        user = guest.login("fan@gmail.com", "Aa1234");
    }
    @Test
    public void checkUserRole() {
        assertNotNull(mesi.checkUserRole("Player"));
    }

    @Test
    public void addToSearchHistory() {
        mesi.addToSearchHistory("mesi");
        assertEquals(mesi.getSearchHistory().size(),1);
    }

    @Test
    public void getSearchHistory() {
        assertEquals(mesi.getSearchHistory().size(),0);

    }

    @Test
    public void logout() {
    }

    @Test
    public void editPersonalInfo() {
        mesi.editPersonalInfo("leonel","mesi");
        assertEquals(mesi.getName(),"leonel mesi");
    }

    @Test
    public void search() {
        assertNotNull(mesi.search("mesi"));
    }


    @Test
    public void changePassword() {
        user.changePassword("Aa1234","Bb1234");
    }

    @Test
    public void deactivate() {
        mesi.deactivate();
        assertFalse(mesi.isActive());
    }

    @Test
    public void reactivate() {
        mesi.reactivate();
        assertTrue(mesi.isActive());
    }

    @Test
    public void isActive() {
        assertTrue(mesi.isActive());


    }

    @Test
    public void addMessage() {
        mesi.addMessage("shalom");
        //assertEquals(mesi.getMessageBox().size(),1);
        mesi.addMessage("shalom");
        assertEquals(mesi.getMessageBox().size(),2);

    }

    @Test
    public void getName() {
        assertEquals(mesi.getName(),"mesi mesi");
    }

    @Test
    public void setFirstName() {
        mesi.setFirstName("m");
        assertEquals(mesi.getName(),"m mesi");
    }

    @Test
    public void setLastName() {
        mesi.setLastName("m");
        assertEquals(mesi.getName(),"mesi m");
    }

    @Test
    public void getID() {
        assertNotNull(mesi.getID());
    }

    @Test
    public void getMail() {
        assertEquals(mesi.getMail(),"mesi@mail.com");
    }

    @Test
    public void getMessageBox() {
        assertEquals(mesi.getMessageBox().size(),0);

    }

    @Test
    public void addRole() {
       TeamManager teamManager = new TeamManager(mesi.getID(), 30000, true, true);
        mesi.addRole(teamManager);
        assertEquals(mesi.getRoles().size(),3);

    }

    @Test
    public void getRoles() {
        assertEquals(mesi.getRoles().size(),2);
    }

}