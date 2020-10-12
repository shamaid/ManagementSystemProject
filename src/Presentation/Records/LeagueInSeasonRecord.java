package Presentation.Records;

public class LeagueInSeasonRecord implements Record {

    private String id;
    private String league;
    private String season;

    public LeagueInSeasonRecord(String toString) {
        String[] split = toString.split(",");
        id = split[0];
        league = split[1].substring(split[1].indexOf("=")+1);
        season = split[2].substring(split[2].indexOf("=")+1);
    }

    public String getLeague() {
        return league;
    }

    public String getSeason() {
        return season;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return league + " " + season;
    }
}
