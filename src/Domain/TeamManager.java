package Domain;

import Data.Database;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class TeamManager extends Manager implements PartOfATeam, Observer {

    private HashSet<Team> teams;
    private boolean isActive;
    private double price;

    private boolean permissionManageAssets;
    private boolean permissionFinance;



    public TeamManager(String userId, double price, boolean manageAssets , boolean finance ) {
        this.teams = new HashSet<>();
        this.price = price;
        this.isActive = true;
        this.permissionManageAssets = manageAssets;
        this.permissionFinance = finance;
        this.myRole = "TeamManager";
        this.userId = userId;
    }

    public TeamManager(String userId, HashSet<Team> teams, boolean isActive, double price, boolean manageAssets, boolean finance)
    {
        this.userId = userId;
        this.teams = teams;
        this.isActive = isActive;
        this.price = price;
        this.permissionManageAssets = manageAssets;
        this.permissionFinance = finance;
        this.myRole = "TeamManager";
    }

    @Override
    public void addTeam(Team team) {
        teamsToManage.add(team);
        teams.add(team);
    }

    @Override
    public void removeTeam(Team team) {
        teamsToManage.remove(team);
        teams.remove(team);
        Database.updateObject(this);
    }

    @Override
    public String toString() {
        return "TeamManager" +
                ", id="+ userId+
                ": name="+ Database.getUser(userId).getName()+
                ", price=" + price +
                ", permission manage assets=" + permissionManageAssets +
                ", permission finance=" + permissionFinance+
                ", teams= "+ teamsString(teams);
    }

    @Override
    public void update(Observable o, Object arg) {
        String news = (String)arg;
        Database.getUser(userId).addMessage(news);
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    @Override
    public String getID() {
        return userId;
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
        isActive=false;
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

    public boolean isPermissionManageAssets() {
        return permissionManageAssets;
    }

    public boolean isPermissionFinance() {
        return permissionFinance;
    }
}