package Presentation.Records;

public class CoachRecord implements Record {

    private String id;
    private String name;
    private String training;
    private String roleInTeam;
    private String teams;

    public CoachRecord(String coachToString){
        String[]split = coachToString.split(",");
        this.id = split[1].substring(split[1].indexOf("=")+2, split[1].indexOf(":"));
        split[1] = split[1].substring(split[1].indexOf(":")+2);
        this.name = split[1].substring(split[1].indexOf("=")+1);
        this.training = split[2].substring(split[2].indexOf("=")+1);
        this.roleInTeam = split[3].substring(split[3].indexOf("=")+1);
        this.teams = split[4].substring(split[4].indexOf("=")+1);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTraining() {
        return training;
    }

    public String getRoleInTeam() {
        return roleInTeam;
    }

    public String getTeams() {
        return teams;
    }
}
