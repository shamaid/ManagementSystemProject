package Domain;

import Data.Database;
import java.util.*;

public class TeamOwner extends Manager implements Observer {

    private List<Team> closedTeams;
    private HashMap<User, Team> appointedTeamOwners;
    private HashMap<User, Team> appointedTeamManagers;
    private HashMap<Team,PersonalPage> personalPages;

    public TeamOwner(String userId) {
        super();
        this.closedTeams = new LinkedList<>();
        this.personalPages = new HashMap<>();
        this.appointedTeamOwners = new HashMap<>();
        this.appointedTeamManagers = new HashMap<>();
        this.myRole = "TeamOwner";
        this.userId = userId;
    }

    public TeamOwner(String userId, List<Team> teams, List<Team> closedTeams, HashMap<User, Team> appointedTeamOwners, HashMap<User, Team> appointedTeamManagers, HashMap<Team, PersonalPage> personalPages)
    {
        this.userId = userId;
        this.teamsToManage = teams;
        this.closedTeams = closedTeams;
        this.appointedTeamOwners = appointedTeamOwners;
        this.appointedTeamManagers = appointedTeamManagers;
        this.personalPages = personalPages;
        this.myRole = "TeamOwner";
    }

    @Override
    public String toString() {
        return "TeamOwner" +
                ", id=" + userId +
                ": name=" + Database.getUser(userId).getName();
    }

    @Override
    public void update(Observable o, Object arg) {
        String news = (String)arg;
        Database.getUser(userId).addMessage(news);
    }

    public void addTeam(Team team)
    {
        if(!teamsToManage.contains(team)) {
            teamsToManage.add(team);
        }
    }

    public boolean addTeamPersonalPage(Team team){
        if(teamsToManage.contains(team)) {
            if (!personalPages.containsKey(team)) {
                personalPages.put(team, team.getPage());
                Database.updateObject(this);
                return true;
            }
        }
        return false;
    }

    public Team getTeamById(String id){
        for(Team team : teamsToManage){
            if(team.getID().equals(id))
                return team;
        }
        return null;
    }

    public void removeTeam(Team team) {
        teamsToManage.remove(team);
    }

    public boolean createTeam(User user , String teamName, List<String> playersId, List<String> coachesId, String fieldId){
        Role teamOwnerRole = user.checkUserRole("TeamOwner");
        if(teamOwnerRole != null){
            List<User> teamOwner = new LinkedList<>();
            teamOwner.add(user);

            Field field =Database.getField(fieldId);
            List<User> players = findUsers(playersId);
            List<User> coaches = findUsers(coachesId);

            /**create new team*/
            Team team = new Team(teamName ,teamOwner ,players,coaches, field);
            return Database.addTeam(team);
        }
        return false;
    }

    private List<User> findUsers(List<String> usersId) {
        List<User> listOfUsers = new LinkedList<>();
        for (String id : usersId){
            User user = Database.getUser(id);
            if(user!=null){
                listOfUsers.add(user);
            }
        }
        return listOfUsers;
    }

