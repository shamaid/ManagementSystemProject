package Service;
import Domain.*;
import Logger.Logger;

import java.util.List;
import java.util.Date;

public class UnionRepresentativeSystem {

    public UnionRepresentativeSystem() {
    }

    public String configureNewLeague(String userId, String name, String level){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                League.LevelLeague levelLeague = League.LevelLeague.valueOf(level);
                String leagueId = ((UnionRepresentative) role).configureNewLeague(name, levelLeague);
                if (leagueId!=null) {
                    Logger.logEvent(user.getID(), "Configured new League");
                    return leagueId;
                }
                else
                    Logger.logError("Configure league Failed");
            }
        }
        return null;
    }

    public String configureNewSeason(String userId, int year, Date startDate){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                String seasonId = ((UnionRepresentative) role).configureNewSeason(year, startDate);
                if (seasonId!=null) {
                    Logger.logEvent(user.getID(), "Configured new Season");
                    return seasonId;
                }
                else
                    Logger.logError("Configure Season Failed");

            }
        }
        return null;
    }


    public String configureLeagueInSeason(String userId, String leagueId, String seasonId, String assignmentPolicy, String scorePolicy, double fee){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            if (assignmentPolicy == null || scorePolicy == null)
                return null;
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                LeagueInSeason lis = ((UnionRepresentative) role).configureLeagueInSeason(leagueId, seasonId, assignmentPolicy, scorePolicy, fee);

                if (lis != null) {
                    Logger.logEvent(user.getID(), "Configured league in season");
                    return lis.getId();
                }
                else
                    Logger.logError("Configuring league in season Failed");
            }
        }
        return null;
    }
    public String appointReferee(String userId, String firstName,String lastName, String mail, String training)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Referee.TrainingReferee trainingCoach= Referee.TrainingReferee.valueOf(training);
                User appointedRef = ((UnionRepresentative) role).appointReferee(firstName, lastName, mail, trainingCoach);

                if (appointedRef != null) {
                    Logger.logEvent(user.getID(), "Appointed referee " + appointedRef.getID());
                    return appointedRef.getID();
                }
                else
                    Logger.logError("Appointing Referee Failed");
            }
        }
        return null;
    }

    public boolean removeAppointReferee(String userId, String refId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Role roleRef = user.checkUserRole("Referee");
                if(roleRef instanceof Referee){
                    boolean success = ((UnionRepresentative) role).removeAppointReferee((Referee)roleRef);
                    if(success){
                        Logger.logEvent(user.getID(), "remove appointed referee " + refId);
                        return true;
                    }
                    else
                        Logger.logError("remove appointing Referee Failed");
                }
            }
        }
        return false;
    }

    public boolean assignRefToLeague(String userId, String leagueId, String refereeId)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                User referee = UserFactory.getUser(refereeId);
                Referee refereeRole = (Referee) referee.checkUserRole("Referee");
                if (refereeRole instanceof Referee) {
                    boolean success = ((UnionRepresentative) role).addRefereeToLeague(leagueId, refereeRole);
                    if (success)
                        Logger.logEvent(user.getID(), "Assigned Referee " + referee.getID() + " to League");
                    else
                        Logger.logError("Assigning referee to league Failed");

                    return success;
                }
            }
        }
        return false;
    }


    public boolean changeScorePolicy(String userId, String leagueId, String policy)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative) role).changeScorePolicy(leagueId, policy);
                if (success)
                    Logger.logEvent(user.getID(), "Changed score policy");
                else
                    Logger.logError("Change score policy Failed");

                return success;
            }
        }
        return false;
    }

    public boolean changeAssignmentPolicy(String userId, String leagueId, String policy)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative) role).changeAssignmentPolicy(leagueId, policy);
                if (success)
                    Logger.logEvent(user.getID(), "Changed assignment policy");
                else
                    Logger.logError("Change assignment policy Failed");
                return success;
            }
        }
        return false;
    }

    /*
    throws exceptions
     */
    public boolean assignGames(String userId, String leagueId)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative) role).assignGames(leagueId);

                if (success)
                    Logger.logEvent(user.getID(), "Assigned games to league");
                else
                    Logger.logError("Assigning games to league Failed");

                return success;
            }
        }
        return false;
    }

    public boolean changeGameDate(String userId, String gameId, Date newDate){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Changed game " + gameId + " date");
                return ((UnionRepresentative)role).changeGameDate(gameId, newDate);
            }
        }
        Logger.logError("Changing game's date Failed");
        return false;
    }

    public boolean changeGameLocation(String userId, String gameId, String fieldId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Changed game " + gameId + " location");
                return ((UnionRepresentative)role).changeGameLocation(gameId, fieldId);
            }
        }
        Logger.logError("Changing game's location Failed");
        return false;
    }

    public boolean addTeamToLeague(String userId, String leagueId, String teamId, ProxyAccountingSystem proxyAccountingSystem) {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Added team " + teamId + " to league " + leagueId);
                return  ((UnionRepresentative)role).addTeamToLeague(leagueId, teamId, proxyAccountingSystem);
            }
        }
        Logger.logError("Adding team to league Failed");
        return false;
    }

    public boolean calculateLeagueScore(String userId, String leagueId)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative)role).calculateLeagueScore(leagueId);
                if(success)
                    Logger.logEvent(user.getID(), "Calculated league score");
            }
        }
        Logger.logError("Calculating league's Failed");
        return false;
    }

    public boolean calculateGameScore(String userId, String leagueId,String gameId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative)role).calculateGameScore(leagueId, gameId);
                if(success) {
                    Logger.logEvent(user.getID(), "Calculated game score");
                    return true;
                }
            }
        }
        Logger.logError("Calculating game's score Failed");
        return false;
    }
    public boolean changeRegistrationFee(String userId, String leagueId, double newFee)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative)role).changeRegistrationFee(leagueId, newFee);
                if(success) {
                    Logger.logEvent(user.getID(), "Changed registration fee to " + newFee);
                    return true;
                }
            }
        }
        Logger.logError("Changing registration fee Failed");
        return false;
    }


    public boolean addTUTUPayment(String userId, String teamId, double payment){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                boolean success = ((UnionRepresentative) role).addTUTUPayment(teamId, payment);
                if(success) {
                    Logger.logEvent(user.getID(), "Added TUTU payment of " + payment);
                    return true;
                }
            }
        }
        Logger.logError("Adding TUTU Payment Failed");
        return false;
    }
    public boolean addPaymentsFromTheTUTU(String userId, String teamName, String date ,double payment, ProxyAccountingSystem proxyAccountingSystem){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Added payments from TUTU");
                return proxyAccountingSystem.addPayment(teamName, date, payment);
            }
        }
        Logger.logError("Adding payments from TUTU Failed");
        return false;
    }

    public boolean addFieldToSystem(String userId,String location,String fieldName, int capacity, double price){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Added Field " + fieldName + " to system");
                return ((UnionRepresentative)role).addFieldToSystem(location, fieldName, capacity, price);
            }
        }
        Logger.logError("Adding field to system Failed");
        return false;
    }

    public double getRegistrationFee(String userId,String leagueId)
    {
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got registration fee");
                return ((UnionRepresentative)role).getRegistrationFee(leagueId);

            }
        }
        Logger.logError("Getting registration fee Failed");
        return -1;
    }

    public List<String> allLeaguesInSeasons(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all Leagues in Seasons");
                return ((UnionRepresentative) role).allLeaguesInSeasons();
            }
        }
        Logger.logError("Getting all leagues in seasons Failed");
        return null;
    }
    public List<String> getAllLeagues(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all Leagues");
                return ((UnionRepresentative) role).getAllLeagues();
            }
        }
        Logger.logError("Getting all leagues Failed");
        return null;
    }
    public List<String> getAllSeasons(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all Seasons");
                return ((UnionRepresentative) role).getAllSeasons();
            }
        }
        Logger.logError("Getting all seasons Failed");
        return null;
    }
    public List<String> getAllScorePolicies(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all Score Policies");
                return ((UnionRepresentative) role).getAllScorePolicies();
            }
        }
        Logger.logError("Getting all score policies Failed");
        return null;
    }

    public List<String> getAllDetailsAboutOpenTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all details of open teams");
                return role.getAllDetailsAboutOpenTeams();
            }
        }
        Logger.logError("Getting all details of open teams Failed");
        return null;
    }

    public List<String> getAllOpenTeams(String userId){
        User user = UserFactory.getUser(userId);
        if(user!=null) {
            Role role = user.checkUserRole("UnionRepresentative");
            if (role instanceof UnionRepresentative) {
                Logger.logEvent(user.getID(), "Got all Open Teams");
                return role.getAllOpenTeams();
            }
        }
        Logger.logError("Getting all Open Teams Failed");
        return null;
    }

    public List<String> getAllPastGames(String userID){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("UnionRepresentative");
        if(role instanceof  UnionRepresentative ) {
            Logger.logEvent(user.getID(), "Got all Past Games");
            return  ((UnionRepresentative)role).getAllPastGames();
        }
        Logger.logError("Getting all Past Games Failed");
        return null;
    }

    public List<String> getAllReferees(String userID){
        User user= UserFactory.getUser(userID);
        Role role = user.checkUserRole("UnionRepresentative");
        if(role instanceof  UnionRepresentative ) {
            Logger.logEvent(user.getID(), "Got all Referees");
            return ((UnionRepresentative)role).getAllReferees();
        }
        Logger.logError("Getting all referees Failed");
        return null;
    }

    public List<String> getAllAssignmentsPolicies(String userId) {
        User user= UserFactory.getUser(userId);
        Role role = user.checkUserRole("UnionRepresentative");
        if(role instanceof  UnionRepresentative ) {
            Logger.logEvent(user.getID(), "Got all Assignments Policies");
            return ((UnionRepresentative)role).getAllAssignmentsPolicies();
        }
        Logger.logError("Getting all assignments policies Failed");
        return null;
    }
}
