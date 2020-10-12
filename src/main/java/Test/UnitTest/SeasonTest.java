package UnitTest;

import Data.Database;
import Domain.LeagueInSeason;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class SeasonTest {
    FootballManagementSystem system;
    LeagueInSeason leagueInSeason;
    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        leagueInSeason = Database.getLeagueInSeason(leagueId);
    }


    @Test
    public void getStartDate() {
       assertEquals( leagueInSeason.getSeason().getStartDate(),Database.getDate(2020, 5, 1));
    }

    @Test
    public void getYear() {
        assertEquals(leagueInSeason.getSeason().getYear(),2020);
    }

    @Test
    public void getLeagueInSeasons() {
        assertEquals(leagueInSeason.getSeason().getLeagueInSeasons().size(),1);
    }

    @Test
    public void addLeagueInSeason() {

    }

    @Test
    public void getId(){
        assertNotNull(leagueInSeason.getSeason().getId());
    }
}