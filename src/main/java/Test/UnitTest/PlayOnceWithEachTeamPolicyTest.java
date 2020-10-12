package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayOnceWithEachTeamPolicyTest {

    @Test
    public void assignGames() {
        FootballManagementSystem system;
        PlayOnceWithEachTeamPolicy one = new PlayOnceWithEachTeamPolicy();
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason haal = Database.getLeagueInSeason(leagueId);
        assertNotNull(one.assignGames(haal));

    }
}