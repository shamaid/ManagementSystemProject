package Domain;

import Data.Database;

import java.util.List;
import java.util.LinkedList;
import java.util.Observable;

public class Team extends Observable {

    private String id;
    private String name;
    private int wins;
    private int losses;
    private int draws;
    private PersonalPage page;
    private List<User> teamOwners; // at least one
    private List<User> teamManagers;
    private List<User> players; // at least 11
    private List<User> coaches; // at least one
    private Budget budget;
    private List<String> gamesId;
    private List<Field> fields;
    private List<String> leaguesId;
    private boolean active;
    private boolean permanentlyClosed; //closed by admin and cannot open again

    public Team(String name, List<User> teamOwners, List<User> players, List<User> coaches, Field field) {
        this.id = "T"+IdGenerator.getNewId();
        this.name = name;
        if(teamOwners==null||teamOwners.size()<1)
           throw new RuntimeException("not enough TeamOwners");
        this.teamOwners = new LinkedList<>();
        linkTeamOwner(teamOwners);

        this.page = new PersonalPage("Team "+name+"'s page!", teamOwners.get(0));
        teamOwners.get(0).addRole(new HasPage(this.page));
        Database.addPersonalPage(page);

        if(players==null||players.size()<11)
            throw new RuntimeException("not enough Players");
        this.players = players;
        linkPlayers();
        if(coaches==null||coaches.size()<1)
            throw new RuntimeException("not enough Coaches");
        this.coaches = coaches;
        linkCoaches();
        this.budget = new Budget();
        this.budget.addIncome(1000000000);
        this.fields = new LinkedList<>();
        fields.add(field);
        linkField();
        this.wins=0;
        this.losses=0;
        this.draws=0;
        this.teamManagers=new LinkedList<>();
        this.gamesId = new LinkedList<>();
        this.active = true;
        this.permanentlyClosed = false;
        this.leaguesId = new LinkedList<>();
    }

    public Team(String id, String name, int wins, int losses, int draws, PersonalPage page, List<User> teamOwners, List<User> teamManagers, List<User> players, List<User> coaches, Budget budget, List<String> gamesId, List<Field> fields, List<String> lis, boolean isActive, boolean isPermanentlyClosed)
    {
        this.id = id;
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.page = page;
        this.teamOwners = teamOwners;
        this.teamManagers = teamManagers;
        this.players = players;
        this.coaches = coaches;
        this.budget = budget;
        this.gamesId = gamesId;
        this.fields = fields;
        this.leaguesId = lis;
        this.active = isActive;
        this.permanentlyClosed = isPermanentlyClosed;
    }

    public boolean addLeague(LeagueInSeason league) {
        if(!leaguesId.contains(league.getId())){
            this.leaguesId.add(league.getId());
            league.addATeam(this);
            return true;
        }
        return false;
    }

    private void linkField() {
        for(Field field :fields){
            field.addTeam(this);
        }
    }

    private void linkCoaches() {
        for(User user :coaches){
            //check if has coach add add team
            Coach coach =  (Coach) user.checkUserRole("Coach");
            if(coach==null)
                throw new RuntimeException("unauthorized coach");
            else
                coach.addTeam(this);
        }
    }

    private void linkPlayers() {
        for(User user :players){
            Player player = (Player) user.checkUserRole("Player");
            if(player==null)
                throw new RuntimeException("unauthorized player");
            else
                player.addTeam(this);
        }
    }

    private void linkTeamOwner(List<User> userList) {
        for(User user : userList){
            if(user!= null)
                addTeamOwner(user,false);
        }
    }

    @Override
    public String toString() {
        return "Team" +
                ", id=" + id +
                ": name=" + name;
    }

    public String AllDetailsAboutTeam(){
        return "Team Owners: " + printTeamOwners() +
                "Players: " + printPlayers() +
                "Coaches: " +printCoaches() +
                "Games: " +printGames();
    }

    @Override
    public boolean equals(Object obj) {
        Team team = (Team)obj;
        if(team!=null)
            return team.getID().equals(this.id);
        return false;
    }

