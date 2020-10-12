package Service;

import Domain.*;
import Domain.User;
import Logger.Logger;

import java.util.List;

public class TeamManagementSystem {

    public TeamManagementSystem() {

    }
    /*
    this function adds a new asset to the system
     */
    public boolean addAssetPlayer(String userId, String assetId, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).addPlayerToTeam(assetId, teamId);
                if (success)
                    Logger.logEvent(user.getID(), "Added Player " + assetId + " to Team " + teamId);
                else
                    Logger.logError("Adding Player to team Failed");

                return success;
            }
        }
        return false;
    }

    public boolean addAssetCoach(String userId, String assetId, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).addCoachToTeam(assetId, teamId);

                if (success)
                    Logger.logEvent(user.getID(), "Added Coach " + assetId + " to Team " + teamId);
                else
                    Logger.logError("Adding Coach to team Failed");

                return success;
            }
        }
        return false;
    }

    public boolean addField(String userId, String assetId , String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).addFieldToTeam(assetId, teamId);

                if (success)
                    Logger.logEvent(user.getID(), "Added Field " + assetId + " to Team " + teamId);
                else
                    Logger.logError("Adding Field to team Failed");

                return success;
            }
        }
        return false;
    }

    public boolean removeField(String userId, String assetId , String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).removeFieldFromTeam(assetId, teamId);

                if (success)
                    Logger.logEvent(user.getID(), "Removed Field " + assetId + " from Team " + teamId);
                else
                    Logger.logError("Removing Field from team Failed");

                return success;
            }
        }
        return false;
    }
    /*
    Remove PartOfATeam
     */
    public boolean removeAssetPlayer(String userId, String assetId, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).removePlayerFormTeam(assetId, teamId);

                if (success)
                    Logger.logEvent(user.getID(), "Removed Player " + assetId + " from Team " + teamId);
                else
                    Logger.logError("Removing Player from team Failed");

                return success;
            }
        }
        return false;
    }
    public boolean removeAssetCoach(String userId, String assetId, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).removeCoachFormTeam(assetId, teamId);

                if (success)
                    Logger.logEvent(user.getID(), "Removed Coach " + assetId + " from Team " + teamId);
                else
                    Logger.logError("Removing Coach from team Failed");

                return success;
            }
        }
        return false;
    }

    public boolean updateAsset(String userId,String assetType, String assetId, String action, String update){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                if (role.myRole().equals("TeamManager") && !((TeamManager) role).isPermissionManageAssets())
                    return false;
                boolean success = ((Manager) role).updateAsset(assetType, assetId, action, update);

                if (success)
                    Logger.logEvent(user.getID(), "Updated asset " + assetId);
                else
                    Logger.logError("Updating Asset Failed");

                return success;
            }
        }
        return false;
    }


    public boolean createTeam(String userId , String teamName, List<String> playersId, List<String> coachesId, String fieldId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                if (((TeamOwner) role).createTeam(user, teamName, playersId, coachesId, fieldId)) {
                    Logger.logEvent(userId, "Created Team " + teamName);

                    return true;
                }
            }
        }
        Logger.logError("Failed Creating Team");

        return false;
    }

    public boolean appointmentTeamOwner(String userId, String userIdToAppoint, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                User userToAppoint = UserFactory.getUser(userIdToAppoint);
                if (((TeamOwner) role).appointTeamOwner(userToAppoint, teamId)) {
                    Logger.logEvent(userId, "Appointed Team Owner " + userIdToAppoint);
                    return true;
                }
            }
        }
        Logger.logError("Failed Appointing Team Owner");

        return false;
    }

    public boolean appointmentTeamManager(String userId,String userIdToAppoint, String teamId, double price, boolean manageAssets , boolean finance){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                User userToAppoint = UserFactory.getUser(userIdToAppoint);
                if (((TeamOwner) role).appointTeamManager(userToAppoint, teamId, price, manageAssets, finance)) {
                    Logger.logEvent(userId, "Appointed Team Manager " + userIdToAppoint);
                    return true;
                }
            }
        }
        Logger.logError("Failed Appointing Team Manager");

        return false;
    }

    public boolean removeAppointmentTeamOwner(String userId ,String userIdToRemove, String teamId) {
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                User userToRemove = UserFactory.getUser(userIdToRemove);
                if (((TeamOwner) role).removeAppointTeamOwner(userToRemove, teamId)) {
                    Logger.logEvent(userId, "Removed Team Owner " + userIdToRemove);
                    return true;
                }
            }
        }
        Logger.logError("Failed removing appointed Team Owner");

        return false;
    }

    public boolean removeAppointmentTeamManager(String userId ,String userIdToRemove, String teamId){
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                User userToRemove = UserFactory.getUser(userIdToRemove);
                if (((TeamOwner) role).removeAppointTeamManager(userToRemove, teamId)) {
                    Logger.logEvent(user.getID(), "Removed Team Manager " + userToRemove.getID());
                    return true;
                }
            }
        }
        Logger.logError("Failed removing appointed Team Manager");

        return false;

    }

    public boolean closeTeam(String userId, String teamId) {
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                if (((TeamOwner) role).closeTeam(teamId)) {
                    Logger.logEvent(user.getID(), "Closed Team " + teamId);
                    return true;
                }
            }
        }
        Logger.logError("Failed closing Team");

        return false;
    }

    public boolean reOpeningTeam(String userId, String teamId) {
        User user = UserFactory.getUser(userId);
        if (user != null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                if (((TeamOwner) role).reopenTeam(teamId)) {
                    Logger.logEvent(user.getID(), "Reopened Team " + teamId);
                    return true;
                }
            }
        }
        Logger.logError("Failed reopening Team");
        return false;
    }

    public List<String> getTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                Logger.logEvent(user.getID(), "Got Teams");
                return ((Manager)role).getStringTeams();
            }
        }
        Logger.logError("Failed getting Teams");
        return null;
    }

    public List<String> getAllUsers(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof Admin) {
                Logger.logEvent(user.getID(), "Got all Users");
                role.getAllUsers();
            }
        }
        Logger.logError("Failed getting all Users");
        return null;
    }

    public List<String> getAllPlayers(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                Logger.logEvent(user.getID(), "Got all Players");
                return ((Manager)role).getAllPlayers();
            }
        }
        Logger.logError("Failed getting all Players");
        return null;
    }

    public List<String> getAllCoaches(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                Logger.logEvent(user.getID(), "Got all Coaches");
                return ((Manager)role).getAllCoaches();
            }
        }
        Logger.logError("Failed getting all Coaches");
        return null;
    }

    public List<String> getAllFields(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                Logger.logEvent(user.getID(), "Got all Fields");
                return ((Manager)role).getAllFields();
            }
        }
        Logger.logError("Failed getting all Fields");
        return null;
    }

    public List<String> getAllTeamAssets(String userId, String teamId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Team");
            if (role instanceof Manager) {
                Logger.logEvent(user.getID(), "Got all Team Assets");
                return (role).getAllTeamAssets(teamId);
            }
        }
        Logger.logError("Failed getting all Team Assets");
        return null;
    }

    public List<String> getAllClosedTeam(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("TeamOwner");
            if (role instanceof TeamOwner) {
                Logger.logEvent(user.getID(), "Got all Closed Teams");
                return ((TeamOwner)role).getAllClosedTeam();
            }
        }
        Logger.logError("Failed getting all Closed Teams");
        return null;
    }
}
