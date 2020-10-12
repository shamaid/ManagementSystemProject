package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

 public class TeamManagerTest {

    TeamManager teamManager;
    FootballManagementSystem system;
    Team team;
    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        Admin admin = (Admin) system.getAdmin();
        User userTeamManager= admin.addNewTeamManager("team", "manager", "teamManager@gmail.com", 20000, false, false);
        teamManager = (TeamManager)userTeamManager.checkUserRole("TeamManager");

    }

    @Test
    public void getID() {
        assertNotNull(teamManager.getID());
    }

    @Test
    public void getPrice() {
        assertEquals(teamManager.getPrice(),20000,1);
    }

    @Test
    public void setPrice() {
        teamManager.setPrice(30);
        assertEquals(teamManager.getPrice(),30,1);

    }

    @Test
    public void getTeams() {
        assertEquals(teamManager.getTeams().size(),0);
    }


    @Test
    public void addTeam() {
        teamManager.addTeam(team);
        assertEquals(teamManager.getTeams().size(),1);
    }

    @Test
    public void removeTeam() {
        teamManager.addTeam(team);
        assertEquals(teamManager.getTeams().size(),1);
        teamManager.removeTeam(team);
        assertEquals(teamManager.getTeams().size(),0);
    }

    @Test
    public void isPermissionManageAssets(){
        assertFalse(teamManager.isPermissionManageAssets());
    }

    @Test
    public void isPermissionFinance(){
        assertFalse(teamManager.isPermissionFinance());
    }
    @Test
    public void isActive() {
        assertTrue(teamManager.isActive());
    }

    @Test
    public void reactivate() {
        teamManager.reactivate();
        assertTrue(teamManager.isActive());

    }

    @Test
    public void myRole() {
        assertEquals(teamManager.myRole(),"TeamManager");
    }
}