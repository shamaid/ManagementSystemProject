package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventReportTest {

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
//        event = new Event(Event.EventType.RedCard, game, "add event");

    }


    @Test
    public void addEvent() {
        assertEquals(0,eventReport.getEvents().size());
        eventReport.addEvent(event);
        assertEquals(1,eventReport.getEvents().size());
    }

    @Test
    public void getEvents() {
        assertNotNull(eventReport.getEvents());
    }

    @Test
    public void getId(){
        assertNotNull(eventReport.getId());
    }
}