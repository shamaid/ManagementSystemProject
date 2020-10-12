package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BudgetTest {

    FootballManagementSystem system;
    Team team;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(false);

        LeagueInSeason league =system.getDatabase().getAllLeaguesInSeasons().get(0);
        team = league.getTeams().get(0);
    }
    @Test
    public void addIncome() {
        double incomeTeam = team.getBudget().getIncome();
        team.addIncome(1500);
        assertEquals(team.getBudget().getIncome(), incomeTeam+1500, 0);
    }

    @Test
    public void addExpanse() {
        double expanseTeam = team.getBudget().getExpanses();
        team.addExpanse(1000);
        assertEquals(team.getBudget().getExpanses(), expanseTeam+1000, 0);
        team.addExpanse(-1000);
    }

    @Test
    public void getBalance() {
        assertEquals(team.getBudget().getBalance(),team.getBudget().getIncome()-team.getBudget().getExpanses() ,0);
    }

    @Test
    public void getExpanses() {
        assertEquals(team.getBudget().getExpanses(), -(team.getBudget().getBalance()-team.getBudget().getIncome()), 0);
    }

    @Test
    public void getIncome() {
        assertEquals(team.getBudget().getIncome(), team.getBudget().getBalance()+team.getBudget().getExpanses(),0);
    }
}