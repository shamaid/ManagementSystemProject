package AcceptanceTest;

import Data.Database;
import Domain.*;
import Service.AdminSystem;
import Service.FootballManagementSystem;
import Service.ProxyAccountingSystem;
import Service.UnionRepresentativeSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SeasonInitiationTest {

    private FootballManagementSystem system;
    private User UnionRep;
    private AdminSystem adminSystem;
    private Admin admin;
    private ProxyAccountingSystem accountingSystem;
    @Before
    public void init()
    {
        adminSystem=new AdminSystem();
        system = new FootballManagementSystem();
        system.systemInit(true);
        UnionRep = UserFactory.getNewUnionRepresentative("aa", "bb", "aa@bb.com");
        admin = (Admin) system.getAdmin();
        accountingSystem = new ProxyAccountingSystem();
        accountingSystem.connect();
    }

    @Test
    public void defineLeagueTest_55()
    {

        UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2021,Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Alufot", "level1");
        String leagueInSeason = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Alufot", "2021",  "PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 250);

        String ref = FootballManagementSystem.mainReferee(UnionRep);
        String ref2 = FootballManagementSystem.mainReferee(UnionRep);
        String ref3 = FootballManagementSystem.mainReferee(UnionRep);

        representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeason, ref);
        representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeason, ref2);
        representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeason, ref3);


        League league = Database.getLeague("Alufot");
        assertNotNull(league);

        boolean success = representativeSystem.assignGames(UnionRep.getID(),leagueInSeason);
        assertFalse(success);
    }

    @Test
    public void defineLeagueTest_56()
    {

        UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2021,Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Alufot", "level1");
        String leagueInSeason = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Alufot", "2021", null, null, 250);

        assertNull(leagueInSeason);
    }

    @Test
    public void defineLeagueTest_57()
    {

        UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),1800, Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Alufot", "level1");
        String leagueInSeason = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Alufot", "1800","PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 250);

        assertNull(leagueInSeason);
    }

    @Test
    public void defineLeagueTest_58()//needs 14 teams to run
    {

        UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2020,Database.getCurrentDate());
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
                team.addIncome(1000000000);
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
    public void setReferees_65()
    {

       UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2021 ,Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Alufot", "level1");
        String leagueInSeason = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Alufot", "2021",  "PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 300);

        String ref = FootballManagementSystem.mainReferee(UnionRep);

        representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeason, ref);
        assertNotNull(ref);

    }

    @Test
    public void setReferees_66()
    {

       UnionRepresentativeSystem representativeSystem = system.getUnionRepresentativeSystem();
        representativeSystem.configureNewSeason(UnionRep.getID(),2021, Database.getCurrentDate());
        representativeSystem.configureNewLeague(UnionRep.getID(),"Alufot", "level1");
        String leagueInSeason = representativeSystem.configureLeagueInSeason(UnionRep.getID(),"Alufot", "2021","PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 300);

        String ref = FootballManagementSystem.mainReferee(UnionRep);


        representativeSystem.assignRefToLeague(UnionRep.getID(),leagueInSeason, ref);

        boolean success = representativeSystem.assignGames(UnionRep.getID(),leagueInSeason);

        assertFalse(success);

    }

}
