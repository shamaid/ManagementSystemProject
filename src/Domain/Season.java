package Domain;

import Data.Database;
import java.util.List;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Date;

public class Season {

    private String id;
    private int year;
    private Date startDate;
    private List<LeagueInSeason> leagueInSeasons;

    public Season(int year, Date startDate) {
        int now = Database.getCurrentYear();
        if(year < now - 3)//allows to define season only in the future or the last three years
            throw new RuntimeException("year is not valid!");
        this.year = year;
        this.startDate = startDate;
        this.id = "S"+IdGenerator.getNewId();
        leagueInSeasons = new LinkedList<>();
    }

    public Season(String id, int year, Date date, List<LeagueInSeason> lis)
    {
        this.id = id;
        this.year = year;
        this.startDate = date;
        this.leagueInSeasons = lis;
    }

    public Season(String id, int year, Date date)
    {
        this.id = id;
        this.year = year;
        this.startDate = date;
        this.leagueInSeasons =new LinkedList<>();
    }

    @Override
    public String toString() {
        return
                id+
                        ":" + year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Season)) return false;
        Season season = (Season) o;
        return getYear() == season.getYear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear());
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public Date getStartDate() {
        return startDate;
    }

    public int getYear() {
        return year;
    }

    public List<LeagueInSeason> getLeagueInSeasons() {
        return leagueInSeasons;
    }

    public String getId() {
        return id;
    }

    public void addLeagueInSeason(LeagueInSeason leagueInSeason){
        leagueInSeasons.add(leagueInSeason);
    }
}
