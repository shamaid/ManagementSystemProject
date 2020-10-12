package UnitTest;

import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuestTest {
    FootballManagementSystem system;
    Guest guest;

    @Before
    public void init() {
        system = new FootballManagementSystem();
        system.systemInit(false);
        guest = new Guest();
    }

    @Test
    public void search() {
        assertNotNull(guest.search("mesiPage"));
    }

    @Test
    public void register() {
        assertNotNull(guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23"));

    }

    @Test
    public void login() { assertNotNull(guest.login("fan@gmail.com","Aa1234"));
    }

    @Test
    public void viewInfoAboutTeams() {
        assertNotNull(guest.viewInfoAboutTeams());
    }

    @Test
    public void viewInfoAboutPlayers() {
        assertNotNull(guest.viewInfoAboutPlayers());
    }

    @Test
    public void viewInfoAboutCoaches() {
        assertNotNull(guest.viewInfoAboutCoaches());
    }

    @Test
    public void viewInfoAboutLeagues() {
        assertNotNull(guest.viewInfoAboutLeagues());
    }

    @Test
    public void viewInfoAboutSeasons() {
        assertNotNull(guest.viewInfoAboutSeasons());
    }

    @Test
    public void viewInfoAboutReferees() {
        assertNotNull(guest.viewInfoAboutReferees());
    }
}