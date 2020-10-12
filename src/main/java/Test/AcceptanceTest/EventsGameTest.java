package AcceptanceTest;

import Data.Database;
import Domain.*;

import Service.FootballManagementSystem;
import Service.RefereeSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.*;


public class EventsGameTest {
    RefereeSystem refereeSystem;
    FootballManagementSystem system;
    Admin admin;
    Referee referee;
    Game game;
    User mesi;
    @Before
    public void init() {
        refereeSystem=new RefereeSystem();
        system = new FootballManagementSystem();
        system.systemInit(false);
        //String  leagueId = system.dataReboot();
        //LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        LeagueInSeason league =system.getDatabase().getAllLeaguesInSeasons().get(0);
        admin = (Admin) system.getAdmin();
        game= Database.getGame(league.getGamesId().get(0));
        referee=game.getMainReferee();
        mesi = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);

    }

    @Test
    public void eventsGameSuccess_72()
    {
       assertTrue(refereeSystem.addEventToGame(referee.getID(),game.getId(), "RedCard",game.getHostTeam().getPlayers().get(0).getID(),game.getHostTeam().getID()));
    }
    @Test
    public void eventsGameFail_73()
    {

        assertFalse(refereeSystem.addEventToGame(referee.getID(),game.getId(), "RedCard",game.getHostTeam().getPlayers().get(0).getID(),null));

        assertFalse(refereeSystem.addEventToGame(game.getMainReferee().getID(),game.getId(), "RedCard",game.getHostTeam().getPlayers().get(0).getID(),null));
    }
    @Test
    public void changeEventsGame_74()//check about the 5h
    {
        assertTrue(refereeSystem.changeEvent(game.getMainReferee().getID(),game.getId(),game.getEventReportString().get(0),"new"));
    }
}
