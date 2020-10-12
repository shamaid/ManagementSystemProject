package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ComplaintTest {
    Fan fan;
    FootballManagementSystem system;
    User user;
    Complaint complaint;

    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        Admin admin = (Admin) system.getAdmin();
        Guest guest = new Guest();
        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        fan = (Fan) user.checkUserRole("Fan");
        complaint=new Complaint("",fan);
    }

    @Test
    public void getDate() {
        assertNotNull(complaint.getDate());
    }

    @Test
    public void getDescription() {
        assertEquals("",complaint.getDescription());
    }

    @Test
    public void getIsActive() {
        assertTrue(complaint.getIsActive());
    }

    @Test
    public void getFanComplained() {
        assertEquals(complaint.getFanComplained().getPhone(),fan.getPhone());
    }

    @Test
    public void deactivate() {
        complaint.deactivate();
        assertFalse(complaint.getIsActive());
    }
    @Test
    public void setResponse() {
        complaint.setResponse("answer to complaint");
        assertNotNull(fan.getMessageBox().get(0));
    }

    @Test
    public void getId(){
        assertNotNull(complaint.getId());
    }
}