package Domain;

public class ScoreTableRecord
{
    private Team team;
    private int totalScore;

    public ScoreTableRecord(Team team, int totalScore) {
        this.team = team;
        this.totalScore = totalScore;
    }

    // ++++++++++++++++++++++++++++ Functions ++++++++++++++++++++++++++++

    public Team getTeam() {
        return team;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
