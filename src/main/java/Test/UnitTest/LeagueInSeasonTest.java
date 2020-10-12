package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LeagueInSeasonTest {
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
    public void changeRegistrationFee() {
        leagueInSeason.changeRegistrationFee(30);
        assertEquals(leagueInSeason.getRegistrationFee(),30,1);
    }

    @Test
    public void getTeams() {
        assertEquals(leagueInSeason.getTeams().size(),14);
    }

    @Test
    public void setTeams() {
        Team team0 = leagueInSeason.getTeams().get(0);
        List<Team> teams=new LinkedList<>();
        teams.add(team0);
        leagueInSeason.setTeams(teams);
        assertEquals(leagueInSeason.getTeams().size(),1);

    }

    @Test
    public void addATeam() {
        Team team0 = leagueInSeason.getTeams().get(0);
leagueInSeason.addATeam(team0);
        assertEquals(leagueInSeason.getTeams().size(),14);

    }

    @Test
    public void setGames() {
        Team team0 = leagueInSeason.getTeams().get(0);
        Team team1 = leagueInSeason.getTeams().get(1);
        Field field = new Field("Tel-Aviv", "Bloomfield", 10000, 150000);
        Referee mainReferee = leagueInSeason.getReferees().get(0);
        List<Referee> sideReferees = new LinkedList<>();
        sideReferees.add(leagueInSeason.getReferees().get(1));
        sideReferees.add(leagueInSeason.getReferees().get(2));
        Game  game = new Game(Database.getDate(2020, 5, 25, 20, 0), field, mainReferee, sideReferees, team0, team1, leagueInSeason);
        List<Game> games=new LinkedList<>();
        games.add(game);
        leagueInSeason.setGamesId(games);
        assertEquals(leagueInSeason.getGamesId().size(),1);

    }

    @Test
    public void addGame() {
        Team team0 = leagueInSeason.getTeams().get(0);
        Team team1 = leagueInSeason.getTeams().get(1);
        Field field = new Field("Tel-Aviv", "Bloomfield", 10000, 150000);
        Referee mainReferee = leagueInSeason.getReferees().get(0);
        List<Referee> sideReferees = new LinkedList<>();
        sideReferees.add(leagueInSeason.getReferees().get(1));
        sideReferees.add(leagueInSeason.getReferees().get(2));
        Game  game = new Game(Database.getDate(2020, 5, 25, 20, 0), field, mainReferee, sideReferees, team0, team1, leagueInSeason);
        leagueInSeason.addGame(game);
        assertEquals(leagueInSeason.getGamesId().size(),1);

    }

    @Test
    public void getScoreTable() {
        assertNotNull(leagueInSeason.getScoreTable());
    }

    @Test
    public void getAllGames() {
        assertEquals(leagueInSeason.getGamesId().size(),0);

    }

    @Test
    public void getGameById() {
        Team team0 = leagueInSeason.getTeams().get(0);
        Team team1 = leagueInSeason.getTeams().get(1);
        Field field = new Field("Tel-Aviv", "Bloomfield", 10000, 150000);
        Referee mainReferee = leagueInSeason.getReferees().get(0);
        List<Referee> sideReferees = new LinkedList<>();
        sideReferees.add(leagueInSeason.getReferees().get(1));
        sideReferees.add(leagueInSeason.getReferees().get(2));
        Game  game = new Game(Database.getDate(2020, 5, 25, 20, 0), field, mainReferee, sideReferees, team0, team1, leagueInSeason);
        leagueInSeason.addGame(game);
        assertNotNull(leagueInSeason.getGamesId());


    }

    @Test
    public void addReferee() {
        Referee mainReferee = leagueInSeason.getReferees().get(0);
        assertFalse(leagueInSeason.addReferee(mainReferee));
    }

    @Test
    public void changeScorePolicy() {
        ScorePolicy policy=leagueInSeason.getScorePolicy();
        assertFalse(leagueInSeason.changeScorePolicy(policy));
    }

    @Test
    public void changeAssignmentPolicy() {
        GameAssignmentPolicy policy=leagueInSeason.getAssignmentPolicy();
        assertFalse(leagueInSeason.changeAssignmentPolicy(policy));

    }

    @Test
    public void getReferees() {
        assertEquals(leagueInSeason.getReferees().size(),10);
    }

    @Test
    public void addScoreTableRecord() {
        Team team0 = leagueInSeason.getTeams().get(0);
        ScoreTableRecord str=new ScoreTableRecord(team0,5);
        leagueInSeason.addScoreTableRecord(str);
        assertEquals(leagueInSeason.getScoreTable().size(),1);
    }

    @Test
    public void getAssignmentPolicy() {
        assertNotNull(leagueInSeason.getAssignmentPolicy());
    }

    @Test
    public void getScorePolicy() {
        assertNotNull(leagueInSeason.getScorePolicy());

    }


    @Test
    public void getLeague() {
        assertEquals(leagueInSeason.getLeague().getName(),"Haal");
    }

    @Test
    public void getSeason() {
        assertEquals(leagueInSeason.getSeason().getYear(),2020);
    }

    @Test
    public void getRegistrationFee() {
        assertEquals(leagueInSeason.getRegistrationFee(),300,1);
    }
}