package Domain;

import Data.Database;
import java.util.*;

public class Referee extends Role implements Observer {

    public enum TrainingReferee{
        referees,
        linesman,
        var
    }

    private TrainingReferee training;
    private HashSet<String> gamesId;

    public Referee(String userId, TrainingReferee training) {
        this.userId = userId;
        this.training = training;
        gamesId = new HashSet<>();
        this.myRole = "Referee";
    }

    public Referee(String userId, String training, HashSet<String> gamesId)
    {
        this.userId = userId;
        this.training = TrainingReferee.valueOf(training);
        this.gamesId = gamesId;
        this.myRole = "Referee";
    }

    public boolean addGame(String game) {
        if(!gamesId.contains(game)) {
            this.gamesId.add(game);
            return true;
        }
        return false;
    }

    public HashSet<String> viewGames(){return gamesId;}


    public Boolean addEventToGame(String gameID, Event.EventType type, String playerId, String teamId)
    {
        Game game= Database.getGame(gameID);
        Team team = Database.getTeam(teamId);
        Player player = Database.getPlayer(playerId);
        if(game!=null && team!=null && player!=null) {
            String description = Database.getUser(playerId).getName() + " from team "+ team.getName();
            Date date = new Date();
            Event event = new Event(type,date, calculateMinutes(game.getDate(), date), description);
            if(game.getEventReport().addEvent(event)) {
                game.setNewsFromReferee(event.createMessage());
                Database.updateObject(game);
                return true;
            }
        }
        return false;
    }

    private double calculateMinutes(Date gameDate, Date nowDate) {
        long now = (nowDate.getTime()/1000)/60 + (nowDate.getTime()/1000)%60;
        long gameTime = (gameDate.getTime()/1000)/60 + (gameDate.getTime()/1000)%60;
        long minutes = now-gameTime;
        return minutes;
    }

    public boolean changeEvent(String gameID, String eventID, String change){
        Game game= Database.getGame(gameID);
        Event event=game.getEventReport().gerEventById(eventID);
        if (event!=null && this.equals(game.getMainReferee())) {
            double minute = calculateMinutes(game.getDate(), new Date());
            if(minute>120 && minute<=420) {
                for (Event event1 : game.getEventReport().getEvents()) {
                    if (event1.getId().equals(event.getId())) {
                        event1.setDescription(change);
                        Database.updateObject(game.getEventReport());
                        Database.updateObject(game);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Referee" +
                ", id="+userId+
                ": name="+Database.getUser(userId).getName()+
                ", training=" + training;
    }
    @Override
    public void update(Observable o, Object arg) {
        if(!(arg instanceof Event))
            Database.getUser(userId).addMessage((String)arg);
    }

    @Override
    public boolean equals(Object obj) {
        Referee refObj = (Referee)obj;
        if(refObj!=null)
            return refObj.getID().equals(this.getID());
        return false;
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public boolean setScoreInGame(String gameID,int hostScore, int guestScore)
    {
        Game game= Database.getGame(gameID);
        if(this.equals(game.getMainReferee())){
            game.setGuestScore(guestScore);
            game.setHostScore(hostScore);
            String message = game.getName()+": "+ game.getHostTeam()+": "+hostScore+"-"+game.getGuestTeam()+": "+guestScore;
            game.setNewsFromReferee(message);
            Database.updateObject(game);
            return true;
        }
        return false;
    }

    public List<String> getGameReport(String gameID) {
        List<String> gameReport = new LinkedList<>();
        Game game= Database.getGame(gameID);
        if(game.getMainReferee().equals(this) && calculateMinutes(game.getDate(), new Date())>120) {
            gameReport.addAll(game.getEventReportString());
        }
        return gameReport;
    }

    public String getAllOccurringGame() {
        String gameString = Database.getAllOccurringGame();
        if(gameString!=null){
            Game game = Database.getGame(gameString.split(",")[0]);
            double minute = calculateMinutes(game.getDate(), new Date());
            if(this.equals(game.getMainReferee())) {
                if (minute > 0 && minute <= 420) {
                    return gameString;
                }
            }
            else {
                if(minute>0 && minute<=120){
                    return gameString;
                }
            }
        }
        return null;
        }

    public String getTraining() {
        return training.toString();
    }

    public void setTraining(TrainingReferee training) {
        this.training = training;
        Database.updateObject(this);
    }
}
