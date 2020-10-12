package Domain;

import Data.Database;
import java.util.List;
import java.util.LinkedList;

public class PersonalPage {

    private String id;
    private String data;
    private User user;
    private List<Fan> followers;

    public PersonalPage(String data, User user) {
        this.id = "PP"+IdGenerator.getNewId();
        this.data = data;
        this.user = user;
        followers = new LinkedList<>();
    }

    public PersonalPage(String id, User owner, String data, List<Fan> followers)
    {
        this.id = id;
        this.user = owner;
        this.data = data;
        this.followers = followers;
    }

    @Override
    public String toString() {
        return  id +","+user.getName()+","+data;
    }

    @Override
    public boolean equals(Object obj) {
        PersonalPage personalPage = (PersonalPage)obj;
        if(personalPage!=null)
            return personalPage.getId().equals(this.id);
        return false;
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public User getUser() {
        return user;
    }

    public void addAFollower(Fan follower) {
        if(!followers.contains(follower)){
            followers.add(follower);
            Database.updateObject(this);
            follower.addPageToFollow(this.getId());
        }
    }

    public List<Fan> getFollowers(){
        return followers;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addData(String data) {
        this.data +=" "+ data;
    }

}
