package Domain;

import java.util.*;

public class StandardScorePolicy implements ScorePolicy {
    @Override
    public void calculateScore(Game game) {

        int guest = game.guestScore();
        int host = game.hostScore();
        if (guest == host){
            game.getGuestTeam().addADraw();
            game.getHostTeam().addADraw();
        }
        else if(guest>host){
            game.getGuestTeam().addAWin();
            game.getHostTeam().addALoss();
        }
        else{
            game.getGuestTeam().addALoss();
            game.getHostTeam().addAWin();
        }

    }

    public void calculateLeagueScore(LeagueInSeason league) {
         List<Team> teams = league.getTeams();
         for(Team team: teams){
             int score = team.getWins()*3 + team.getDraws();//3 points for win, 1 point for draw, 0 points for loss
             ScoreTableRecord record = new ScoreTableRecord(team, score);
             league.addScoreTableRecord(record);
         }
    }

    @Override
    public String getName() {
        return "StandardScorePolicy";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StandardScorePolicy) return true;
        return false;
    }
}
