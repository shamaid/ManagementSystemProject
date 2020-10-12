package UnitTest;

import Data.Database;
import Domain.LeagueInSeason;
import Domain.ScoreTableRecord;
import Domain.Team;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreTableRecordTest {
    ScoreTableRecord record;
    FootballManagementSystem system;
    Team team;
    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        record = new ScoreTableRecord(team, 3);
    }

    @Test
    public void getTeam() {
        assertEquals(record.getTeam().getName(),team.getName());
    }

    @Test
    public void getTotalScore() {
        assertEquals(record.getTotalScore(),3);
    }
}