package Presentation.Records;

public class LeagueRecord implements Record{

    private String id;
    private String name;
    private String level;

    public LeagueRecord(String league) {
        String[]split = league.split(":");
        this.id=split[0];
        String[] split1 =split[1].split(",");
        this.name = split1[0];
        this.level = "Level "+split1[1];
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }
}
