package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TeamTest {
    FootballManagementSystem system;
    Team team;
    Admin admin;
    LeagueInSeason league;
    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        admin = (Admin) system.getAdmin();

    }
    @Test
    public void addLeague() {
        User union = admin.addNewUnionRepresentative("Union", "Rep", "unionRep@gmail.com");
        UnionRepresentative unionRole = ((UnionRepresentative)union.checkUserRole("UnionRepresentative"));
        unionRole.configureNewSeason(2021, Database.getDate(2020, 5, 1));
        LeagueInSeason leagueInSeason = unionRole.configureLeagueInSeason("Haal", "2021","PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 300);
        assertTrue(team.addLeague(leagueInSeason));
    }

    @Test
    public void addAWin() {
        int wins = team.getWins();
        team.addAWin();
        assertEquals(wins+1, team.getWins());
    }

    @Test
    public void addALoss() {
        int losses = team.getLosses();
        team.addALoss();
        assertEquals(losses+1, team.getLosses());
    }

    @Test
    public void addADraw() {
        int draws = team.getDraws();
        team.addADraw();
        assertEquals(draws+1, team.getDraws());
    }

    @Test
    public void addTeamOwner() {
        User userTeamOwner = admin.addNewTeamOwner("Team", "Owner", "teamOwner@gmail.com");
        team.addTeamOwner(userTeamOwner,true);
        assertNotNull(userTeamOwner.checkUserRole("TeamOwner"));
    }

    @Test
    public void addTeamManager() {
        User userTeamManager = admin.addNewTeamManager("Team", "Manager", "teamManager@gmail.com", 10000, true, false);
        team.addTeamManager(userTeamManager, 10000, true, false);
        assertNotNull(userTeamManager.checkUserRole("TeamManager"));
    }

    @Test
    public void addPlayer() {
        User user = admin.addNewPlayer("p", "Player", "p@gmail.com" ,Database.getDate(1999, 10, 10), Player.RolePlayer.playerBack, 1000);
        assertTrue(team.addPlayer(user));

    }

    @Test
    public void addCoach() {
        User user = admin.addNewCoach("c", "Coach", "c@gmail.com" , Coach.TrainingCoach.UEFA_PRO , Coach.RoleCoach.assistantCoach,1000);
        assertTrue(team.addCoach(user));
    }

    @Test
    public void addGame() {
        Team team0 = league.getTeams().get(1);
        Field field = new Field("Tel-Aviv","Bloomfield", 10000, 150000);
        Referee mainReferee = league.getReferees().get(0);
        List<Referee> sideReferees = new LinkedList<>();
        sideReferees.add(league.getReferees().get(1));
        sideReferees.add(league.getReferees().get(2));
        Game game = new Game(Database.getDate(2020, 5, 25, 20, 0), field, mainReferee, sideReferees, team, team0, league);
        assertTrue(team.addGame(game));
    }

    @Test
    public void removeTeamOwner() {
        User userTeamOwner = admin.addNewTeamOwner("Team", "Owner", "teamOwner@gmail.com");
        team.addTeamOwner(userTeamOwner,true);
        assertTrue(team.removeTeamOwner(userTeamOwner));
        /*for notification*/
        assertEquals(userTeamOwner.getMessageBox().size(), 2);
    }

    @Test
    public void removeTeamManager() {
        User userTeamManager = admin.addNewTeamManager("Team", "Manager", "teamManager@gmail.com", 10000, true, false);
        team.addTeamManager(userTeamManager, 10000, true, false);
        assertTrue(team.removeTeamManager(userTeamManager));
        /*for notification*/
        assertEquals(userTeamManager.getMessageBox().size(), 2);
    }

    @Test
    public void removePlayer() {
        User user = admin.addNewPlayer("p", "Player", "p@gmail.com" ,Database.getDate(1999, 10, 10), Player.RolePlayer.playerBack, 1000);
        team.addPlayer(user);
        assertTrue(team.removePlayer(user));
    }

    @Test
    public void removeCoach() {
        User user = admin.addNewCoach("c", "Coach", "c@gmail.com" , Coach.TrainingCoach.UEFA_PRO , Coach.RoleCoach.assistantCoach,1000);
        team.addCoach(user);
        assertTrue(team.removeCoach(user));
    }

    @Test
    public void getName() {
        assertEquals(team.getName(), "team0");
    }

    @Test
    public void getWins() {
        team.addAWin();
        assertEquals(team.getWins(), 1);
    }

    @Test
    public void getLosses() {
        team.addALoss();
        assertEquals(team.getLosses(), 1);
    }

    @Test
    public void getDraws() {
        team.addADraw();
        assertEquals(team.getDraws(), 1);
    }

    @Test
    public void getPage() {
        assertNotNull(team.getPage());
    }

    @Test
    public void getTeamOwners() {
        assertEquals(team.getTeamOwners().size(), 1);
    }

    @Test
    public void getTeamManagers() {
        assertEquals(team.getTeamManagers().size(), 0);
    }

    @Test
    public void getPlayers() {
        assertEquals(team.getPlayers().size(), 12);
    }

    @Test
    public void getCoaches() {
        assertEquals(team.getCoaches().size(), 1);
    }

    @Test
    public void getBudget() {
        assertNotNull(team.getBudget());
    }

    @Test
    public void getGames() {
        assertEquals(team.getGamesId().size(), 0);
    }

    @Test
    public void getFields() {
        assertEquals(team.getFields().size(), 1);
    }

    @Test
    public void getField() {
        assertNotNull(team.getField());
    }

    @Test
    public void isActive() {
        assertTrue(team.isActive());
    }

    @Test
    public void setActive() {
        team.setActive(false);
        assertFalse(team.isActive());
        /*for notification*/
        assertEquals(team.getTeamOwners().get(0).getMessageBox().size(), 2);
        assertEquals(admin.getMessageBox().size(), 1);
    }

    @Test
    public void isPermanentlyClosed() {
        assertFalse(team.isPermanentlyClosed());
    }

    @Test
    public void setPermanentlyClosed() {
        team.setPermanentlyClosed(true);
        assertTrue(team.isPermanentlyClosed());
        /*for notification*/
        assertEquals(team.getTeamOwners().get(0).getMessageBox().size(), 2);
        assertEquals(admin.getMessageBox().size(), 1);
    }

    @Test
    public void getID() {
        assertNotNull(team.getID());
    }

    @Test
    public void getPrice() {
        assertNotNull(team.getPrice());
    }

    @Test
    public void addField() {
        Field field = new Field("Tel-Aviv","Bloomfield", 10000, 150000);
        assertTrue(team.addField(field));
    }

    @Test
    public void removeField() {
        assertFalse(team.removeField(team.getField()));
        Field field = new Field("Tel-Aviv","Bloomfield", 10000, 150000);
        team.addField(field);
        assertTrue(team.removeField(team.getField()));
    }

    @Test
    public void AllDetailsAboutTeam(){
        assertNotNull(team.AllDetailsAboutTeam());
    }
}