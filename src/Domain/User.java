package Domain;

import Data.Database;
import Service.NotificationSystem;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends Guest {

    private String ID;
    private String firstName;
    private String lastName;
    private String mail;
    private boolean isActive;
    private List<Role> roles;
    private List<String> searchHistory;
    private static int counter =0;

    /**
     * constructor for user
     * @param firstName
     * @param lastName
     * @param ID - only first letter of user id... we add the number to it
     * @param mail
     */
    public User(String firstName, String lastName, String ID, String mail) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID +counter;
        counter++;
        this.ID = ID + IdGenerator.getNewId();
        if(!matcher.find())
            throw new RuntimeException("email address not valid");
        this.mail = mail;
        this.isActive = true;
        this.roles = new LinkedList<>();
        this.searchHistory = new LinkedList<>();
    }

    public User(String id, String firstName, String lastName, String mail, boolean isActive, List<Role> roles, List<String> searchHistory)
    {
        this.ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.isActive = isActive;
        this.roles = roles;
        this.searchHistory = searchHistory;
    }

    public User(String id, String firstName, String lastName, String mail, boolean isActive, List<String> searchHistory)
    {
        this.ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.isActive = isActive;
        this.roles = new LinkedList<>();
        this.searchHistory = searchHistory;
    }

    public Role checkUserRole (String userRole) {
        for(Role role : roles){
            if(role.myRole().contains(userRole))
                return role;
        }
        return null;
    }

    public boolean addToSearchHistory(String word){
        searchHistory.add(word);
        Database.updateObject(this);
        return true;
    }

    public List<String> getSearchHistory() {
        return searchHistory;
    }

    public User logout(){return null;}

    public void editPersonalInfo(String firstName, String lastName){
        setFirstName(firstName);
        setLastName(lastName);
        Database.updateObject(this);
    }

    public List<String> search(String wordToSearch)
    {
        addToSearchHistory(wordToSearch);
        return super.search(wordToSearch);
    }

    public boolean changePassword(String oldPassword, String newPassword){
        return Database.changePassword(this.mail, oldPassword, newPassword);
    }

    public void addMessage(String message){ /***/
        if(!(NotificationSystem.notifyUser(this, message))){
            if(message!=null)
                Database.addNotificationToUser(this.ID, message);
        }
    }

    @Override
    public String toString() {
        String userToString="User{" +
                ", ID=" + ID +
                ", first name=" + firstName +
                ", last name=" + lastName +
                ", isActive=" + isActive;

        for(Role role : roles){
            if(role instanceof Coach || role instanceof Player
               || role instanceof Fan){

                userToString = userToString + '\'' +role.toString();
            }
        }
        return userToString+"}";
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User)obj;
        if(user!=null)
            return user.getID().equals(this.getID());
        return false;
    }

    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++
    public String getName() {
        return firstName+" "+lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getID() {
        return ID;
    }

    public String getMail() {
        return mail;
    }

    public List<String> getMessageBox() {
        return Database.getAllNotifications(ID);
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<String> getStringRoles(){
        List<String> myRoles = new LinkedList<>();
        for (Role role : roles)
            myRoles.add(role.myRole());
        return myRoles;
    }

    public void deactivate() {
        isActive = false;
    }

    public void reactivate() {
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void addRoles(List<Role> listOfRoles) {
        roles.addAll(listOfRoles);
        for(Role role : roles)
            role.userId = this.ID;
    }
}
