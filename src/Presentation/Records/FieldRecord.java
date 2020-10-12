package Presentation.Records;

public class FieldRecord implements Record {

    private String id;
    private String name;
    private String location;
    private String capacity;


    public FieldRecord(String toString) {
        String[]split = toString.split(",");
        this.id = split[1].substring(split[1].indexOf("=")+1, split[1].indexOf(":"));
        split[1] = split[1].substring(split[1].indexOf(":")+1);
        this.name = split[1].substring(split[1].indexOf("=")+1);
        this.location = split[2].substring(split[2].indexOf("=")+1);
        this.location = split[3].substring(split[3].indexOf("=")+1);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCapacity() {
        return capacity;
    }

    @Override
    public String getId() {
        return id;
    }
}