    public boolean appointTeamOwner(User user, String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            Role teamOwnerRole = user.checkUserRole("TeamOwner");
            if (teamsToManage.contains(team)) {
                if (!team.getTeamOwners().contains(user)) {
                    if (teamOwnerRole == null) {
                        TeamOwner teamOwner = new TeamOwner(user.getID());
                        user.addRole(teamOwner);
                        teamOwnerRole = user.checkUserRole("TeamOwner");
                        Database.addTeamOwner((TeamOwner) teamOwnerRole);
                        Database.updateObject(user);
                    }
                    ((TeamOwner) teamOwnerRole).addTeam(team);
                    team.addTeamOwner(user,true);
                    appointedTeamOwners.put(user, team);
                    Database.updateObject(this);
                    Database.updateObject(team);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean appointTeamManager(User user, String teamId, double price, boolean manageAssets , boolean finance){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            Role teamManagerRole = user.checkUserRole("TeamManager");
            if (teamsToManage.contains(team)) {
                if (!team.getTeamManagers().contains(user) && !team.getTeamOwners().contains(user)) {
                    if (teamManagerRole == null) {
                        TeamManager teamManager = new TeamManager(user.getID(), price, manageAssets, finance);
                        user.addRole(teamManager);
                        Database.updateObject(teamManager);
                    }
                    team.addTeamManager(user, price, manageAssets, finance);
                    appointedTeamManagers.put(user, team);
                    Database.updateObject(this);
                    Database.updateObject(team);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeAppointTeamOwner(User user, String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            TeamOwner teamOwnerRole = (TeamOwner) user.checkUserRole("TeamOwner");
            if (teamsToManage.contains(team) && appointedTeamOwners.containsKey(user)) {
                if (team.getTeamOwners().contains(user)) {
                    this.appointedTeamOwners.remove(user);
                    removeAllAppoint(user, team.getID());
                    team.removeTeamOwner(user);
                    if (teamOwnerRole.getTeamsToManage().size() == 0){
                        user.getRoles().remove(teamOwnerRole);
                        Database.removeRowFromTable("TeamOwners", user.getID());
                    }
                    Database.updateObject(this);
                    Database.updateObject(user);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeAppointTeamManager(User user, String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            TeamManager teamManagerRole = (TeamManager) user.checkUserRole("TeamManager");
            if (this.teamsToManage.contains(team) && this.appointedTeamManagers.containsKey(user)) {
                if (team.getTeamManagers().contains(user)) {
                    team.removeTeamManager(user);
                    this.appointedTeamManagers.remove(user);
                    if (teamManagerRole.getTeamsToManage().size() == 0) {
                        user.getRoles().remove(teamManagerRole);
                        Database.removeRowFromTable("TeamManagers",user.getID());
                    }
                    Database.updateObject(this);
                    Database.updateObject(user);
                    return true;
                }
            }
        }
        return false;
    }

    private void removeAllAppoint(User user, String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            TeamOwner teamOwnerRole = (TeamOwner) user.checkUserRole("TeamOwner");

            for (User userTO : teamOwnerRole.getAppointedTeamOwners().keySet()) {
                teamOwnerRole.removeAppointTeamOwner(userTO, team.getID());
            }
            for (User userTM : teamOwnerRole.getAppointedTeamManagers().keySet()) {
                teamOwnerRole.removeAppointTeamManager(userTM, team.getID());
            }
        }
    }

    public boolean closeTeam(String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            if (teamsToManage.contains(team) && !closedTeams.contains(team)) {
                team.setActive(false);
                teamsToManage.remove(team);
                closedTeams.add(team);
                Database.updateObject(this);
                Database.updateObject(team);
                return true;
            }
        }
        return false;
    }
    public boolean reopenTeam(String teamId){
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            if (closedTeams.contains(team) && !team.isActive() && !team.isPermanentlyClosed()) {
                team.setActive(true);
                closedTeams.remove(team);
                teamsToManage.add(team);
                Database.updateObject(this);
                Database.updateObject(team);
                return true;
            }
        }
        return false;
    }

    // ++++++++++++++++++++++++++++ getter ++++++++++++++++++++++++++++

    public List<Team> getClosedTeams() {
        return closedTeams;
    }

    public List<String> getAllClosedTeam() {
        List <String> closed = new LinkedList<>();
        for (Team team : closedTeams) {
            if (!team.isPermanentlyClosed())
                closed.add(team.toString());
        }
        return closed;
    }

    public HashMap<Team, PersonalPage> getPersonalPages() {
        return personalPages;
    }

    public HashMap<User, Team> getAppointedTeamOwners() {
        return appointedTeamOwners;
    }

    public HashMap<User, Team> getAppointedTeamManagers() {
        return appointedTeamManagers;
    }

}
