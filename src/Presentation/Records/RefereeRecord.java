package Presentation.Records;

public class RefereeRecord implements Record {

    private String id;
    private String name;
    private String training;

    public RefereeRecord(String refereeToString){

        String[]split = refereeToString.split(",");
        this.id = split[1].substring(split[1].indexOf("=")+1,split[1].indexOf(":"));
        split[1] = split[1].substring(split[1].indexOf(":"));
        this.name = split[1].substring(split[1].indexOf("=")+1);
        this.training = split[2].substring(split[2].indexOf("=")+1);

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
}
