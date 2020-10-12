package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

public class GameAssignmentPolicyTest {
    Game game;
    FootballManagementSystem system;
    User user;
    Fan fan;
    Team team0;
    Team team1;

    @Before
    public void init(){
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        Admin admin = (Admin) system.getAdmin();
        User union = admin.addNewUnionRepresentative("Union", "Rep", "unionRep@gmail.com");
        UnionRepresentative unionRole = ((UnionRepresentative)union.checkUserRole("UnionRepresentative"));
        unionRole.assignGames(league.getId());
        game= Database.getGame(league.getGamesId().get(0));
    }

    @Test
    public void checkForDuplicates() {

    }

    @Test
    public void getSideReferees() {
    }

    @Test
    public void getMainReferee() {
    }

    @Test
    public void getDateFromList() {
    }

    @Test
    public void checkConstrains() {
    }

    @Test
    public void makeGame() {
    }
}