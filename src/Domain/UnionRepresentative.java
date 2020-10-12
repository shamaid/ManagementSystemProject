package Domain;

import Data.Database;
import Logger.Logger;
import Service.ProxyAccountingSystem;
import java.util.*;

public class UnionRepresentative extends Role implements Observer {

    public UnionRepresentative(String userId) {
        this.userId = userId;
        myRole = "UnionRepresentative";

    }

    public String configureNewLeague(String name, League.LevelLeague level) {
        League league = new League(name, level);
        if(Database.addLeague(league))
            return league.getId();
        return null;
    }

    public String configureNewSeason(int year, Date startDate) {
        try{
            if (Database.getCurrentYear()<=year) {
                Season season = new Season(year, startDate);
                if(Database.addSeason(season))
                    return season.getId();
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }

    public LeagueInSeason configureLeagueInSeason(String leagueId, String seasonId, String assignmentPolicy, String scorePolicy, double registrationFee) {
        League league = Database.getLeague(leagueId);
        Season season = Database.getSeason(seasonId);

        if(league!=null && season!=null && assignmentPolicy!=null && scorePolicy!=null) {
            GameAssignmentPolicy gameAssignmentPolicy = GameAssignmentPolicy.checkPolicy(assignmentPolicy);
            ScorePolicy gameScorePolicy = ScorePolicy.checkPolicy(scorePolicy);
            if (gameAssignmentPolicy != null && gameScorePolicy != null) {
                if(Database.getLeagueInSeason(league.getId()+season.getId())==null) {
                    LeagueInSeason leagueInSeason = new LeagueInSeason(gameAssignmentPolicy, gameScorePolicy, league, season, registrationFee);
                    league.addLeagueInSeason(leagueInSeason);
                    Database.updateObject(league);
                    season.addLeagueInSeason(leagueInSeason);
                    Database.updateObject(season);
                    Database.addLeagueInSeason(leagueInSeason);
                    return leagueInSeason;
                }
            }
        }
        return null;
    }

    public boolean assignGames(String leagueId) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        List<Game> games =league.getAssignmentPolicy().assignGames(league);
        if(games!=null){
            league.setGamesId(games);
            return true;
        }
        return false;
    }

    public User appointReferee(String firstName, String lastName, String mail, Referee.TrainingReferee training)
    {
        return UserFactory.getNewReferee(firstName, lastName, mail, training);
    }

    public boolean removeAppointReferee(Referee referee)
    {
        if(referee.getAllOccurringGame()==null){
            Database.getUser(referee.getID()).getRoles().remove(referee);
            return Database.updateObject(referee);
        }
        return false;
    }

    public boolean addRefereeToLeague(String leagueId, Referee referee)
    {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        return league.addReferee(referee);
    }


    public boolean changeScorePolicy(String leagueId, String policy)
    {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        ScorePolicy scorePolicy = ScorePolicy.checkPolicy(policy);
        if(league!=null && scorePolicy!=null)
            return league.changeScorePolicy(scorePolicy);
        return false;
    }

    public boolean changeAssignmentPolicy(String leagueId, String policy)
    {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        GameAssignmentPolicy assignmentPolicy = GameAssignmentPolicy.checkPolicy(policy);
        if(league!=null && assignmentPolicy!=null)
            return league.changeAssignmentPolicy(assignmentPolicy);
        return false;
    }

    public boolean addTUTUPayment(String teamId, double payment) {
        Team team = Database.getTeam(teamId);
        if(team!=null) {
            if(team.addIncome(payment)){
                Database.updateObject(team);
                return true;
            }
        }
        return false;
    }

    public boolean changeGameDate(String gameId, Date newDate) {
        Game game = Database.getGame(gameId);
        if(game.getDate().after(Database.getCurrentDate())) {
            game.setDate(newDate);
            Database.updateObject(game);
            return true;
        }
        return false;
    }

    public boolean changeGameLocation(String gameId, String fieldId) {
        Game game = Database.getGame(gameId);
        Field field = Database.getField(fieldId);
        if(field!=null && field.isActive()) {
            game.setField(field);
            Database.updateObject(game);
            return true;
        }
        return false;
    }

    public boolean calculateLeagueScore(String leagueId) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        if(league!=null) {
            league.getScorePolicy().calculateLeagueScore(league);
            Database.updateObject(league);
            return true;
        }
        return false;
    }

    public boolean changeRegistrationFee(String leagueId, double newFee) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        if(league!=null){
            league.changeRegistrationFee(newFee);
            Database.updateObject(league);
            return true;
        }
        return false;
    }

    public boolean calculateGameScore(String leagueId, String gameId) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        Game game= Database.getGame(gameId);
        if(league!=null && game!=null) {
            league.getScorePolicy().calculateScore(game);
            Database.updateObject(league);
            return true;
        }
        return false;
    }

    public boolean addTeamToLeague(String leagueId, String teamId, ProxyAccountingSystem proxyAccountingSystem) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        Team team = Database.getTeam(teamId);
        if(team!=null && league!=null && team.isActive()) {
            if (team.addExpanse(league.getRegistrationFee())) {
                proxyAccountingSystem.addPayment(team.getName(),(new Date()).toString() ,league.getRegistrationFee());
                league.addATeam(team);
                Database.updateObject(league);
                Logger.logEvent(userId, "Added team " + team.getName() + " to league");
                return true;
            }
        }
        Logger.logError("Adding Team to league failed");
        return false;
    }

    public boolean addFieldToSystem(String location,String fieldName, int capacity, double price){
        Field field = new Field(location, fieldName, capacity, price);
        return Database.addAsset(field);
    }

    @Override
    public String toString() {
        return "UnionRepresentative" +
                ", id=" + userId +
                ": name=" +Database.getUser(userId).getName();
    }

    @Override
    public void update(Observable o, Object arg) {
        String news = (String)arg;
        Database.getUser(userId).addMessage(news);
    }

    // ++++++++++++++++++++++++++++ getter ++++++++++++++++++++++++++++

    public double getRegistrationFee(String leagueId) {
        LeagueInSeason league = Database.getLeagueInSeason(leagueId);
        if(league!=null)
            return league.getRegistrationFee();
        return -1;
    }

    public List<String> getAllReferees() {
        List<String> referees = new LinkedList<>();
        for (Referee referee : Database.getAllReferees())
            referees.add(referee.toString());
        return referees;
    }

    public List<String> allLeaguesInSeasons() {
        List<String> allLeagues= new LinkedList<>();
        for(League league: Database.getLeagues()){
            for(LeagueInSeason leagueInSeason : league.getLeagueInSeasons())
                allLeagues.add(leagueInSeason.toString());
        }

        return allLeagues;
    }

    public List<String> getAllScorePolicies() {
        List<String> scorePolicies= new LinkedList<>();
        for(ScorePolicy s: Database.getAllScorePolicies()){
            scorePolicies.add(s.getName());
        }
        return scorePolicies;
    }
    public List<String> getAllAssignmentsPolicies() {
        List<String> assignmentsPolicies= new LinkedList<>();
        for(GameAssignmentPolicy a: Database.getAllAssignmentsPolicies()){
            assignmentsPolicies.add(a.getName());
        }
        return assignmentsPolicies;
    }

    public List<String> getAllLeagues() {
        List<String> leagues = new LinkedList<>();
        for(League l: Database.getLeagues()) {
            leagues.add(l.toString());
        }
        return leagues;
    }

    public List<String> getAllSeasons() {
        List<String> seasons = new LinkedList<>();
        for(Season s: Database.getSeasons()) {
            seasons.add(s.toString());
        }
        return seasons;
    }

}
