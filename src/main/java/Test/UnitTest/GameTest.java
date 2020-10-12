package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import java.util.Date;

import static org.junit.Assert.*;

    public class GameTest {
        Game game;
        FootballManagementSystem system;
        User user;
        Admin admin;
        LeagueInSeason league;


    @Before
    public void init(){

        system = new FootballManagementSystem();
        system.systemInit(false);
        //String  leagueId = system.dataReboot();
        league =system.getDatabase().getAllLeaguesInSeasons().get(0);

        admin = system.getAdmin();

        //User union = admin.addNewUnionRepresentative("Union", "Rep", "unionRep@gmail.com");
        //UnionRepresentative unionRole = ((UnionRepresentative)union.checkUserRole("UnionRepresentative"));
        //unionRole.assignGames(league.getId());
        game= Database.getGame(league.getGamesId().get(0));
        Guest guest = new Guest();

        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        if(user==null) {
            user = guest.login("fan@gmail.com", "Aa1234");
        }

    }

    @Test
    public void addFanForNotifications() {
        Fan fan = (Fan) user.checkUserRole("Fan");
        assertTrue(game.addFanForNotifications(fan,true));
        system.getDatabase().removeFromTables(fan.getID());

    }

    @Test
    public void getEventReportString() {
        assertNotNull(game.getEventReportString());
    }

    @Test
    public void getId() {
        assertNotNull(game.getId());
    }

    @Test
    public void getDate() {
        Date date =Database.getDate(2021, 11, 21, 20, 0);//enter every time the correct date
        assertEquals(game.getDate().toString(),date.toString());
    }


    @Test
    public void hostScore() {
        game.setHostScore(2);
        assertEquals(game.hostScore(),2);
    }

    @Test
    public void guestScore() {
        game.setGuestScore(3);
        assertEquals(game.guestScore(),3);
    }

    @Test
    public void setHostScore() {
        game.setHostScore(2);
        assertEquals(game.hostScore(),2);
    }

    @Test
    public void setGuestScore() {
        game.setGuestScore(3);
        assertEquals(game.guestScore(),3);
    }

    @Test
    public void getField() {
            assertEquals(game.getField().getName(),"Teddy");
        }

    @Test
    public void getMainReferee() {
        assertNotNull(game.getMainReferee());
    }

    @Test
    public void getSideReferees() {
        assertEquals(game.getSideReferees().size(),2);
    }

    @Test
    public void getHostTeam() {
        Team team0 = league.getTeams().get(0);
        assertEquals(game.getHostTeam().getName(),team0.getName());
    }

    @Test
    public void getGuestTeam() {
        Team team1=league.getTeams().get(1);
        assertEquals(game.getGuestTeam().getName(),team1.getName());

    }

    @Test
    public void getName(){
        assertEquals(game.getName(), game.getHostTeam().getID() + ":" + game.getHostTeam().getName()
                + " VS " + game.getGuestTeam().getID() + ":" + game.getGuestTeam().getName());
    }

    @Test
    public void getEventReport() {
        assertEquals(game.getEventReport().getEvents().size(),0);
    }

    @Test
    public void setDate() {
        game.setDate(Database.getDate(2020, 5, 25, 20, 0));
        assertEquals(game.getDate(),Database.getDate(2020, 5, 25, 20, 0));
    }

    @Test
    public void setField() {
        Field field1 = new Field("beer sheva","terner", 10000, 150000);
        game.setField(field1);
        assertEquals(game.getField().getName(),field1.getName());
    }

    @Test
    public void setNewsFromReferee() {

        assertTrue(game.setNewsFromReferee("test news"));
    }

    @Test
    public void getLeague() {
        assertNotNull(game.getLeague());

    }

    @Test
    public void getFansForAlerts() {
        assertNotNull(game.getFansForAlerts());

    }
}