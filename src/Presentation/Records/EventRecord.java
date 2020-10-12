package Presentation.Records;

public class EventRecord implements Record {


    private String id;
    private String type;
    private String time;
    private String minuteInGame;
    private String name;

    public EventRecord(String toString) {
        String[] split = toString.split(",");
        this.id = split[1].substring(split[1].indexOf("=")+1);
        this.type = split[2].substring(split[2].indexOf("=")+1);
        this.time = split[3].substring(split[3].indexOf("=")+1);
        this.minuteInGame = split[4].substring(split[4].indexOf("=")+1);
        this.name = split[5].substring(split[5].indexOf("=")+1).replace("}","");
    }

    @Override
    public String getId() {
        return id;//??
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getMinuteInGame() {
        return minuteInGame;
    }
}
