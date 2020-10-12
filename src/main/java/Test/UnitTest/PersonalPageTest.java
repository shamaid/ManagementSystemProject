package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class PersonalPageTest {
    FootballManagementSystem system;
    User user;
    User mesi;
    PersonalPage mesiPage;
    Fan fan;


    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        Admin admin = (Admin) system.getAdmin();
        Guest guest = new Guest();
        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        mesi = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesi.checkUserRole("HasPage");
        mesiPage = ((HasPage) pageRole).getPage();
        fan = (Fan) user.checkUserRole("Fan");
    }
    @Test
    public void getId() {
        assertNotNull(mesiPage.getId());
    }


    @Test
    public void getData() {
        assertNotNull(mesiPage.getData());
    }

    @Test
    public void getUser() {
        assertEquals(mesiPage.getUser().getID(),mesi.getID());

    }

    @Test
    public void addAFollower() {
        mesiPage.addAFollower(fan);
        assertEquals(fan.getFollowPages().size(),1);
    }

    @Test
    public void setData() {
        mesiPage.setData("shir");
        assertEquals(mesiPage.getData(),"shir");
    }

    @Test
    public void addData() {
        mesiPage.setData("shir");
        mesiPage.addData("ben david");
        assertEquals(mesiPage.getData(),"shir ben david");

    }
}