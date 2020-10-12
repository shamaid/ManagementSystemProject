package IntegrationTest;

import Data.Database;
import Domain.*;
import Service.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceIntegration {

    private FootballManagementSystem system;
    private AdminSystem adminSystem;
    private UnionRepresentativeSystem representativeSystem;
    private GuestSystem guestSystem;
    private UserSystem userSystem;
    private NotificationSystem notSystem;
    private Admin admin;
    private String unionRep;
    private Team team;
    private User userTeamOwner;

    @Before
    public void init()
    {
        system = new FootballManagementSystem();
        system.systemInit(true);
        adminSystem = system.getAdminSystem();
        //system.dataReboot();
        guestSystem = system.getGuestSystem();
        userSystem = system.getUserSystem();
        representativeSystem = system.getUnionRepresentativeSystem();
        notSystem = system.getNotificationSystem();
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        //admin = Database.getSystemAdmins().get(0);
        admin = (Admin) system.getAdmin();
        userTeamOwner= admin.addNewTeamOwner("team", "owner", "teamOwner@gmail.com");
        userTeamOwner= team.getTeamOwners().get(0);

    }

    @Test
    public void integration_AdminSystem()
    {

        String user = adminSystem.addNewCoach(admin.getID(),"First", "Last", "a@b.com", "IFA_C", "main", 50);
        assertNotNull(user);
        assertNotNull(adminSystem.permanentlyCloseTeam(admin.getID() ,team.getID()));
    }

    @Test
    public void integration_FinanceSystem()
    {

        FinanceTransactionsSystem finSystem = system.getFinanceTransactionsSystem();
        assertTrue(finSystem.reportNewExpanse(userTeamOwner.getID(), team.getID(), 100));

        double amount = finSystem.getBalance(userTeamOwner.getID(), team.getID());
        finSystem.reportNewIncome(userTeamOwner.getID(), team.getID(), 100);
        finSystem.reportNewExpanse(userTeamOwner.getID(), team.getID(), 100);
        double afterChange = finSystem.getBalance(userTeamOwner.getID(), team.getID());

        assertEquals(amount, afterChange, 0);

    }

    @Test
    public void integration_GuestSystem()
    {

        String registered = guestSystem.register("a@b.com", "ABb123", "First", "Last", "0123456789", "Israel");
        assertNotNull(registered);

        String user = guestSystem.logIn("a@b.com", "ABb123");
        assertNotNull(user);

        assertNotNull(userSystem.logOut());

        List<String> searchResults = guestSystem.search( "TestNotFoundWord");
        assertNotNull(searchResults);
        assertEquals(0, searchResults.size());

    }


    @Test
    public void integration_UnionRepresentativeSystem()//we need 14 teams to assign games
    {
        User UnionRep=UserFactory.getNewUnionRepresentative("aa", "bb", "aa@bb.com");
        UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2020, Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Haal", "level1");
        String leagueInSeasonId = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Haal", "2020", "PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 300);
        representativeSystem.addFieldToSystem(UnionRep.getID(),"jerusalem","Teddy" ,550, 150000);
        Field field = (Field) Database.getAllFields().get(0);
        Team team;
        for (int i = 0; i < 14; i++) {
            List<String> players = system.createPlayers();
            List<String> coaches =system.createCoaches();
            List<User> owners = new LinkedList<>();

            String ownerId = adminSystem.addNewTeamOwner(system.getAdmin().getID(),"owner","owner","to"+i+"@gmail.com");
            User owner = UserFactory.getUser(ownerId);
            if(owner!=null){
                owners.add(owner);
                TeamOwner teamOwner = (TeamOwner)owner.checkUserRole("TeamOwner");
                teamOwner.createTeam(owner,"team"+i, players, coaches, field.getID());
                team = teamOwner.getTeamsToManage().get(0);
                team.getBudget().addIncome(1000000000);
                representativeSystem.addTeamToLeague(UnionRep.getID(),leagueInSeasonId, team.getID(), new ProxyAccountingSystem());
            }

        }
        for (int i = 0; i <10 ; i++) {
            String refId = FootballManagementSystem.mainReferee(UnionRep);
            representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeasonId, refId);
        }


        League league = Database.getLeague("Haal");
        assertNotNull(league);

        boolean success = representativeSystem.assignGames(UnionRep.getID(),leagueInSeasonId);
        assertTrue(success);

    }

    @Test
    public void integration_UserSystem()
    {


        String user = guestSystem.register("a@b.com", "As123456", "first", "last", "123456789", "Israel");
        assertNotNull(user);

        List<String> pages = userSystem.getFanPages(user);

        String keyword = "test check";
        userSystem.search(user, keyword);
        List<String> history = userSystem.viewSearchHistory(user);
        assertEquals(1, history.size());


    }

   /* @Test
    public void integration_NotificationSystem()
    {
        representativeSystem.configureNewSeason(2020, new Date(120,4,1 ));
        representativeSystem.configureNewLeague("Haal", "3");
        LeagueInSeason leagueInSeason = representativeSystem.configureLeagueInSeason("Haal", "2020", new PlayTwiceWithEachTeamPolicy(), new StandardScorePolicy(), 300);
        List<Player> players = FootballManagementSystem.createPlayers();
        List<Coach> coaches = FootballManagementSystem.createCoaches();

        TeamOwner owner = new TeamOwner("Team","Owner", "a"+"@gmail.com");
        Database database = system.getDatabase();
        List<User> owners = new LinkedList<>();
        owners.add(owner);
        PersonalPage page = new PersonalPage("", players.get(0));
        Field field = new Field( "jerusalem", 550, 150000);
        Team team = new Team("team",page,owners,players,coaches, field);
        database.addTeam(team);
        team.setActive(false);
        representativeSystem.addTeamToLeague(leagueInSeason, team);


        boolean success = notSystem.openORCloseTeam("closed", team, false);
        assertTrue(success);

        team.setActive(true);

        success = notSystem.openORCloseTeam("open", team, false);
        assertTrue(success);


    }*/
}
