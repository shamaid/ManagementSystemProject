package Presentation.Records;

public class TeamRecord implements Record{

    private String id;
    private String name;

    public TeamRecord(String teamToString){
        String[]split = teamToString.split(":");
        this.id = split[0].substring(split[0].indexOf("=")+1);
        this.name = split[1].substring(split[1].indexOf("=")+1);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
