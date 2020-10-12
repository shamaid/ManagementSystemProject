package Domain;

import java.util.List;
import java.util.LinkedList;
import java.util.Objects;

public class League {

    public enum LevelLeague {
        level1,
        level2,
        level3,
        level4,
        level5
    }

    private String id;
    private String name;
    private LevelLeague level;
    private List<LeagueInSeason> leagueInSeasons;

    /*
    create each league once and for every season add new leagueInSeason
     */
    public League(String name, LevelLeague level) {
        this.name = name;
        this.level = level;
        this.id = "L" + IdGenerator.getNewId();
        leagueInSeasons = new LinkedList<>();
    }

    public League(String id, String name, LevelLeague level, List<LeagueInSeason> lis)
    {
        this.id = id;
        this.name = name;
        this.level = level;
        this.leagueInSeasons = lis;
    }

    public League(String id, String name, LevelLeague level)
    {
        this.id = id;
        this.name = name;
        this.level = level;
        this.leagueInSeasons = new LinkedList<>();
    }

    public void addLeagueInSeason(LeagueInSeason leagueInSeason){
        leagueInSeasons.add(leagueInSeason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLevel());
    }

    @Override
    public String toString() {
        return  id+
                ":" + name +
                "," + level;
    }

    // ++++++++++++++++++++++++++++ getter ++++++++++++++++++++++++++++

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level.toString();
    }

    public String getId() {
        return id;
    }

    public List<LeagueInSeason> getLeagueInSeasons() {
        return leagueInSeasons;
    }
}
