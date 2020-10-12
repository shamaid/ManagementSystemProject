package Data;

import Domain.*;
import Service.FootballManagementSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseTest {

    DataAccess dataAccess;
    Database database;
    FootballManagementSystem system;
    Coach coach;
    Team team;
    Team team1;

    @Before
    public void init(){
        dataAccess=new DataAccess();
        database = new Database();
        system = new FootballManagementSystem();
        system.systemInit(true);
        String leagueId = system.dataReboot();
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        team = league.getTeams().get(0);
        team1 = league.getTeams().get(1);
        Admin admin = (Admin) system.getAdmin();
        User coachU= admin.addNewCoach("dor","dor","dor@mail.com", Coach.TrainingCoach.UEFA_B, Coach.RoleCoach.main,50000);
        coach = (Coach) coachU.checkUserRole("Coach");
        coach.addTeam(team);
        coach.addTeam(team1);

        //dataAccess.createDB();

        //dataAccess.createDB();


    }

    @Test
    public void updateObject(){

        database.updateObject(coach);
    }

    @Test
    public void getAllUsers(){
       // dataAccess.addCell("Users","123","doron","doron","doron@gmail.com","true","TeamOwner","aa");
       // dataAccess.addCell("Users","456","Saly","Saly","Saly@gmail.com","true","TeamOwner","aa");
        List<User> users = Database.getAllUsers();

        System.out.println(Boolean.parseBoolean("true"));

        assertEquals(2,users.size());
    }

    @Test
    public void getUser(){
        User user = Database.getUser("888");
    }
}