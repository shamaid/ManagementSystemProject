package Service;

import Domain.*;
import Logger.Logger;
import Presentation.Checker;

import java.util.List;

public class GuestSystem {

    Guest guest;

    public GuestSystem() {
        guest = new Guest();
    }

    /*
    Guest registration for the system
     */
    public String register(String mail, String password, String firstName, String lastName,
                                        String phone, String address){
        if(Checker.isValidEmailAddress(mail) && Checker.isValidPassword(password)) {
            User registeredUser = guest.register(mail, password, firstName, lastName, phone, address);
            if (registeredUser != null) {
                Logger.logEvent(registeredUser.getID() + " (Guest)", "Register Success");
                return registeredUser.getID();
            }
        }
        Logger.logError("Guest failed Registering");
        return null;
    }

    /*
     * User login to system
     * if exists return connected user
     * if the password is invalid or there is no such email in the system return null
     */
    public String logIn( String mail, String password){

        User loginUser = guest.login(mail, password);

        if (loginUser != null) {
            Logger.logEvent(loginUser.getID() + " (Guest)", "Login Success");
            return loginUser.getID();
        }
        else {
            Logger.logError("Guest failed Login");
            return null;
        }
    }

    /*
    Search results in a system
     */

    public List<String> search( String wordToSearch){
        Logger.logEvent("(Guest)","Searched " + wordToSearch);
        return guest.search(wordToSearch);
    }

    /*
    the guest chooses what to watch - teamsToManage, players, coaches, leagues and more
     */
    public List<String> viewInformationAboutTeams(){
        Logger.logEvent("(Guest)","Viewed Teams");
        return guest.viewInfoAboutTeams();
    }

    public List<String> viewInformationAboutPlayers(){
        Logger.logEvent("(Guest)","Viewed Players");
        return guest.viewInfoAboutPlayers();
    }

    public List<String> viewInformationAboutCoaches(){
        Logger.logEvent("(Guest)","Viewed Coaches");
        return guest.viewInfoAboutCoaches();
    }

    public List<String> viewInformationAboutLeagues(){
        Logger.logEvent("(Guest)","Viewed Leagues");
        return guest.viewInfoAboutLeagues();
    }

    public List<String> viewInformationAboutSeasons(){
        Logger.logEvent("(Guest)","Viewed Seasons");
        return guest.viewInfoAboutSeasons();
    }

    public List<String> viewInformationAboutReferees(){
        Logger.logEvent("(Guest)","Viewed Referees");
        return guest.viewInfoAboutReferees();
    }
}
