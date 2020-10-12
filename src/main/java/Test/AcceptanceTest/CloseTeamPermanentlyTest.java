package AcceptanceTest;

import Data.Database;
import Domain.*;
import Service.AdminSystem;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CloseTeamPermanentlyTest {

    private FootballManagementSystem system;
    private AdminSystem adminSystem;
    private Team team;
    private Admin admin;

    @Before
    public void init()
    {
        system = new FootballManagementSystem();
        system.systemInit(true);
        adminSystem = system.getAdminSystem();
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        admin = (Admin) system.getAdmin();

    }

    @Test
    public void closeFail_41() {
        adminSystem.permanentlyCloseTeam(admin.getID(), team.getID());
        assertNull(adminSystem.permanentlyCloseTeam(admin.getID(), team.getID()));

    }
    @Test
    public void closeSuccess_42()
    {
      assertNotNull(adminSystem.permanentlyCloseTeam(admin.getID(),team.getID()));
    }
}