    private String printGames() {
        String printNames = "";
        for(String gameId : gamesId)
            printNames= printNames + Database.getGame(gameId).getName() +",";
        return printNames;
    }

    private String printCoaches() {
        String printNames = "";
        for(User user : coaches)
            printNames= printNames + user.getName() +",";
        return printNames;
    }

    private String printPlayers() {
        String printNames = "";
        for(User user : players)
            printNames= printNames + user.getName() +",";
        return printNames;
    }

    private String printTeamOwners() {
        String printNames = "";
        for(User user : teamOwners)
            printNames= printNames + user.getName() +",";
        return printNames;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(active){
            setChanged();
            notifyObservers(Database.getCurrentDate() + ": " + this.name + " is open");
            updateAllSystemAdmins(Database.getCurrentDate() + ": " + "team " + this.name + " open");
        }
        else {
            setChanged();
            notifyObservers(Database.getCurrentDate() + ": " + this.name + " is closed");
            updateAllSystemAdmins(Database.getCurrentDate() + ": " + "team " + this.name + " closed");
        }
    }

    private void updateAllSystemAdmins(String news) {
        for(Admin admin : Database.getAllAdmins()){
            Admin adminRole = (Admin)Database.getUser(admin.getID()).checkUserRole("Admin");
            if(adminRole instanceof Admin){
                adminRole.update(this, news);
            }
        }
    }

    public void addAWin() {
        this.wins++;
    }

    public void addALoss() {
        this.losses++;
    }

    public void addADraw() {
        this.draws++;
    }

    public boolean addTeamOwner(User user,boolean notFirstTime) {
        if(!teamOwners.contains(user)) {
            this.teamOwners.add(user);
            TeamOwner teamOwner = (TeamOwner) user.checkUserRole("TeamOwner");
            if(teamOwner!=null){
                teamOwner.addTeam(this);
            }
            else{
                teamOwner = new TeamOwner(user.getID());
                teamOwner.addTeam(this);
                user.addRole(teamOwner);
            }
            this.addObserver(teamOwner);
            teamOwner.update(this, Database.getCurrentDate() + ": " + "You've added a team owner appointment to team " +this.getName());
            if(notFirstTime) {
                Database.updateObject(this);
            }
            Database.updateObject(teamOwner);
            return true;
        }
        return false;
    }

    public boolean addTeamManager(User user, double price,boolean manageAssets , boolean finance) {
        if(!teamManagers.contains(user)) {
            this.teamManagers.add(user);
            TeamManager teamManager = (TeamManager)user.checkUserRole("TeamManager");
            if(teamManager!=null){
                teamManager.addTeam(this);
            }
            else{
                teamManager = new TeamManager(user.getID(), price, manageAssets, finance);
                teamManager.addTeam(this);
                user.addRole(teamManager);
            }
            this.addObserver(teamManager);
            teamManager.update(this, Database.getCurrentDate() + ": " + "You've added a team manager appointment to team " +this.getName());
            Database.updateObject(this);
            Database.updateObject(teamManager);
            return true;
        }
        return false;
    }

    public boolean addPlayer(User user) {
        if(!players.contains(user)) {
            Player player = (Player)user.checkUserRole("Player");
            if(player!=null){
                for(Team playersTeams: player.getTeams()){
                    if(doListsHaveLeaguesInCommon(this.leaguesId,playersTeams.leaguesId))
                        return false;
                }
                this.players.add(user);
                player.addTeam(this);
                Database.updateObject(this);
                Database.updateObject(player);
                return true;
            }
        }
        return false;
    }

    private boolean doListsHaveLeaguesInCommon(List<String> list1, List<String> list2){
        for(String league1:list1){
            for(String league2: list2){
                if(league1.equals(league2))
                    return true;
            }
        }
        return false;
    }

    public boolean addCoach(User user) {
        if(!coaches.contains(user)) {
            Coach coach = (Coach) user.checkUserRole("Coach");
            if(coach!=null){
                this.coaches.add(user);
                coach.addTeam(this);
                Database.updateObject(this);
                Database.updateObject(coach);
                return true;
            }
        }
        return false;
    }

