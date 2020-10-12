package UnitTest;

import Data.Database;
import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdminTest {

    FootballManagementSystem system;
    Admin admin;
    Team team;

    @Before
    public void init(){
        system = new FootballManagementSystem();

        system.systemInit(false);
        //String  leagueId = system.dataReboot();
        LeagueInSeason league =system.getDatabase().getAllLeaguesInSeasons().get(0);

        team = league.getTeams().get(0);
        admin = system.getAdmin();
    }

    @Test
    public void closeTeamPermanently() {
        String teamId = team.getID();
        assertNotNull(admin.closeTeamPermanently(teamId));
        assertNull(admin.closeTeamPermanently(team.getID()));
        team.setPermanentlyClosed(false);
        team.setActive(true);
        system.getDatabase().updateObject(team);
    }

    @Test
    public void addNewPlayer() {
        User newUser = admin.addNewPlayer("lionel","mesi","lmesi@gmail.com",Database.getDate(1992, 1, 1), Player.RolePlayer.attackingPlayer,300000);
        assertNotNull(newUser);
    }

    @Test
    public void addNewCoach() {
        User newUser= admin.addNewCoach("coach1", "coach",+IdGenerator.getNewId()+"@gmail.com", Coach.TrainingCoach.UEFA_A, Coach.RoleCoach.main, 1500);
        assertNotNull(newUser);
    }

    @Test
    public void addNewTeamOwner() {
        User newUser=admin.addNewTeamOwner("Team","Owner","to"+30+"@gmail.com" );
        assertNotNull(newUser);
    }

    @Test
    public void addNewTeamManager() {
        User newUser = admin.addNewTeamManager("team", "manager", "teamManager@gmail.com", 20000, false, false);
        assertNotNull(newUser);
    }

    @Test
    public void addNewUnionRepresentative() {
        User newUser = admin.addNewUnionRepresentative("", "","mail1@mail.com");
        assertNotNull(newUser);
    }

    @Test
    public void addNewAdmin() {
        User newUser = admin.addNewAdmin("Aa123456","","","admin@mail.com");
        assertNotNull(newUser);
    }

    @Test
    public void removeUser() {
        User user = admin.addNewAdmin("Aa123456","test","testAdmin","testAdmin@gmail.com");
        assertEquals(admin.removeUser(user.getID()),user.getMail());
    }

    @Test
    public void removeField() {
        UnionRepresentative unionRepresentative = system.getDatabase().getAllUnions().get(0);
        unionRepresentative.addFieldToSystem("Tel-Aviv","Bloomfield", 150000, 125000);
        admin.removeField(system.getDatabase().getAllActiveFields().get(1));
        assertFalse(system.getDatabase().getAllFields().get(1).isActive());
        system.getDatabase().getAllFields().get(1).reactivate();
        Database.updateObject(system.getDatabase().getAllFields().get(1));
    }

    @Test
    public void addField() {
        Field  field = new Field("Tel-Aviv","Bloomfield", 150000, 125000);
        admin.addField(field);
        assertTrue(field.isActive());
    }

    @Test
    public void responseToComplaint() {
        Guest guest = new Guest();
        User user = guest.register("fan@gmail.com", "Aa1234","test","testFan","0502055454","aa");
        Fan fan = (Fan) user.checkUserRole("Fan");
        fan.submitComplaint("complaint to system");
        String complaint = fan.getComplaintsId().get(0);
        assertTrue(admin.responseToComplaint(complaint, "answer"));
    }

    @Test
    public void viewLog() {
        assertNotNull(admin.viewLog("Errors"));
    }

    @Test
    public void myRole() {
        assertEquals(admin.myRole(),"Admin");

    }
    @Test
    public void getAllDetailsAboutCloseTeams(){
        assertNotNull(admin.getAllDetailsAboutCloseTeams());
    }
    @Test
    public void getAllCloseTeams() {
        assertNotNull(admin.getAllCloseTeams());
    }

    @Test
    public void getAllActiveComplaints(){
        assertNotNull(admin.getAllActiveComplaints());
    }
    }
