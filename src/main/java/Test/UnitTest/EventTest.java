package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventTest {


    FootballManagementSystem system;
    EventReport eventReport;
    Event event;
    Game game;

    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        /*create games*/
        Admin admin = (Admin) system.getAdmin();
        User union = admin.addNewUnionRepresentative("Union", "Rep", "unionRep@gmail.com");
        UnionRepresentative unionRole = ((UnionRepresentative)union.checkUserRole("UnionRepresentative"));
        unionRole.assignGames(league.getId());
        game= Database.getGame(league.getGamesId().get(0));
        eventReport = game.getEventReport();
//        event = new Event(Event.EventType.Foul, game,"data");
    }
    @Test
    public void getType() {
        assertNotNull(event.getType());
        assertEquals(Event.EventType.Foul,event.getType());
    }

    @Test
    public void getDate() {
        assertNotNull(event.getDate());
    }

    @Test
    public void getMinuteInGame() {
        assertNotNull(event.getMinuteInGame());
        assertNotNull(event.getMinuteInGame());
    }

    @Test
    public void getDescription() {
        assertNotNull(event.getDescription());
        assertEquals("data",event.getDescription());
    }

    @Test
    public void setDescription() {

        event.setDescription("Description");
        assertEquals("Description",event.getDescription());
    }

    @Test
    public void getId() {
        assertNotNull(event.getId());
    }
}