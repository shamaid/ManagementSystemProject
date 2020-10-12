package Domain;

import Data.Database;
import java.util.*;

public class Game extends Observable {

    private String id;
    private String name;
    private Date date;
    private int hostScore;//number of goals
    private int guestScore;//number of goals
    private Field field;
    private Referee mainReferee; // exactly 1, check type of referee
    private List<Referee> sideReferees; // between 2 and 6, check type of referee
    private Team hostTeam; // check type of team
    private Team guestTeam; // check type of team
    private HashMap<Fan, Boolean> fansForAlerts; //list of fans that signed up to receive game alerts
    private EventReport eventReport;
    private LeagueInSeason league;


    public Game(Date date, Field field, Referee mainReferee, List<Referee> sideReferees,
                Team hostTeam, Team guestTeam, LeagueInSeason league) {
        this.league = league;
        this.id = "G"+IdGenerator.getNewId();
        this.date = date;
        this.field = field;
        this.mainReferee = mainReferee;
        if(sideReferees==null||sideReferees.size()<2)
            throw new RuntimeException("not enough side referees");
        this.sideReferees = sideReferees;
        addRefereeToObservers(mainReferee, sideReferees);
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.name = hostTeam.getID() + ":" + hostTeam.getName() + " VS " + guestTeam.getID() + ":" + guestTeam.getName();
        eventReport = new EventReport();
        Database.addEventReport(eventReport);
        hostScore=0;
        guestScore=0;
        fansForAlerts = new HashMap<>();
    }

    public Game(String id, Date date, int hostScore, int guestScore, Field field, Referee mainReferee, List<Referee> sideReferees, Team hostTeam, Team guestTeam, HashMap<Fan,Boolean> fansAlerts, EventReport eventReport, LeagueInSeason lis)
    {
        this.id = id;
        this.date = date;
        this.hostScore = hostScore;
        this.guestScore = guestScore;
        this.field = field;
        this.mainReferee = mainReferee;
        this.sideReferees = sideReferees;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.name = hostTeam.getID() + ":" + hostTeam.getName() + " VS " + guestTeam.getID() + ":" + guestTeam.getName();
        this.fansForAlerts = fansAlerts;
        this.eventReport = eventReport;
        this.league = lis;
    }

    // ++++++++++++++++++++++++++++ Functions ++++++++++++++++++++++++++++
    @Override
    public String toString() {
        return id +","+name +","+date;
    }

    @Override
    public boolean equals(Object obj) {
        Game game = (Game)obj;
        if(game!=null)
            return game.getId().equals(this.getId());
        return false;
    }

    private void addRefereeToObservers(Referee mainReferee, List<Referee> sideReferees) {
        this.addObserver(mainReferee);
        this.addObserver(sideReferees.get(0));
        this.addObserver(sideReferees.get(1));
    }

    /**
     * @param fan - signed up for game alerts
     * @return true- if the fan is added to list to receive game alerts
     */
    public boolean addFanForNotifications(Fan fan, boolean toMail) {
        if(!fansForAlerts.containsKey(fan)) {
            fansForAlerts.put(fan, toMail);
            this.addObserver(fan);
            Database.updateObject(this);
            return true;
        }
        return false;
    }

    public void setNews(String news) {
        setChanged();
        news = "New Alert for game "+this.name+":\n"+ Database.getCurrentDate() + ": " + news;
        notifyObservers(news);
        sendMailToFan(news);
    }

    /*alert for game, sent to mail if fan want get alert to mail*/
    private void sendMailToFan(String news) {
        for (Fan fan : fansForAlerts.keySet())
            if (fansForAlerts.get(fan))
                MailSender.send(Database.getUser(fan.getID()).getMail(), news);
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public List<String> getEventReportString() {
        List<String> events = new LinkedList<>();
        for(Event event : eventReport.getEvents())
            events.add(event.toString());
        return events;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    /*
    number of goals
     */
    public int hostScore() {
        return hostScore;
    }
    /*
    number of goals
    */
    public int guestScore() {
        return guestScore;
    }

    public void setHostScore(int hostScore) {
        this.hostScore = hostScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public Referee getMainReferee() {
        return mainReferee;
    }

    public List<Referee> getSideReferees() {
        return sideReferees;
    }

    public Team getHostTeam() {
        return hostTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public EventReport getEventReport() {
        return eventReport;
    }

    public String getSideRefereesId(){
        String listOfId = "";
        for (Referee referee: sideReferees) {
            if(listOfId.equals("")){
                listOfId = listOfId+referee.getID();
            }
            else {
                listOfId = listOfId + ","+referee.getID();
            }
        }
        return listOfId;
    }

    public String getAlertsFansId(){
        String listOfId = "";
        for (Fan fan: fansForAlerts.keySet()) {
            if(listOfId.equals("")){
                listOfId = listOfId+fan.getID();
            }
            else {
                listOfId = listOfId + ","+fan.getID();
            }
        }
        return listOfId;
    }

    public void setDate(Date newDate) {
        this.date = newDate;
        setNews("Date of the game between the teams: " +this.name+ " change to "+this.date); // referees and fans
    }

    public void setField(Field newField) {
        this.field = newField;
        setNews("Location of the game between the teams: " +this.name+ " change to "+this.field.getName()); // referees and fans
    }

    public boolean setNewsFromReferee(String news){
        setChanged();
        news = "New Alert for game "+this.name+":\n" + news;
        for (Fan fan : fansForAlerts.keySet())
            fan.update(this, news);
        sendMailToFan(news);
        return true;
    }

    public LeagueInSeason getLeague() {
        return league;
    }

    public HashMap<Fan, Boolean> getFansForAlerts() {
        return fansForAlerts;
    }

}
