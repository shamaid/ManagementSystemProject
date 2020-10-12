package AcceptanceTest;

import Data.Database;
import Domain.*;
import Domain.Player;
import Service.FootballManagementSystem;
import Service.TeamManagementSystem;
import Service.UserSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TeamManagementTest {

        TeamManagementSystem teamManagementSystem;
        UserSystem userSystem;
        TeamOwner teamOwner;
        FootballManagementSystem system;
        Admin admin;
        Team team;
        User userTeamOwner;
       User mesiU;
       Player mesi;
       User manager;

    @Before
        public void init(){
        teamManagementSystem=new TeamManagementSystem();
        userSystem=new UserSystem();
        system = new FootballManagementSystem();
        system.systemInit(true);
        String  leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        admin = (Admin) system.getAdmin();
        userTeamOwner= admin.addNewTeamOwner("team", "owner", "teamOwner@gmail.com");
        teamOwner = (TeamOwner) userTeamOwner.checkUserRole("TeamOwner");
        userTeamOwner= team.getTeamOwners().get(0);
        mesiU = admin.addNewPlayer("mesi", "mesi", "mesi@mail.com", new Date(30 / 5 / 93), Player.RolePlayer.goalkeeper, 200000);
        mesi=(Player) mesiU.checkUserRole("Player");
        manager=admin.addNewTeamManager("Maor","Buzaglo","maor@gmail.com",3000,true,true);
        }



    @Test
    public void manageAssets_26()
    {
        teamManagementSystem.addAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID());
        assertFalse(teamManagementSystem.addAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID()));
    }

    @Test
    public void manageAssets_27()
    {
        teamManagementSystem.addAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID());
       assertTrue(teamManagementSystem.removeAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID()));

    }

    @Test
    public void manageAssets_28()
    {
        teamManagementSystem.addAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID());
        //teamManagementSystem.updateAsset(userTeamOwner.getID(),mesiU.getID(),"Price","30");

    }

    @Test
    public void manageAssets_31()
    {
        assertTrue(teamManagementSystem.addAssetPlayer(userTeamOwner.getID(),mesiU.getID(),team.getID()));
        assertEquals(mesi.getTeams().size(),1);
    }

    @Test
    public void appointManager_32()
    {
        assertEquals(team.getTeamManagers().size(),0);
        assertTrue(teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false));
        assertEquals(team.getTeamManagers().size(),1);
    }

    @Test
    public void appointManager_33()
    {
      teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false);
      assertFalse(teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false));

    }

    @Test
    public void closeTeam_34()
    {
    assertTrue(teamManagementSystem.closeTeam(userTeamOwner.getID(),team.getID()));

    }

    @Test
    public void closeTeam_35() {

        assertEquals(teamManagementSystem.getTeams(userTeamOwner.getID()).size(), 1);
        teamManagementSystem.closeTeam(userTeamOwner.getID(), team.getID());
        assertEquals(teamManagementSystem.getTeams(userTeamOwner.getID()).size(), 0);
        teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false);
    }




    @Test
    public void openTeam_37()
    {
    assertFalse(teamManagementSystem.reOpeningTeam(userTeamOwner.getID(), team.getID()));
    }

    @Test
    public void openTeam_38()
    {
        teamManagementSystem.closeTeam(userTeamOwner.getID(), team.getID());
        assertTrue(teamManagementSystem.reOpeningTeam(userTeamOwner.getID(), team.getID()));

    }

    @Test
    public void removeAsset_46()
    {
        teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false);
        assertTrue(teamManagementSystem.removeAppointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID()));
    }

    @Test
    public void removeAsset_47()
    {
        User userTeamOwner2= admin.addNewTeamOwner("team2", "owner2", "teamOwner2@gmail.com");
        teamManagementSystem.appointmentTeamOwner(userTeamOwner.getID(),userTeamOwner2.getID(),team.getID());
        teamManagementSystem.appointmentTeamManager(userTeamOwner.getID(),manager.getID(),team.getID(),200,false,false);
        assertFalse(teamManagementSystem.removeAppointmentTeamManager(userTeamOwner2.getID(),manager.getID(),team.getID()));

    }


}
