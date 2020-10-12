package Domain;

import Data.Database;
import java.util.*;

public class Fan extends Role implements Observer {

    private String address;
    private String phone;
    private List<String> complaintsId;
    private List<PersonalPage> followPages;

    public Fan(String userId, String phone, String address) {
        this.userId = userId;
        this.address = address;
        this.phone = phone;
        this.complaintsId = new LinkedList<>();
        this.followPages = new LinkedList<>();
        this.myRole = "Fan";
    }

    public Fan(String userId, String address, String phone, List<PersonalPage> personalPages, List<String> complaintsId)
    {
        this.userId = userId;
        this.address = address;
        this.phone = phone;
        this.followPages = personalPages;
        this.complaintsId = complaintsId;
        this.myRole = "Fan";
    }

    public boolean addPageToFollow(String pageId){
        PersonalPage personalPage = Database.getPersonalPage(pageId);
        if(!followPages.contains(personalPage)){
            followPages.add(personalPage);
            Database.updateObject(this);
            personalPage.addAFollower(this);
            return true;
        }
        return false;
    }

    public void editPersonalInfo(User user, String firstName, String lastName, String address, String phone){
        user.editPersonalInfo(firstName, lastName);
        this.address = address;
        this.phone = phone;
        Database.updateObject(this);
    }

    public boolean registrationForGamesAlerts(List<String> gamesId , boolean receiveAlerts){
        for(String gameId: gamesId) {
            Game game = Database.getGame(gameId);
            if(!game.addFanForNotifications(this, receiveAlerts))
                return false;
        }
        return true;
    }
    public boolean submitComplaint(String description)
    {
        if(description.length()<1)
            return false;
        Complaint complaint = new Complaint(description, this);
        complaintsId.add(complaint.getId());
        Database.updateObject(this);
        return Database.addComplaint(complaint);
    }

    @Override
    public void update(Observable o, Object arg) {
        String news = (String)arg;
        Database.getUser(userId).addMessage(news);
    }

    @Override
    public String toString() {
        return "Fan" +
                ", address=" + address +
                ", phone=" + phone;
    }

    public String getUserInfo() {
        return "Fan" +
                ", firstName=" + Database.getUser(userId).getFirstName() +
                ", lastName=" + Database.getUser(userId).getLastName() +
                ", phone=" + phone +
                ", address=" + address;
    }

    @Override
    public boolean equals(Object obj) {
        Fan fan = (Fan)obj;
        if(fan!=null)
            return fan.getID().equals(this.getID());
        return false;
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public List<String> getFollowedPages(){
        List<String> pages = new LinkedList<>();
        for(PersonalPage p: followPages)
            pages.add(p.toString());
        return pages;
    }

    public List<String> getAllPages(){
        List<String> pages = new LinkedList<>();
        for(PersonalPage p: Database.getAllPages()){
         if(p.getUser().isActive())
             pages.add(p.toString());
        }
        return pages;
    }

    /*
    this function returns a list of all future games
     */
    public List<String> getAllFutureGames(){
        List<String> futureGames= new LinkedList<>();
        for(String game : Database.getAllFutureGames()){
            futureGames.add(checkFan(game));
        }
        return futureGames;
    }

    private String checkFan(String game) {
        String[] stringList = game.split(",");
        if(stringList.length>4) {
            for(int i=3; i<stringList.length; i++){
                if(stringList[i].equals(this.getID()))
                    return game+ "+";
            }
        }
        return game+"-";
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getComplaintsId() {
        return complaintsId;
    }

    public List<PersonalPage> getFollowPages() {
        return followPages;
    }
}
