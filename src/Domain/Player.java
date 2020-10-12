package Domain;

import Data.Database;
import java.util.Date;
import java.util.HashSet;

public class Player extends Role implements PartOfATeam {

    public enum RolePlayer {
        goalkeeper,
        playerBack,
        midfielderPlayer,
        attackingPlayer
    }
    private Date birthDate;
    private RolePlayer roleInTeam;
    private HashSet<Team> teams;
    private boolean isActive;
    private double price;


    public Player(String userId, Date birthDate, RolePlayer role, double price) {
        this.userId = userId;
        this.birthDate = birthDate;
        this.roleInTeam = role;
        this.teams = new HashSet<>();
        this.isActive = true;
        this.price = price;
        this.myRole = "Player";

    }

    public Player(String userId, Date birthDate, HashSet<Team> teams, String roleInTeam, boolean isActive, double price)
    {
        this.userId = userId;
        this.birthDate = birthDate;
        this.teams = teams;
        this.roleInTeam = RolePlayer.valueOf(roleInTeam);
        this.isActive = isActive;
        this.price = price;
        this.myRole = "Player";
    }

    @Override
    public String toString() {
        return "Player" +
                ", id= " +userId +
                ": name=" +Database.getUser(userId).getName() +
                ", birthDate=" + birthDate +
                ", role in team=" + roleInTeam+
                ", teams= "+ teamsString(teams);
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

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public Date getBirthDate() {
        return birthDate;
    }

    public String getRole() {
        return roleInTeam.toString();
    }

    public void setRole(RolePlayer role) {
        this.roleInTeam = role;
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
        this.price = update;
    }

    @Override
    public HashSet<Team> getTeams() {
        return teams;
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
    public void deactivate() {
        isActive = false;
    }

}
