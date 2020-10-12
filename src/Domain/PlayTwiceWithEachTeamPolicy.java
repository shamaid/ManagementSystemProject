package Domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PlayTwiceWithEachTeamPolicy extends GameAssignmentPolicy {

    public PlayTwiceWithEachTeamPolicy(){
        name="PlayTwiceWithEachTeamPolicy";
    }

    @Override
    public List<Game> assignGames(LeagueInSeason league) {
        if(league.getTeams().size()>2){
            List<Date> dates = getDates(league.getSeason().getYear(), findOutHowManyDatesRequired(league.getTeams().size()));
            List<Referee> referees = league.getReferees();
            checkConstrains(league.getTeams(), referees);
            List<Game> games = new LinkedList<>();

            for(Team team1 : league.getTeams()){
                for(Team team2: league.getTeams()){
                    if(team1.isActive()&&team2.isActive()&&!team1.equals(team2)){
                        games.add(makeGame(referees, team1, team2, dates,league));
                    }
                }
            }
            return games;
        }
        return null;
    }

    private int findOutHowManyDatesRequired(int size) {
        int res = (size)*(size-1);
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PlayTwiceWithEachTeamPolicy);
    }

}
