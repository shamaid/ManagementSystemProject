package Domain;

import Data.Database;

import java.util.LinkedList;
import java.util.List;

public class Guest {


    public Guest() {

    }

    public List<String> search(String wordToSearch){
        List<String> searches = new LinkedList<>();
        for(Object o: Database.searchObject(wordToSearch) )
            searches.add(o.toString());
        return searches;
    }

    public User register(String mail, String password, String firstName, String lastName, String phone, String address)
    {
        return UserFactory.getNewFan(password, firstName, lastName, mail, phone, address);
    }
    public User login(String mail, String password)
    {
        return Database.getUserByMail(mail ,password );
    }

    public List<String> viewInfoAboutTeams() {
        List<Team> teams =  Database.getAllTeams();
        List<String> stringOfTeams = new LinkedList<>();
        for(Team t : teams)
            stringOfTeams.add(t.toString());
        return stringOfTeams;
    }

    public List<String> viewInfoAboutPlayers() {
        List<Player> players = Database.getAllPlayers();
        List<String> stringOfPlayers = new LinkedList<>();
        for(PartOfATeam p: players)
            stringOfPlayers.add(p.toString());
        return stringOfPlayers;
    }

    public List<String> viewInfoAboutCoaches() {
        List<Coach> coaches =  Database.getAllCoaches();
        List<String> stringOfCoaches = new LinkedList<>();
        for(PartOfATeam c : coaches)
            stringOfCoaches.add(c.toString());
        return stringOfCoaches;
    }

    public List<String> viewInfoAboutLeagues() {
        List<League> leagues = Database.getLeagues();
        List<String> stringOfLeagues = new LinkedList<>();
        for (League l : leagues)
            stringOfLeagues.add(l.toString());
        return stringOfLeagues;
    }
    public List<String> viewInfoAboutSeasons() {
        List<Season> seasons = Database.getSeasons();
        List <String> stringOfSeasons = new LinkedList<>();
        for (Season s: seasons)
            stringOfSeasons.add(s.toString());
        return stringOfSeasons;
    }
    public List<String> viewInfoAboutReferees(){
        List<Referee> referees = Database.getAllReferees();
        List <String> stringOfReferees = new LinkedList<>();
        for(Referee r : referees)
            stringOfReferees.add(r.toString());
        return stringOfReferees;
    }
}
