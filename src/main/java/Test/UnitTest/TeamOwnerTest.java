package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeamOwnerTest {
    FootballManagementSystem system;
    LeagueInSeason league;
    TeamOwner teamOwner;
    TeamManager teamManager;
    Admin admin;
    Team team;
    User user;


    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(false);
        //String  leagueId = system.dataReboot();

        league =system.getDatabase().getAllLeaguesInSeasons().get(0);

        admin = system.getAdmin();
        team = league.getTeams().get(0);
        teamOwner = system.getDatabase().getAllTeamOwners().get(0);


        //user=admin.addNewTeamManager("team", "management", "manage@gmail.com", 1200, false, false);
        /*if(user==null) {
            String userId = system.getDatabase().getAllTeamManagers().get(0).getID();
            user = system.getDatabase().getUser(userId);
        }*/
    }

    @Test
    public void getAppointedTeamOwners() {

        assertNotNull(teamOwner.getAppointedTeamOwners());
    }

    @Test
    public void getAppointedTeamManagers() {
        assertNotNull(teamOwner.getAppointedTeamManagers());
    }

    @Test
    public void addTeam() {
        int listSize = teamOwner.getTeamsToManage().size();
        Team team1 = league.getTeams().get(1);
        teamOwner.addTeam(team1);
        assertEquals(teamOwner.getTeamsToManage().size(),listSize+1);

        teamOwner.removeTeam(team1);
    }

    @Test
    public void addTeamPersonalPage() {

        Team team1 = teamOwner.getTeamsToManage().get(0);
        assertTrue(teamOwner.addTeamPersonalPage(team1));

    }

    @Test
    public void getTeamsToManage() {
        assertNotNull(teamOwner.getTeamsToManage());
    }


    @Test
    public void getTeamById() {
        teamOwner.addTeam(team);
        assertNotNull(teamOwner.getTeamById(team.getID()));
        teamOwner.removeTeam(team);

    }

    @Test
    public void removeTeam() {
        int listSize = teamOwner.getTeamsToManage().size();
        Team team1 = league.getTeams().get(1);
        teamOwner.addTeam(team1);
        assertEquals(teamOwner.getTeamsToManage().size(),listSize+1);
        teamOwner.removeTeam(team1);
        assertEquals(teamOwner.getTeamsToManage().size(),listSize);
    }

    @Test
    public void appointTeamOwner() {
        user=admin.addNewTeamManager("team", "management", "manage5@gmail.com", 1200, false, false);
        teamOwner.addTeam(team);
        assertTrue(teamOwner.appointTeamOwner(user,team.getID()));
        teamOwner.removeAppointTeamOwner(user,team.getID());
        teamOwner.removeTeam(team);
        system.getDatabase().removeFromTables(user.getID());
    }

    @Test
    public void appointTeamManager() {
        user=admin.addNewTeamManager("team", "management", "manage1@gmail.com", 1200, false, false);
        teamOwner.addTeam(team);
        assertTrue(teamOwner.appointTeamManager(user,team.getID(),30,false,false));
        teamOwner.removeAppointTeamManager(user,team.getID());
        teamOwner.removeTeam(team);
        system.getDatabase().removeFromTables(user.getID());

    }

    @Test
    public void removeAppointTeamOwner() {
        user=admin.addNewTeamManager("team", "management", "manage2@gmail.com", 1200, false, false);
        teamOwner.addTeam(team);
        teamOwner.appointTeamOwner(user,team.getID());
        assertTrue(teamOwner.removeAppointTeamOwner(user,team.getID()));
        teamOwner.removeTeam(team);
        system.getDatabase().removeFromTables(user.getID());
    }

    @Test
    public void removeAppointTeamManager() {
        user=admin.addNewTeamManager("team", "management", "manage3@gmail.com", 1200, false, false);
        teamOwner.addTeam(team);
        teamOwner.appointTeamManager(user,team.getID(),200,true,true);
        assertTrue(teamOwner.removeAppointTeamManager(user,team.getID()));
        teamOwner.removeTeam(team);
        system.getDatabase().removeFromTables(user.getID());

    }

    @Test
    public void closeTeam() {

        teamOwner.addTeam(team);
        assertTrue(teamOwner.closeTeam(team.getID()));
        teamOwner.removeTeam(team);
    }

    @Test
    public void reopenTeam() {
        teamOwner.closeTeam(team.getID());
        assertTrue(teamOwner.reopenTeam(team.getID()));

    }

    @Test
    public void myRole() {
        assertEquals(teamOwner.myRole(),"TeamOwner");
    }

    @Test
    public void createTeam() {

    }
}