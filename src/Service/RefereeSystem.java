package Service;

import Domain.*;
import java.util.LinkedList;
import java.util.List;
import Logger.Logger;

public class RefereeSystem {

    public RefereeSystem() {
    }


    public boolean addEventToGame(String userID, String gameID, String event, String playerId, String teamId){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("Referee");
        if(role instanceof  Referee){
           Boolean success= ((Referee)role).addEventToGame(gameID, Event.EventType.valueOf(event), playerId, teamId);
           if(success) {
               Logger.logEvent(user.getID() + " (Referee)", "Added Event to Game");
               return true;
           }

        }
        Logger.logError("Adding event to game failed");
        return false;
    }

    public boolean setScoreInGame(String userID,String gameID, int hostScore, int guestScore){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("Referee");
        if(role instanceof  Referee){
            boolean success = ((Referee)role).setScoreInGame(gameID, hostScore, guestScore);
            if(success)
                Logger.logEvent( user.getID()+ " (Referee)","Set game's score");
            return success;
        }
        return false;
    }

    public List<String> getGameReport(String userID, String gameID){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("Referee");
        if(role instanceof  Referee ) {
            Logger.logEvent(userID, "Got Game Report");
            return  ((Referee)role).getGameReport(gameID);
        }
        Logger.logError("Getting Game Report failed");
        return null;
    }

    public boolean changeEvent(String userID, String gameID, String eventID, String newDescription){
        User user= UserFactory.getUser(userID);
        Referee role = (Referee) user.checkUserRole("Referee");
        if(role instanceof Referee){
           boolean success= role.changeEvent(gameID, eventID, newDescription);
           if(success) {
               Logger.logEvent(userID, "Changed event " + eventID);
               return true;
           }
        }
        Logger.logError("Chaning event failed");
        return false;
    }

    public List<String> getAllPastGames(String userID){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("Referee");
        if(role instanceof  Referee ) {
            Logger.logEvent(userID, "Got all Past Games");
            return  ((Referee)role).getAllPastGames();
        }
        Logger.logError("Getting all Past Games failed");
        return null;
    }

    public String getAllOccurringGame(String userID){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("Referee");
        if(role instanceof  Referee) {
            Logger.logEvent(userID, "Got All Occurring Games");
            return ((Referee)role).getAllOccurringGame();
        }
        Logger.logError("Getting All Occurring games failed");
        return null;
    }

    public List<String> getAllTeamAssets(String userId, String teamId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("Referee");
            if (role instanceof Referee) {
                Logger.logEvent(user.getID(), "Got all Team Assets");
                return (role).getAllTeamAssets(teamId);
            }
        }
        Logger.logError("Failed getting all Team Assets");
        return null;
    }
}
