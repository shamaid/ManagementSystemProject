package Service;

import Domain.*;
import Logger.Logger;

import java.util.List;

public class UserSystem extends GuestSystem {


    public UserSystem() {
    }

    /*
    View fan search history
     */
    public List<String> viewSearchHistory(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Logger.logEvent(user.getID(), "View Search History");
            return user.getSearchHistory();
        }
        Logger.logError("(UserSystem) Viewing search history failed");
        return null;
    }

    /*
    log out user from system
     */
    public Guest logOut()
    {
        Logger.logEvent("(Guest)", "Logout");
        return this.guest;
    }

    /*
    View user's personal information
     */
    public String viewPersonalDetails(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Logger.logEvent(user.getID(), "View Personal Details");
            return user.toString(); // toString by user!?!?
        }
        return "";
    }

    /*
    Edit fan personal information
     */
    public boolean editFanPersonalDetails(String userId, String firstName, String lastName, String phone, String address) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                ((Fan) role).editPersonalInfo(user, firstName, lastName, phone, address);
                Logger.logEvent(user.getID(), "Edit Fan Personal Details");
                return true;
            }
        }
        Logger.logError("Failed editing fan's personal details");
        return false;
    }

    /*
    Edit user personal information
     */
    public void editPersonalInfo(String userId, String firstName, String lastName) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            user.editPersonalInfo(firstName, lastName);
            Logger.logEvent(user.getID(), "Edit Personal Details");
        }
    }

    /*
    user adds a complaint to the system
     */
    public boolean addComplaint(String userId, String description) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                Boolean ans=  ((Fan) role).submitComplaint(description);
                if (ans)
                    Logger.logEvent(user.getID(), "Added Complaint");
                else
                    Logger.logEvent(user.getID(), "Adding complaint Failed");
                return ans;
            }
        }
        Logger.logEvent(user.getID(), "Adding complaint Failed");
        return false;
    }

    public boolean registrationToFollowUp(String userId, String pageId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                boolean success = ((Fan) role).addPageToFollow(pageId);
                if (success)
                    Logger.logEvent(user.getID(), "Follow page Success");
                else
                    Logger.logError("Follow page Failed");

                return success;
            }
        }
        return false;
    }

    /*
    Fan registration for alerts for games you've selected
     */
    public boolean registrationForGamesAlerts(String userId, List<String> gamesId, boolean toMail) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                boolean success = ((Fan) role).registrationForGamesAlerts(gamesId, toMail);
                if (success)
                    Logger.logEvent(user.getID(), "Game Alerts Registration Success");
                else
                    Logger.logError("Game Alerts Registration Failed");

                return success;
            }
        }
        return false;
    }

    public boolean updateTrainingForCoach(String userId, String training) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Coach");
            if (role instanceof Coach) {
                Coach.TrainingCoach trainingCoach = Coach.TrainingCoach.valueOf(training);
                ((Coach) role).setTraining(trainingCoach);
                Logger.logEvent(user.getID(), "Updated coach's training");
                return true;
            }
        }
        Logger.logError("Updating coach's training Failed");
        return false;
    }
    public boolean updateTrainingForReferee(String userId, String training) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Referee");
            if (role instanceof Referee) {
                Referee.TrainingReferee trainingReferee = Referee.TrainingReferee.valueOf(training);
                ((Referee) role).setTraining(trainingReferee);
                Logger.logEvent(user.getID(), "Updated referee's training");
                return true;
            }
        }
        Logger.logError("Updating referee's training Failed");
        return false;
    }

    public boolean updateRoleForPlayer(String userId, String newRole) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Player");
            if (role instanceof Player) {
                Player.RolePlayer rolePlayer = Player.RolePlayer.valueOf(newRole);
                ((Player) role).setRole(rolePlayer);
                Logger.logEvent(user.getID(), "Updated player's role");
                return true;
            }
        }
        Logger.logError("Updating player's role Failed");
        return false;
    }
    public boolean updateRoleForCoach(String userId, String newRole) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Coach");
            if (role instanceof Coach) {
                Coach.RoleCoach roleCoach = Coach.RoleCoach.valueOf(newRole);
                ((Coach) role).setRoleInTeam(roleCoach);
                Logger.logEvent(user.getID(), "Updated coach's role");
                return true;
            }
        }
        Logger.logError("Updating coach's role Failed");
        return false;
    }
     /*
    Search results in a system
     */
    public List<String> search(String userId,  String wordToSearch){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Logger.logEvent(user.getID(), "Searched " + wordToSearch);
            return user.search(wordToSearch);
        }
        Logger.logError("(UserSystem) Searching Failed");
        return null;
    }

    public List<String> getUserRoles(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Logger.logEvent(user.getID(), "Got user roles");
            return user.getStringRoles();
        }
        Logger.logError("Getting user roles Failed");
        return null;
    }

    public List<String> getFanPages(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                Logger.logEvent(user.getID(), "Requested followed pages");
                return ((Fan) role).getFollowedPages();
            }
        }
        Logger.logError("Getting fan pages Failed");
        return null;
    }
    public List<String> getAllPages(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                Logger.logEvent(user.getID(), "Requested all the personal pages in the system");
                return ((Fan) role).getAllPages();
            }
        }
        Logger.logError("Getting all pages Failed");
        return null;
    }
    public List<String> getAllFutureGames(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if (role instanceof Fan) {
                Logger.logEvent(user.getID(), "Requested all the future games");
                return ((Fan) role).getAllFutureGames();
            }
        }
        Logger.logError("Getting all future games Failed");
        return null;
    }

    public String getRoleForPlayer(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Player");
            if (role instanceof Player) {
                Logger.logEvent(user.getID(), "Got player's role");
                return ((Player) role).getRole();
            }
        }
        Logger.logError("Getting player's role Failed");
        return "";
    }

    public String getRoleForCoach(String userId) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Coach");
            if (role instanceof Coach) {
                Logger.logEvent(user.getID(), "Got coach's role");
                return ((Coach) role).getRoleInTeam();
            }
        }
        Logger.logError("Getting coach's role Failed");
        return "";
    }

    public String getUserInfo(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Fan");
            if(role instanceof Fan)
            {
                Logger.logEvent(userId, "Got user info");
                return ((Fan)role).getUserInfo();

            }
            role = user.checkUserRole("Player");

            if(role instanceof Player)
            {
                Logger.logEvent(userId, "Got user info");
                return role.getUserInfo();

            }
            role = user.checkUserRole("Coach");

            if(role instanceof Player)
            {
                Logger.logEvent(userId, "Got user info");
                return role.getUserInfo();

            }
        }
        Logger.logError("Getting user info Failed");
        return null;
    }

}
