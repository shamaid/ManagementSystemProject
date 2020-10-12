package AcceptanceTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import Service.UserSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

public class GameNotificationsTest {

    FootballManagementSystem system;
    User user;
    User mesi;
    PersonalPage mesiPage;
    Fan fan;
    UserSystem userSystem;
    Game game;

    @Before
    public void init() {
        userSystem = new UserSystem();
        system = new FootballManagementSystem();
        system.systemInit(true);
        Admin admin = (Admin) system.getAdmin();
        Guest guest = new Guest();
        user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        mesi = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesi.checkUserRole("HasPage");
        mesiPage = ((HasPage) pageRole).getPage();
        fan = (Fan) user.checkUserRole("Fan");
        LeagueInSeason league = Database.getLeagueInSeason(system.dataReboot());
        //game = Database.getAllFutureGames().get(0);

    }
    @Test
    public void gameNotifications_11(){
        List<String > games=new LinkedList<>();
        games.add(game.getId());
        assertTrue(userSystem.registrationForGamesAlerts(user.getID(),games,false));
    }
    @Test
    public void gameNotifications_12(){
        List<String > games=new LinkedList<>();
        games.add(game.getId());
        userSystem.registrationForGamesAlerts(user.getID(),games,false);
        assertFalse(userSystem.registrationForGamesAlerts(user.getID(),games,false));
    }

}
