package Presentation.Records;

public class GameRecord implements Record {


    private String id;
    private String name;
    private String date;
    private String follows;
    private String team1Id;
    private String team2Id;
    private String team1Name;
    private String team2Name;

    public GameRecord(String toString) {
        //G1593,T1025:team0 VS T1067:team1,Sun May 31 01:00:00 IDT 2020
        String[] split = toString.split(",");
        this.id = split[0];
        this.team1Id = split[1].substring(0,split[1].indexOf(":"));
        split[1] = split[1].substring(split[1].indexOf(":")+1);
        this.team1Name = split[1].substring(0,split[1].indexOf("VS")-1);
        split[1] = split[1].substring(split[1].indexOf("VS")+3);
        this.team2Id = split[1].substring(0,split[1].indexOf(":"));
        this.team2Name = split[1].substring(split[1].indexOf(":")+1);

        this.name = team1Name + " VS " + team2Name;
        this.date = split[2];
        if(split.length>4 && split[3].equals("+"))
            this.follows = " you follow this game";
        else
            this.follows = "";
    }

    public String getTeam1Id() {
        return team1Id;
    }

    public String getTeam2Id() {
        return team2Id;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public String getDate() {
        return date;
    }

    public String getFollows() {
        return follows;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name + ", " + date;
    }
}
