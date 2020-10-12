package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CupScorePolicyTest {

    FootballManagementSystem system;
    LeagueInSeason league;
    CupScorePolicy score;
    Team team0;
    Team team1;
    Game game;

    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        league = Database.getLeagueInSeason(leagueId);
        score = new CupScorePolicy();
        league.changeScorePolicy(score);
        team0 = league.getTeams().get(0);
        team1 = league.getTeams().get(1);

       Field field = new Field("Tel-Aviv","Bloomfield", 150000, 125000);
        Referee mainReferee= league.getReferees().get(0);
        List<Referee> sideReferees = new LinkedList<>();
        sideReferees.add(league.getReferees().get(1));
        sideReferees.add(league.getReferees().get(2));
        game = new Game(Database.getDate(2020, 5, 25, 20, 0), field, mainReferee, sideReferees ,team0, team1,league);
    }
    @Test
    public void calculateScore() {
        game.setGuestScore(3);
        game.setHostScore(1);
        score.calculateScore(game);
        assertEquals(1,game.getGuestTeam().getWins() ,0);
        assertEquals(0,game.getHostTeam().getWins() ,0);
    }

    @Test
    public void calculateLeagueScore() {

        score.calculateLeagueScore(league);
        assertEquals(league.getTeams().size(),league.getScoreTable().size(),0);
    }
    }