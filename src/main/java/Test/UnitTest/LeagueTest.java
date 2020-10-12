package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeagueTest {
    FootballManagementSystem system;
    LeagueInSeason leagueInSeason;
    League league;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        leagueInSeason = Database.getLeagueInSeason(leagueId);
        league=new League("Haal", League.LevelLeague.level2);
    }
    @Test
    public void addLeagueInSeason() {
        league.addLeagueInSeason(leagueInSeason);
       assertEquals(league.getLeagueInSeasons().size(),1);
    }

    @Test
    public void getLeagueInSeasons() {
        assertEquals(league.getLeagueInSeasons().size(),0);
    }

    @Test
    public void getName() {
        assertEquals(league.getName(),"Haal");
    }

    @Test
    public void getLevel() {
        assertEquals(league.getLevel(), "level2");
    }

    @Test
    public void getId() {
        assertNotNull(league.getId());
    }
}