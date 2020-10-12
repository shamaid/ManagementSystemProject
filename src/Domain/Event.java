package Domain;

import Data.Database;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Event {

    public enum EventType {
        Undefined,
        Goal,
        Offside,
        Foul,
        RedCard,
        YellowCard,
        Injury,
        Replacement
    }
    private String id;
    private EventType type;
    private Date date;
    private double minuteInGame;
    private String description;


    public Event(EventType type,Date date, double minuteInGame, String description) {
        this.id = "E"+IdGenerator.getNewId();
        this.type = type;
        this.date = date;
        this.minuteInGame = minuteInGame;
        this.description = description;
    }

    public Event(String id, String type, Date date, double minuteInGame, String description)
    {
        this.id = id;
        this.type = EventType.valueOf(type);
        this.date = date;
        this.minuteInGame = minuteInGame;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", id=" + id +
                ", type=" + type +
                ", date=" + date +
                ", minuteInGame=" + minuteInGame +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Event event = (Event)obj;
        if(event!=null)
            return this.getId().equals(event.getId()) || (this.getType().equals(event.getType()) && this.getDescription().equals(event.getDescription()));
        return false;
    }

    public String createMessage(){
        return date + ": " + minuteInGame + ", " + type + "- " + description;
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public EventType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public double getMinuteInGame() {
        return minuteInGame;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        Database.updateObject(this);
    }

    public String getId() {
        return id;
    }
}
