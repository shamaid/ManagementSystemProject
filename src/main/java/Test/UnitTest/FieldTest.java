package UnitTest;

import Data.Database;
import Domain.Field;
import Domain.LeagueInSeason;
import Domain.Team;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {

    FootballManagementSystem system;
    Field field;
    Team team;
    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        field = new Field("Tel-Aviv","Bloomfield", 150000, 125000);
    }

    @Test
    public void getLocation() {
        assertEquals(field.getLocation(),"Tel-Aviv");
    }

    @Test
    public void getCapacity() {
        assertEquals(field.getCapacity(),150000);
    }

    @Test
    public void getGames() {
        assertEquals(field.getGames().size(),0);
    }

    @Test
    public void getID() {
        assertNotNull(field.getID());
    }

    @Test
    public void deactivate() {
        field.deactivate();
        assertFalse(field.isActive());
    }

    @Test
    public void reactivate() {
        field.reactivate();
        assertTrue(field.isActive());
    }

    @Test
    public void getPrice() {
        assertEquals(field.getPrice(),125000,1);
    }

    @Test
    public void setPrice() {
        field.setPrice(150000);
        assertEquals(field.getPrice(),150000,1);
    }

    @Test
    public void getTeams() {
        assertEquals(field.getTeams().size(),0);
    }

    @Test
    public void addTeam() {
        field.addTeam(team);
        assertEquals(field.getTeams().size(),1);
    }

    @Test
    public void removeTeam() {
        field.addTeam(team);
        field.removeTeam(team);
        assertEquals(field.getTeams().size(),0);

    }

    @Test
    public void isActive() {
        assertNotNull(field.isActive());
    }
}