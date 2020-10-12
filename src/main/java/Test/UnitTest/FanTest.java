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

public class FanTest {

    FootballManagementSystem system;
    User user;
    User mesi;
    PersonalPage mesiPage;
    Fan fan;
    Game game;
    Admin admin;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(false);
        //String  leagueId = system.dataReboot();
        //LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        LeagueInSeason league =system.getDatabase().getAllLeaguesInSeasons().get(0);
        admin = system.getAdmin();
        Guest guest = new Guest();
        user = guest.register("fantest@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        user = guest.login("fantest@gmail.com", "Aa1234");
       /* mesi = admin.addNewPlayer("mesi", "mesi", "mesitest1@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesi.checkUserRole("HasPage");
        mesiPage = ((HasPage) pageRole).getPage();*/
        fan = (Fan) user.checkUserRole("Fan");
        /*create games*/
        //User union = admin.addNewUnionRepresentative("Union", "Rep", "union@gmail.com");
        //UnionRepresentative unionRole = ((UnionRepresentative)union.checkUserRole("UnionRepresentative"));
        //unionRole.assignGames(league.getId());
        game= Database.getGame(league.getGamesId().get(1));
    }

    @Test
    public void registrationForGamesAlerts(){
        //user = guest.register("fantest@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        List<String> games = new LinkedList<>();
        games.add(game.getId());
        fan.registrationForGamesAlerts(games, false);
        system.getDatabase().removeFromTables(fan.getID());
    }

    @Test
    public void addPageToFollow() {

        //assertTrue(fan.addPageToFollow(mesiPage.getId()));
        List<String> games = new LinkedList<>();
        games.add(game.getId());
        assertTrue(fan.registrationForGamesAlerts(games, true));

        /*for notification*/
        //Field newField = new Field("Jerusalem","Teddy", 10000, 200000);
        //game.setField(newField);
        //assertEquals(1, fan.getMessageBox().size(),0);

        //Guest guest1 = new Guest();
        //User user1 = guest1.register("newfam@gmail.com", "Aa1234", "fan1", "fan1", "0500001234", "yosef23");
        //Fan fan1 = (Fan) user1.checkUserRole("Fan");
        //assertEquals(0, fan1.getMessageBox().size(), 0);
        //Referee mainReferee = game.getMainReferee();
        //mainReferee.addEventToGame(game.getId(), Event.EventType.RedCard, game.getHostTeam().getPlayers().get(0).getID(), game.getHostTeam().getID());
        //assertEquals(2, fan.getMessageBox().size(), 0);
        //assertEquals(1, mainReferee.getMessageBox().size(), 0);
    }

  /*  @Test
    public void getAllFutureGames(){
        List<String> futureGames = fan.getAllFutureGames();

        assertNotNull(futureGames);
    }*/
    @Test
    public void editPersonalInfo() {
        fan.editPersonalInfo(user, "shir", "shir", "ff", "052555654");
        assertEquals(fan.getAddress(), "ff");
    }

    @Test
    public void submitComplaint() {
        assertNotNull(fan.submitComplaint("bla bla"));
        assertFalse(fan.submitComplaint(""));
    }

    @Test
    public void getAllFutureGames(){
        assertNotNull((fan.getAllFutureGames()));
    }
    @Test
    public void getFollowedPages() {
        mesi = admin.addNewPlayer("mesi", "mesi", "mesitest1@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesi.checkUserRole("HasPage");
        mesiPage = ((HasPage) pageRole).getPage();
        fan.addPageToFollow(mesiPage.getId());
        assertEquals(fan.getFollowedPages().size(), 1);
    }

    @Test
    public void getAddress() {
        assertNotNull(fan.getAddress());
    }

    @Test
    public void getPhone() {
        assertNotNull(fan.getPhone());
    }

    @Test
    public void getComplaints() {
        assertEquals(fan.getComplaintsId().size(), 0);
    }

    @Test
    public void getFollowPages() {
        assertEquals(fan.getFollowedPages().size(), 0);
    }

    @Test
    public void myRole() {
        assertEquals(fan.myRole(),"Fan");
    }

}