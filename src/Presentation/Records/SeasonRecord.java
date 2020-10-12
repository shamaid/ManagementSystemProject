package Presentation.Records;

public class SeasonRecord implements Record {

    private String id;
    private String year;

    public SeasonRecord(String seasonToString){
        String[]split = seasonToString.split(":");
        this.id = split[0];
        this.year = split[1];


    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return year;
    }

    public String getYear() {
        return year;
    }
}
