package Domain;

import Data.Database;
import Logger.Logger;
import java.util.*;

public class Admin extends Role implements Observer {

    private LinkedList<Team> permanentlyClosedTeams;

    public Admin(String userId) {
        this.userId = userId;
        permanentlyClosedTeams = new LinkedList<>();
        myRole = "Admin";
    }

    public String closeTeamPermanently(String teamId)
    {
        Team team = Database.getTeam(teamId);
        if(!team.isPermanentlyClosed() && !permanentlyClosedTeams.contains(team)) {
            team.setActive(false);
            team.setPermanentlyClosed(true);
            permanentlyClosedTeams.add(team);
            Database.updateObject(team);
            Database.updateObject(this);
            Logger.logEvent(userId + " (Admin)", " Closed Team " + team.getName() + " permanently");

            return team.getName();
        }
        return null;
    }
    public User addNewPlayer(String firstName, String lastName, String mail, Date birthDate, Player.RolePlayer role, double price){
        return UserFactory.getNewPlayer(firstName, lastName, mail, birthDate, role, price);
    }
    public User addNewCoach(String firstName, String lastName, String mail, Coach.TrainingCoach training, Coach.RoleCoach role, double price){
        return UserFactory.getNewCoach(firstName, lastName, mail, training, role, price);
    }
    public User addNewTeamOwner(String firstName, String lastName, String mail){
        return  UserFactory.getNewTeamOwner(firstName, lastName, mail);
    }
    public User addNewTeamManager(String firstName, String lastName, String mail, double price,boolean manageAssets , boolean finance){
        return UserFactory.getNewTeamManager(firstName, lastName, mail, price, manageAssets, finance);
    }
    public User addNewUnionRepresentative(String firstName, String lastName, String mail){
        return UserFactory.getNewUnionRepresentative(firstName, lastName, mail);
    }

    public User addNewAdmin(String password,String firstName, String lastName, String mail){
        return UserFactory.getNewAdmin(password,firstName, lastName, mail);
    }

    public String removeUser(String userId)
    {
        User user = Database.getUser(userId);
        TeamOwner roleTeamOwner = (TeamOwner) user.checkUserRole("TeamOwner");
        if(roleTeamOwner!=null){
            List<Team> teams = roleTeamOwner.getTeamsToManage();
            if(teams!=null){
                for(Team team:teams )
                    if(team.getTeamOwners().size()==1)
                        return "";
            }
        }
        return Database.removeUser(userId);
    }

    /**The Admin is responsible for adding
     * (including returning the Field to be active)
     * And removal of a Field*/
    public void removeField(Field field){
        if(field.getTeams().size() == 0){
            Database.removeField(field.getID());
        }
    }

    /**The Admin is responsible for adding
     * (including returning the Field to be active)
     * And removal of a Field*/
    public void addField(Field field){
        Database.addAsset(field);
        field.reactivate();
    }

    public boolean responseToComplaint(String complaintId, String response){
        Complaint complaint = Database.getComplaints(complaintId);
        if(complaint!=null){
            complaint.setResponse(response);
            Database.updateObject(complaint);
            return true;
        }
        return false;
    }
    public List<String> viewLog(String type){
        switch (type) {
            case "Events":
                return Logger.getEventsLog();

            case "Errors":
                return Logger.getErrorsLog();

            case "Server":
                return Logger.getServerLog();

            default:
                return null;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String news = (String)arg;
        Database.getUser(userId).addMessage(news);
    }

    // ++++++++++++++++++++++++++++ getter ++++++++++++++++++++++++++++

    public List<String> getAllDetailsAboutCloseTeams() {
        List<String> details = new LinkedList<>();
        for (Team team : Database.getCloseTeams())
            details.add(team.toString() + " " + team.AllDetailsAboutTeam());
        return details;
    }

    public List<String> getAllCloseTeams() {
        List<String> details = new LinkedList<>();
        for (Team team : Database.getCloseTeams())
            details.add(team.toString());
        return details;
    }

    public List<String> getAllActiveComplaints(){
        List<String> complaints = new LinkedList<>();
        for (Complaint c : Database.getAllActiveComplaints()){
            complaints.add(c.toString());
        }
        return complaints;
    }

}
