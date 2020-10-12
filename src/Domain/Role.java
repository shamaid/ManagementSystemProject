package Domain;

import Data.Database;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class Role{

    protected String myRole;
    protected String userId;

    public String getID() {
        return userId;
    }

    public List<String> getMessageBox() {
        return Database.getUser(userId).getMessageBox();
    }

    public String myRole(){
        return myRole;
    }

    @Override
    public boolean equals(Object obj) {
        Role roleObj = (Role)obj;
        if(roleObj!=null)
            return roleObj.getID().equals(this.getID());
        return false;
    }

    public String teamsString(HashSet<Team> teams){
        String teamsName="";
        for (Team team : teams){
            teamsName = teamsName + team.getName() + ",";
        }
        return teamsName;
    }
    public List<String> getAllDetailsAboutOpenTeams() {
        List<String> details = new LinkedList<>();
        for (Team team : Database.getOpenTeams())
            details.add(team.toString() + " " + team.AllDetailsAboutTeam());
        return details;
    }

    public List<String> getAllOpenTeams() {
        List<String> details = new LinkedList<>();
        for (Team team : Database.getOpenTeams())
            details.add(team.toString());
        return details;
    }

    public List<String> getAllUsers() {
        List<String> users = new LinkedList<>();
        String details;
        for (User user : Database.getAllUsers()){
            details= user.getID()+":"+ user.getName()+",";
            for(Role role : user.getRoles()){
                details=details+role.myRole+":";
            }
            users.add(details);
        }
        return users;
    }

    public String getUserInfo() {
        return "Fan" +
                ", firstName=" + Database.getUser(userId).getFirstName() +
                ", lastName=" + Database.getUser(userId).getLastName() ;
    }

    public List<String> getAllTeamAssets(String teamId) {
        List<String> teamAssets = new LinkedList<>();
        Team team = Database.getTeam(teamId);
        if(team!=null){
            for(User teamManager: team.getTeamManagers())
                teamAssets.add(teamManager.toString());
            for(User coach : team.getCoaches())
                teamAssets.add(coach.toString());
            for (User player : team.getPlayers())
                teamAssets.add(player.toString());
            for(Field field : team.getFields())
                teamAssets.add(field.toString());
        }
        return teamAssets;
    }

    public static List<String> getAllPastGames(){
        return Database.getAllPastGames();
    }

}