    public boolean addGame(Game game) {
        if(!gamesId.contains(game.getId())){
            this.gamesId.add(game.getId());
            return true;
        }
        return false;
    }

    /**remove**/
    public boolean removeTeamOwner(User teamOwner) {
        if(teamOwners.contains(teamOwner) && teamOwners.size()>1) {
            teamOwners.remove(teamOwner);
            TeamOwner teamOwnerRole = (TeamOwner) teamOwner.checkUserRole("TeamOwner");
            teamOwnerRole.removeTeam(this);
            teamOwnerRole.update(this, Database.getCurrentDate() + ": " + "Your subscription has been removed from the team "+this.name);
            this.deleteObserver(teamOwnerRole);
            Database.updateObject(this);
            Database.updateObject(teamOwnerRole);
            return true;
        }
        return false;
    }

    public boolean removeTeamManager(User teamManager) {
        if(teamManagers.contains(teamManager)) {
            teamManagers.remove(teamManager);
            TeamManager teamManagerRole = (TeamManager) teamManager.checkUserRole("TeamManager");
            teamManagerRole.removeTeam(this);
            teamManagerRole.update(this, Database.getCurrentDate() + ": " + "Your subscription has been removed from the team "+this.name);
            this.deleteObserver(teamManagerRole);
            Database.updateObject(this);
            Database.updateObject(teamManagerRole);
            return true;
        }
        return false;
    }

    public boolean removePlayer(User player) {
        if(players.contains(player) && players.size()>11) {
            Role role = player.checkUserRole("Player");
            ((Player)role).removeTeam(this);
            this.players.remove(player);
            Database.updateObject(this);
            return true;
        }
        return false;
    }

    public boolean removeCoach(User coach) {
        if(coaches.contains(coach) && coaches.size()>1) {
            Role role = coach.checkUserRole("Coach");
            ((Coach)role).removeTeam(this);
            this.coaches.remove(coach);
            Database.updateObject(this);
            return true;
        }
        return false;
    }

    public boolean addField(Field field) {
        if(!fields.contains(field)) {
            fields.add(field);
            Database.updateObject(this);
            return true;
        }
        return false;
    }

    public boolean removeField(Field field) {
        if(fields.size()>1) {
            fields.remove(field);
            Database.updateObject(this);
            return true;
        }
        return false;
    }
    // ++++++++++++++++++++++++++++ getter&setter ++++++++++++++++++++++++++++
    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public PersonalPage getPage() {
        return page;
    }

    public List<User> getTeamOwners() {
        return teamOwners;
    }

    public List<User> getTeamManagers() {
        return teamManagers;
    }

    public List<User> getPlayers() {
        return players;
    }

    public List<User> getCoaches() {
        return coaches;
    }

    public Budget getBudget() {
        return budget;
    }

    public boolean addIncome(double income) {
        if(budget.addIncome(income)) {
            Database.updateObject(this);
            return true;
        }
        return false;

    }
    public boolean addExpanse(double expanse) {
        if(budget.addExpanse(expanse)) {
            Database.updateObject(this);
            return true;
        }
        updateAllUnionRep(Database.getCurrentDate() + "The team: "+name+" has exceeded its budget");

        return false;

    }

    private void updateAllUnionRep(String news) {
        for (Role union : Database.getAllUnions()){
            ((UnionRepresentative)union).update(this, news);
        }
    }


    public List<String> getGamesId() {
        return gamesId;
    }

    public List<Field> getFields() {
        return fields;
    }

    /**GET ONE FIELD*/
    public Field getField() {
        return fields.get(0);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setPermanentlyClosed(boolean permanentlyClosed) {
        this.permanentlyClosed = permanentlyClosed;
        if(permanentlyClosed) {
            setChanged();
            notifyObservers(Database.getCurrentDate() + ": " + this.name + " is permanently closed");
            updateAllSystemAdmins(Database.getCurrentDate() + ": " + this.name + " is permanently closed");
        }

    }

    public String getID() {
        return id;
    }

    public double getPrice() {
        return 1000000000;
    }

    public List<String> getLeaguesInSeason() {
        return this.leaguesId;
    }
}
