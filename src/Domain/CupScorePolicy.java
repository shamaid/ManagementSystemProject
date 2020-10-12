package Domain;

import java.util.List;

public class CupScorePolicy implements ScorePolicy {
    @Override
    public void calculateScore(Game game) {
        int guest = game.guestScore();
        int host = game.hostScore();

        if(guest>host){
            game.getGuestTeam().addAWin();
        }
        else if(host>guest){
            game.getGuestTeam().addALoss();
        }
    }
    public String getName() {
        return "CupScorePolicy";
    }
    @Override
    public void calculateLeagueScore(LeagueInSeason league) {
        List<Team> teams = league.getTeams();
        for(Team team: teams){
            int score = team.getWins();//1 points for win
            ScoreTableRecord record = new ScoreTableRecord(team, score);
            league.addScoreTableRecord(record);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CupScorePolicy) return true;
        return false;
    }
}
