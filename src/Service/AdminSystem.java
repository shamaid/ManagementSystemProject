package Service;

import Domain.*;
import Domain.User;
import Logger.Logger;
import java.util.Date;
import java.util.List;

public class AdminSystem {

    public AdminSystem() {

    }

    /*
    Remove user by an administrator
    */
    public boolean removeUser(String adminId, String userId){
        User user = UserFactory.getUser(adminId);
        if (user != null) {
            Role adminRole = user.checkUserRole("Admin");
            if(adminRole instanceof Admin){
                String userMail = ((Admin)adminRole).removeUser(userId);
                if(!(userMail.equals(""))) {
                    Logger.logEvent(adminId + " (Admin)", "Removed user " + userId);
                    return true;
                }
            }
        }
        Logger.logError("AdminSystem: Removing user failed");
        return false;
    }

    public String addNewPlayer(String adminId, String firstName, String lastName, String mail, Date birthDate, String role, double price){
        User user = UserFactory.getUser(adminId);
        if(user!=null){
            Role adminRole = user.checkUserRole("Admin");
            if(adminRole instanceof Admin) {
                Player.RolePlayer rolePlayer = Player.RolePlayer.valueOf(role);
                if (rolePlayer != null) {
                    User playerAdded = ((Admin) adminRole).addNewPlayer(firstName, lastName, mail, birthDate, rolePlayer, price);
                    Logger.logEvent(adminId + " (Admin)", "Added player " + playerAdded.getID());
                    return playerAdded.getID();
                }
            }
        }
        Logger.logError("AdminSystem: adding new Player failed");
        return null;
    }
    public String addNewCoach(String adminId, String firstName, String lastName, String mail, String training, String role, double price){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                Coach.TrainingCoach trainingCoach = Coach.TrainingCoach.valueOf(training);
                Coach.RoleCoach roleCoach = Coach.RoleCoach.valueOf(role);
                User coachAdded = ((Admin) adminRole).addNewCoach(firstName, lastName, mail, trainingCoach, roleCoach, price);
                Logger.logEvent(adminId + " (Admin)", "Added coach " + coachAdded.getID());
                return coachAdded.getID();
            }
        }
        Logger.logError("AdminSystem: adding new Coach failed");
        return null;
    }
    public String addNewTeamOwner(String adminId,String firstName, String lastName, String mail){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                User teamOwnerAdded = ((Admin) adminRole).addNewTeamOwner(firstName, lastName, mail);
                Logger.logEvent(adminId + " (Admin)", "Added Team owner " + teamOwnerAdded.getID());
                return teamOwnerAdded.getID();
            }
        }
        Logger.logError("AdminSystem: adding new Team Owner failed");

        return null;
    }
    public String addNewTeamManager(String adminId,String firstName, String lastName, String mail, double price,boolean manageAssets , boolean finance){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                User managerAdded = ((Admin) adminRole).addNewTeamManager(firstName, lastName, mail, price, manageAssets, finance);
                Logger.logEvent(adminId + " (Admin)", "Added Team manager " + managerAdded.getID());
                return managerAdded.getID();
            }
        }
        Logger.logError("AdminSystem: adding new Team Owner failed");

        return null;
    }
    public String addNewUnionRepresentative(String adminId,String firstName, String lastName, String mail){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                User representetiveAdded = ((Admin) adminRole).addNewUnionRepresentative(firstName, lastName, mail);
                Logger.logEvent(adminId + " (Admin)", "Added Union Representetive " + representetiveAdded.getID());
                return representetiveAdded.getID();
            }
        }
        Logger.logError("AdminSystem: adding new Union Representative failed");

        return null;
    }

    public boolean addFirstAdmin(String password, String firstName, String lastName, String mail)
    {
        Admin firstAdmin = new Admin(null);
        User adminAdded = firstAdmin.addNewAdmin(password, firstName, lastName, mail);

        if (adminAdded == null)
            return false;

        return true;

    }

    public String addNewAdmin(String adminId,String password ,String firstName, String lastName, String mail){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                User adminAdded = ((Admin) adminRole).addNewAdmin(password, firstName, lastName, mail);
                Logger.logEvent(adminId + " (Admin)", "Added Admin " + adminAdded.getID());
                return adminAdded.getID();
            }
        }
        Logger.logError("AdminSystem: adding new admin failed");

        return null;

    }

    /*
    Permanently close a group only by an administrator
    */
    public String permanentlyCloseTeam(String adminId, String teamId){
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                Logger.logEvent(adminId + " (Admin)", " Permanently closed team " + teamId);
                return  (((Admin) adminRole).closeTeamPermanently(teamId));
                    //notificationSystem.openORCloseTeam("closed", team, true);
            }
        }
        Logger.logError("AdminSystem: permanently closing team failed");
        return null;
    }

    public void responseToComplaint(String adminId, String complaintId, String response)
    {
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                ((Admin) adminRole).responseToComplaint(complaintId, response);
                Logger.logEvent(adminId + " (Admin)", " Responded to complaint");
            }
        }
    }

    public List<String> viewLog(String adminId, String type)
    {
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                Logger.logEvent(adminId, " Viewed " + type + " Log");
                return ((Admin) adminRole).viewLog(type);
            }
        }
        Logger.logError("AdminSystem: opening log failed");

        return null;
    }

    public boolean trainModel(String adminId)
    {
        User user = UserFactory.getUser(adminId);
        if(user!=null) {
            Role adminRole = user.checkUserRole("Admin");
            if (adminRole instanceof Admin) {
                ProxyRecommendationSystem recommendationSystem = new ProxyRecommendationSystem();
                recommendationSystem.connect();
                Logger.logEvent(adminId + " (Admin)", " activated the training model");
                return recommendationSystem.trainModel();
            }
        }
        Logger.logError("AdminSystem: training model failed");

        return false;
    }

    public List<String> getAllDetailsAboutOpenTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all details of open teams");
                return role.getAllDetailsAboutOpenTeams();
            }
        }
        Logger.logError("AdminSystem: getting all details of open teams failed");

        return null;
    }

    public List<String> getAllOpenTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all open teams");
                return role.getAllOpenTeams();
            }
        }
        Logger.logError("AdminSystem: getting all open teams failed");

        return null;
    }

    public List<String> getAllDetailsAboutCloseTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all details of closed teams");
                return ((Admin)role).getAllDetailsAboutCloseTeams();
            }
        }
        Logger.logError("AdminSystem: getting all details of closed teams failed");

        return null;
    }

    public List<String> getAllCloseTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all closed teams");
                return ((Admin)role).getAllCloseTeams();
            }
        }
        Logger.logError("AdminSystem: getting all closed teams failed");

        return null;
    }

    public List<String> getAllUsers(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all users");
                role.getAllUsers();
            }
        }
        Logger.logError("AdminSystem: getting all players failed");

        return null;
    }
    public List<String> getAllActiveComplaints(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Admin");
            if (role instanceof Admin) {
                Logger.logEvent(userId + " (Admin)", " got all active complaints");
                ((Admin)role).getAllActiveComplaints();
            }
        }
        Logger.logError("AdminSystem: getting all active complaints failed");

        return null;
    }
}
