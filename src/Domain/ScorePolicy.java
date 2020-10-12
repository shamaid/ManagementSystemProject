package Domain;

public interface ScorePolicy {

    static ScorePolicy checkPolicy(String scorePolicy) {
        switch (scorePolicy){
            case "StandardScorePolicy":
                return new StandardScorePolicy();
            case "CupScorePolicy":
                return new CupScorePolicy();
        }
        return null;
    }

    void calculateScore(Game game);
    void calculateLeagueScore(LeagueInSeason league);
    String getName();

}
