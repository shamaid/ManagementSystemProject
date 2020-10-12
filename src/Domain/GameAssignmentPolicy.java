package Domain;

import Data.Database;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

public abstract class GameAssignmentPolicy {
    String name;

    public static GameAssignmentPolicy checkPolicy(String assignmentPolicy) {
        switch (assignmentPolicy){
            case "PlayOnceWithEachTeamPolicy":
                return new PlayOnceWithEachTeamPolicy();
            case "PlayTwiceWithEachTeamPolicy":
                return new PlayTwiceWithEachTeamPolicy();
        }
        return null;
    }
    public String getName(){
        return name;
    }
    public abstract List<Game> assignGames(LeagueInSeason league);

     protected boolean checkForDuplicates(List<Game> games, Team team1, Team team2) {
          for(Game game : games){
               if(game.getGuestTeam().equals(team1) && game.getHostTeam().equals(team2))
                    return false;
          }
          return true;
     }

     /*
     for now this function returns 2 side referees
     */
     protected List<Referee> getSideReferees(List<Referee> referees, Referee main) {
          List<Referee> sideReferees = new LinkedList<>();
          int index ;
          Referee ref1, ref2;
          do{
               index = (int)((Math.random())*referees.size())%referees.size();
               ref1 = referees.get(index);
          }
          while(ref1.equals(main));
          sideReferees.add(ref1);
          do{
               index = (int)((Math.random())*referees.size())%referees.size();
               ref2 = referees.get(index);
          }
          while(ref2.equals(ref1)|| ref2.equals(main));
          sideReferees.add(ref2);
          return sideReferees;
     }

     protected Referee getMainReferee(List<Referee> referees) {
          int index = (int)((Math.random())*referees.size())%referees.size();
          Referee main = referees.get(index);
          return main;

     }

     protected Date getDateFromList(List<Date> dates) {
          int index = (int)((Math.random())*dates.size())%dates.size();
          Date date = dates.get(index);
          dates.remove(date);
          return date;
     }

     protected void checkConstrains( List<Team> teams, List<Referee> referees) {
          if(referees.size()<3) throw new RuntimeException("not enough referees available to assign games");
          if(teams.size()<13) throw new RuntimeException("not enough teams available to assign games");
     }


     protected Game makeGame(List<Referee> referees, Team team1, Team team2, List<Date> dates, LeagueInSeason league) {
          List<Referee> sideRefs;
          Referee mainRef = getMainReferee(referees);
          sideRefs = getSideReferees(referees,mainRef);
          Game game = new Game(getDateFromList(dates), team1.getField(), mainRef,sideRefs, team1,team2, league);
          mainRef.addGame(game.getId());
          Database.updateObject(mainRef);
          for (Referee side: sideRefs){
              side.addGame(game.getId());
              Database.updateObject(side);
          }
          team1.addGame(game);
          team1.addLeague(league);
          Database.updateObject(team1);
          team2.addGame(game);
          team2.addLeague(league);
          Database.updateObject(team2);
          league.addGame(game);
          Database.updateObject(league);
          return game;
     }
     protected List<Date> getDates(int seasonYear, int listSize)
     {
         LinkedList<Date> dates = new LinkedList<>();
         for (int i = 1; i < 30; i++) {
             dates.add(Database.getDate(seasonYear, 5, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i < 31; i++) {
             dates.add(Database.getDate(seasonYear, 6, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i <31 ; i++) {
             dates.add(Database.getDate(seasonYear, 7, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i <30 ; i++) {
             dates.add(Database.getDate(seasonYear, 8, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i <31 ; i++) {
             dates.add(Database.getDate(seasonYear, 9, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i <30 ; i++) {
             dates.add(Database.getDate(seasonYear, 10, i, 20, 0));
         }
         if(dates.size()>=listSize) return dates;
         for (int i = 1; i <31 ; i++) {
             dates.add(Database.getDate(seasonYear, 11, i, 20, 0));
         }
         return dates;
     }


}
