package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class RefereeTest {
    FootballManagementSystem system;
    Game game;
    Referee referee;
    LeagueInSeason league;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(false);
        //String  leagueId = system.dataReboot();
        league =system.getDatabase().getAllLeaguesInSeasons().get(0);
        game= Database.getGame(league.getGamesId().get(1));
        referee=game.getMainReferee();
        system.getDatabase().getAllUnions().get(0).changeGameDate(game.getId(), new Date());
    }

    @Test
    public void getTraining() {
        assertEquals(game.getSideReferees().get(0).getTraining(),"referees");
    }

    @Test
    public void setTraining() {
        referee.setTraining(Referee.TrainingReferee.var);
        assertEquals(referee.getTraining(),"var");
    }

    @Test
    public void addGame() {
        assertFalse(referee.addGame(game.getId()));
        assertNotNull(referee.viewGames().size());
    }

    @Test
    public void viewGames() {
        assertNotNull(referee.viewGames().size());

    }

    @Test
    public void addEventToGame() {
        assertTrue(referee.addEventToGame(game.getId(),Event.EventType.RedCard,game.getHostTeam().getPlayers().get(0).getID(), game.getHostTeam().getID()));
    }

    @Test
    public void changeEvent() {
       assertTrue( referee.changeEvent(game.getId(),game.getEventReport().getEvents().get(0).getId(),"Disqualified"));
       assertEquals(game.getEventReport().getEvents().get(0).getDescription(),"Disqualified");
    }

    @Test
    public void setScoreInGame() {
        assertTrue(referee.setScoreInGame(game.getId(),3,2));
    }


    @Test
    public void getAllOccurringGame(){
        assertNotNull(game.getSideReferees().get(0).getAllOccurringGame());

    }

    @Test
    public void getGameReport(){
        assertNotNull(referee.getGameReport(game.getId()));

    }
}