package IntegrationTest;

import Data.Database;
import Domain.*;
import Service.*;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class DomainIntegration {


    TeamManagementSystem teamManagementSystem;
    UserSystem userSystem;
    GuestSystem guestSystem;
    AdminSystem adminSystem;
    FinanceTransactionsSystem financeTransactionsManagement;
    UnionRepresentativeSystem unionRepresentativeSystem;
    PersonalPageSystem personalPageSystem;
    FootballManagementSystem system;
    RefereeSystem refereeSystem;
    Admin admin;
    Team team;
    User userTeamOwner;


    @Before
    public void init(){
        teamManagementSystem=new TeamManagementSystem();
        userSystem=new UserSystem();
        guestSystem=new GuestSystem();
        adminSystem=new AdminSystem();
        refereeSystem=new RefereeSystem();
        system = new FootballManagementSystem();
        unionRepresentativeSystem=new UnionRepresentativeSystem();
        financeTransactionsManagement= new FinanceTransactionsSystem();
        personalPageSystem=new PersonalPageSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        admin = (Admin) system.getAdmin();
        userTeamOwner= admin.addNewTeamOwner("team", "owner", "teamOwner@gmail.com");
        userTeamOwner= team.getTeamOwners().get(0);
    }

    @Test
    public void AssetManagement_Test()
    {
       Field field = new Field("Tel-Aviv","Bloomfield", 150000, 125000);
       Database.addAsset(field);
       assertTrue(teamManagementSystem.addField(userTeamOwner.getID(),field.getID(),team.getID()));
        assertEquals(2, team.getFields().size());
        teamManagementSystem.updateAsset(userTeamOwner.getID(),"Field",field.getID(),"Price","30");
        assertEquals(30, field.getPrice(), 0);
        teamManagementSystem.removeField(userTeamOwner.getID(),field.getID(),team.getID());
        assertEquals(1, team.getFields().size());

    }

    @Test
    public void ComplaintManager_Test()
    {

        userSystem.addComplaint(userTeamOwner.getID(),"shalom");

        List<String> comps = adminSystem.getAllActiveComplaints(admin.getID());
        boolean found = false;

        for (String c: comps)
            if (c.equals("shalom"))
                found = true;

        assertTrue(found);
    }

    @Test
    public void EditPersonalInfo_Test()
    {
        User mesiU = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);

        userSystem.editPersonalInfo(mesiU.getID(), "changed1", "changed1");
        assertEquals(mesiU.getName(),"changed1 changed1");
    }

    @Test
    public void FinanceTransactionsManagement_Test()
    {
        double firstBalance = financeTransactionsManagement.getBalance(userTeamOwner.getID(),team.getID());
        financeTransactionsManagement.reportNewIncome(userTeamOwner.getID(),team.getID(),300);
        assertEquals(firstBalance+300, team.getBudget().getBalance(), 0);

        financeTransactionsManagement.reportNewExpanse(userTeamOwner.getID(),team.getID(),300);
        assertEquals(firstBalance, team.getBudget().getBalance(), 0);

        double balance = financeTransactionsManagement.getBalance(userTeamOwner.getID(),team.getID());
        assertEquals(firstBalance, balance, 0);
    }

    @Test
    public void LeagueAndGameManagement_Test()
    {
        User UnionRep= UserFactory.getNewUnionRepresentative("aa", "bb", "aa@bb.com");;
        unionRepresentativeSystem.configureNewLeague(UnionRep.getID(), "5","level1");
        Date date = new Date(2021, 5, 5);
        unionRepresentativeSystem.configureNewSeason(UnionRep.getID(),2021, date);
        Database database = system.getDatabase();
        League l1 = database.getLeague("TestLeagueFAILED");
        League l2 = database.getLeague("5");

        assertNull(l1);
        assertNotNull(l2);

        Season s1 = database.getSeason("2021");
        Season s2 = database.getSeason("3000");

        assertNotNull(s1);
        assertNull(s2);

        String lis1 = unionRepresentativeSystem.configureLeagueInSeason(UnionRep.getID(),"Haal", "5000", "PlayOnceWithEachTeamPolicy", "StandardScorePolicy", 50);
        String lis2 = unionRepresentativeSystem.configureLeagueInSeason(UnionRep.getID(),"Haal", "2021","PlayOnceWithEachTeamPolicy", null, 50);
        String lis3 = unionRepresentativeSystem.configureLeagueInSeason(UnionRep.getID(),"Haal", "2021", null, "StandardScorePolicy", 50);
        String lis4 = unionRepresentativeSystem.configureLeagueInSeason(UnionRep.getID(),"Haal", "2021", "PlayOnceWithEachTeamPolicy", "StandardScorePolicy", 50);

        assertNull(lis1);
        assertNull(lis2);
        assertNull(lis3);
        assertNotNull(lis4);
    }

   @Test
    public void PersonalPageManagement_Test()
    {
        User mesiU = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        Role pageRole = mesiU.checkUserRole("HasPage");
        PersonalPage mesiPage = ((HasPage) pageRole).getPage();
        assertTrue(personalPageSystem.uploadToPage(mesiU.getID(),"shalom"));
        assertEquals(personalPageSystem.viewPage(mesiU.getID(),mesiPage.getId()),"PP1672436222,mesi mesi,This is mesi mesi's Personal Page!  shalom");
    }

    @Test
    public void RefereeManagement_Test()
    {
        User UnionRep= UserFactory.getNewUnionRepresentative("aa", "bb", "aa@bb.com");;
        String ref = unionRepresentativeSystem.appointReferee(UnionRep.getID(),"ref1","ref1","ref1@gmail.com","var");
        String ref2 = unionRepresentativeSystem.appointReferee(UnionRep.getID(),"ref1","ref1","ref1@gmail.com","var");

        assertNotNull(ref);
        assertNull(ref2);
    }

    @Test
    public void UserManagement_Test()
    {
        Guest guest = new Guest();
        User user = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        String userID=guestSystem.logIn("fan@gmail.com","Aa1234");
        assertNotNull(userID);

        String user2=guestSystem.logIn("fan2@gmail.com","Aa1234");
        assertNull(user2);

        User user3 = guest.register("fan@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        assertNull(user3);

        User user4 = guest.register("fan4@gmail.com", "Aa1234", "fan", "fan", "0500001234", "yosef23");
        assertNotNull(user4);

    }



}
