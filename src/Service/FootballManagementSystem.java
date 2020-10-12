package Service;

import Data.Database;
import Domain.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FootballManagementSystem {
    private static Database database;
    //***service***//
    private static NotificationSystem notificationSystem;
    private static FinanceTransactionsSystem financeTransactionsSystem;
    private static GuestSystem guestSystem;
    private static PersonalPageSystem personalPageSystem ;
    private static RefereeSystem refereeSystem;
    public static UnionRepresentativeSystem unionRepresentativeSystem;
    private static UserSystem userSystem;
    private static AdminSystem adminSystem;
    private static TeamManagementSystem teamManagementSystem;
    //***presentation***//
    private static List<Admin> systemAdmins;
    //***External systems***//
    private static ProxyAccountingSystem accountingSystem;
    private static ProxyIsraeliTaxLawsSystem taxLawsSystem;
    public Database getDatabase()
    {
        return database;
    }

    public TeamManagementSystem getTeamManagementSystem() {
        return teamManagementSystem;
    }

    public AdminSystem getAdminSystem() {
        return adminSystem;
    }

    public UserSystem getUserSystem() {
        return userSystem;
    }

    public UnionRepresentativeSystem getUnionRepresentativeSystem() {
        return unionRepresentativeSystem;
    }

    public GuestSystem getGuestSystem() {
        return guestSystem;
    }

    public PersonalPageSystem getPersonalPageSystem() {
        return personalPageSystem;
    }

    public RefereeSystem getRefereeSystem() {
        return refereeSystem;
    }

    public NotificationSystem getNotificationSystem() {
        return notificationSystem;
    }

    public FinanceTransactionsSystem getFinanceTransactionsSystem() {
        return financeTransactionsSystem;
    }

    public static List<Admin> getSystemAdmins() {
        return systemAdmins;
    }

    public static ProxyAccountingSystem getAccountingSystem() {
        return accountingSystem;
    }

    public static ProxyIsraeliTaxLawsSystem getTaxLawsSystem() {
        return taxLawsSystem;
    }

    public static boolean systemInit(boolean firsTime){
        //***data***//
        database = new Database();
        if(!firsTime)
            //database.loadDatabaseFromDisk("");
        //***service***//
        notificationSystem = new NotificationSystem();
        adminSystem = new AdminSystem();
        financeTransactionsSystem = new FinanceTransactionsSystem();
        guestSystem = new GuestSystem();
        personalPageSystem = new PersonalPageSystem();
        refereeSystem = new RefereeSystem();
        unionRepresentativeSystem = new UnionRepresentativeSystem();
        userSystem = new UserSystem();
        teamManagementSystem = new TeamManagementSystem();
        //***presentation***//
        systemAdmins = new LinkedList<>();
        if(firsTime){
            User admin = UserFactory.getNewAdmin("Aa1234","adminush","first","example@gmail.com");
            Admin systemAdmin = (Admin) admin.checkUserRole("Admin");
            systemAdmins.add(systemAdmin);

        }
        else{
            systemAdmins.addAll(database.getAllAdmins());
        }


        accountingSystem = new ProxyAccountingSystem();
        taxLawsSystem = new ProxyIsraeliTaxLawsSystem();

        return true;
    }

    public Admin getAdmin(){
        return Database.getAllAdmins().get(0);
    }

    public boolean connectToOuterSystems()
    {

        boolean con1 = accountingSystem.connect();
        boolean con2 = taxLawsSystem.connect();

        if (con1 && con2)
            return true;

        return false;

    }

    public static String dataReboot(){
        MailSender.setReallySend(false);
        accountingSystem.connect();
        User unionRep = UserFactory.getNewUnionRepresentative("", "","mail@mail.com");
        String seasonId = unionRepresentativeSystem.configureNewSeason(unionRep.getID(),2021, Database.getDate(2020, 5, 1));
        String leagueId = unionRepresentativeSystem.configureNewLeague(unionRep.getID(),"Haal", "level3");
        String leagueInSeasonId = unionRepresentativeSystem.configureLeagueInSeason(unionRep.getID(),leagueId, seasonId, "PlayTwiceWithEachTeamPolicy", "StandardScorePolicy", 300);
        unionRepresentativeSystem.addFieldToSystem(unionRep.getID(),"jerusalem","Teddy" ,550, 150000);
        Field field = Database.getAllActiveFields().get(0);
        Team team;
        for (int i = 0; i < 14; i++) {
            List<String> players = createPlayers();
            List<String> coaches = createCoaches();
            List<User> owners = new LinkedList<>();
            String ownerId = adminSystem.addNewTeamOwner(systemAdmins.get(0).getID(),"Team","Owner","to"+i+"@gmail.com" );
            User owner = UserFactory.getUser(ownerId);
            if(owner!=null){
                owners.add(owner);
                TeamOwner teamOwner = (TeamOwner)owner.checkUserRole("TeamOwner");
                teamOwner.createTeam(owner,"team"+i, players, coaches, field.getID());
                team = teamOwner.getTeamsToManage().get(0);
                unionRepresentativeSystem.addTeamToLeague(unionRep.getID(),leagueInSeasonId, team.getID(), accountingSystem);
            }

        }
        for (int i = 0; i <10 ; i++) {
            String refId = mainReferee(unionRep);
            unionRepresentativeSystem.assignRefToLeague(unionRep.getID(),leagueInSeasonId, refId);
        }
        unionRepresentativeSystem.assignGames(unionRep.getID(), leagueInSeasonId);
        return leagueInSeasonId;
    }


    public static String mainReferee(User unionRep) {
        return unionRepresentativeSystem.appointReferee(unionRep.getID(),"referee", "",+IdGenerator.getNewId()+"@gmail.com", "referees");
    }
    public static List<String> createCoaches() {
        String coachId = adminSystem.addNewCoach(systemAdmins.get(0).getID(),"coach1", "coach",+IdGenerator.getNewId()+"@gmail.com", "UEFA_A", "main", 1500);
        List<String> coaches = new LinkedList<>();
        coaches.add(coachId);
        return coaches;
    }
    public static List<String> createPlayers() {
        List<String> players = new LinkedList<>();
        for (int i = 0; i <12 ; i++) {
            String playerId = adminSystem.addNewPlayer(systemAdmins.get(0).getID(), "player"+i, "...", "mail"+IdGenerator.getNewId()+"@gmail.com", Database.getDate(1995, 10, 5), "attackingPlayer", 3500);
            if(playerId!=null){
                players.add(playerId);
            }
        }
        return players;
    }
    private static void printList(List<Game> allGames) {
        for(Game game: allGames){
            System.out.println(game.toString());
        }
    }

    public static List<Date> getDates() {
        LinkedList<Date> dates = new LinkedList<>();
        for (int i = 1; i < 30; i++) {

            dates.add(Database.getDate(2020, 5, i, 20, 0));
        }
        for (int i = 1; i < 31; i++) {
            dates.add(Database.getDate(2020, 6, i, 20, 0));
        }
        for (int i = 1; i <31 ; i++) {
            dates.add(Database.getDate(2020, 7, i, 20, 0));
        }
        for (int i = 1; i <30 ; i++) {
            dates.add(Database.getDate(2020, 8, i, 20, 0));
        }
        for (int i = 1; i <31 ; i++) {
            dates.add(Database.getDate(2020, 9, i, 20, 0));
        }
        for (int i = 1; i <30 ; i++) {
            dates.add(Database.getDate(2020, 10, i, 20, 0));
        }
        for (int i = 1; i <31 ; i++) {
            dates.add(Database.getDate(2020, 11, i, 20, 0));
        }
        return dates;
    }

}

