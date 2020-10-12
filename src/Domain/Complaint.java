package Domain;

import Data.Database;

import java.util.Date;
import java.util.Observable;

public class Complaint extends Observable {

    private String id;
    private Date date;//also shows time
    private String description;
    private boolean isActive;
    private Fan fanComplained;
    private String response;

    public Complaint(String description, Fan fanComplained) {
        this.id = "Complaint"+IdGenerator.getNewId();
        this.date = Database.getCurrentDate();
        this.description = description;
        this.fanComplained = fanComplained;
        this.addObserver(fanComplained);
        isActive = true;
        this.response = "";
    }

    public Complaint(String id, Date date, boolean isActive, String description, Fan fanComplained, String response)
    {
        this.id = id;
        this.date = date;
        this.description = description;
        this.fanComplained = fanComplained;
        this.addObserver(fanComplained);
        this.isActive = isActive;
        this.response = response;
    }

    public void deactivate(){
        isActive = false;
    }

    // ++++++++++++++++++++++++++++ Functions ++++++++++++++++++++++++++++
    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Fan getFanComplained() {
        return fanComplained;
    }

    public void setResponse(String response) {
        this.response = response;
        this.isActive = false;
        setChanged();
        notifyObservers(Database.getCurrentDate() + ":Response to your complaint from the date"+this.date+": \n"+response);
    }

    public String getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }
}
