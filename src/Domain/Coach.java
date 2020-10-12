package Domain;

import Data.Database;

import java.util.HashSet;

public class Coach extends Role implements PartOfATeam {

    public enum TrainingCoach {
        IFA_C,
        UEFA_A,
        UEFA_B,
        UEFA_PRO
    }

    public enum RoleCoach{
        main,
        assistantCoach,
        fitness,
        goalkeeperCoach
    }

    private TrainingCoach training;
    private RoleCoach roleInTeam;
    private HashSet<Team> teams;
    private boolean isActive;
    private double price;


    public Coach(String userId,TrainingCoach training, RoleCoach role, double price) {
        this.userId = userId;
        this.training = training;
        this.roleInTeam = role;
        this.price = price;
        teams = new HashSet<>();
        this.isActive = true;
        myRole = "Coach";
    }

    public Coach(String userId, TrainingCoach training, RoleCoach role, HashSet<Team> teams, boolean isActive, double price)
    {
        this.userId = userId;
        this.training = training;
        this.roleInTeam = role;
        this.price = price;
        this.teams = teams;
        this.isActive = isActive;
        myRole = "Coach";
    }
    @Override
    public void deactivate() {
        isActive=false;
    }

    @Override
    public void addTeam(Team team) {
        teams.add(team);
    }

    @Override
    public void removeTeam(Team team) {
        teams.remove(team);
        Database.updateObject(this);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void reactivate() {
        isActive = true;
    }

    @Override
    public String toString() {
        return "Coach" +
                ", id= " +userId +
                ": name=" +Database.getUser(userId).getName() +
                ", training=" + training +
                ", role in team=" + roleInTeam+
                ", teams= "+ teamsString(teams);
    }
    
    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public String getTraining() {
        return training.toString();
    }

    public String getRoleInTeam() {
        return roleInTeam.toString();
    }

    public void setTraining(TrainingCoach training) {
        this.training = training;
        Database.updateObject(this);
    }

    public void setRoleInTeam(RoleCoach roleInTeam) {
        this.roleInTeam = roleInTeam;
        Database.updateObject(this);
    }

    @Override
    public String getID() {
        return userId;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double update) {
        price = update;
    }

    @Override
    public HashSet<Team> getTeams() {
        return teams;
    }
}
