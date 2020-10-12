package Service;

import Data.DataAccess;
import Data.Database;
import Domain.IdGenerator;
import Logger.Logger;
import Presentation.Checker;
import Presentation.Client;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private UserSystem userSystem;
    private GuestSystem guestSystem;
    private AdminSystem adminSystem;
    private TeamManagementSystem teamSystem;
    private NotificationSystem notificationSystem;
    private PersonalPageSystem personalPageSystem;
    private FinanceTransactionsSystem financeTransactionsSystem;
    private UnionRepresentativeSystem unionRepresentativeSystem;
    private ProxyAccountingSystem proxyAccountingSystem;
    private RefereeSystem refereeSystem;

    private static HashMap<String, Socket> loggedUsers = new HashMap<>();
    private static HashMap<String, Socket> loggedUsersNotifications = new HashMap<>();
    private final Object loggedUsersMutex = new Object();
    private final Object ioMutex = new Object();


    private static int nextID;

    public static void updateID(int nextId)
    {
        nextID = nextId;
        updateFileID();
    }

    public static int getLastID()
    {
        List<String> lines = new LinkedList<>();
        String configFilePath = "./config";

        try {
            lines = Files.readAllLines(new File(configFilePath).toPath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String lastIDString = lines.get(1);
        lastIDString = lastIDString.substring(lastIDString.indexOf(":") + 1);

        nextID = Integer.parseInt(lastIDString);
        return nextID;
    }

    public static void updateFileID()
    {
        List<String> lines = new LinkedList<>();
        String configFilePath = "./config";

        try {
            lines = Files.readAllLines(new File(configFilePath).toPath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String lastIDSt = "LastID:" + nextID;
        lines.set(1, lastIDSt);

        writeLinesToFile(configFilePath, lines);

    }

    private boolean isFirstInit(List<String> configFileLines)
    {

        String init = configFileLines.get(0);
        init = init.substring(init.indexOf(":") + 1);
        return init.toLowerCase().equals("false");

    }

    public void firstInit()
    {
        List<String> lines = new LinkedList<>();
        String configFilePath = "./config";

        try {
            lines = Files.readAllLines(new File(configFilePath).toPath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        IdGenerator.setNextId(getLastID());

        if (isFirstInit(lines))
        {
            String mail;
            String password;
            String firstName;
            String lastName;
            Scanner in = new Scanner(System.in);

            System.out.println("Please enter Mail Address, press Enter to continue: ");
            mail = in.nextLine();
            boolean isValidMail = Checker.isValid(mail);

            while (!isValidMail)
            {
                System.out.println("Invalid Mail Address was entered, please try again: ");
                mail = in.nextLine();
                isValidMail = Checker.isValid(mail);
            }

            System.out.println("Please enter Password, press Enter to continue: ");
            password = in.nextLine();
            boolean isValidPassword = Checker.isValidPassword(password);

            while (!isValidPassword)
            {
                System.out.println("Invalid Password was entered, please try again: ");
                password = in.nextLine();
                isValidPassword = Checker.isValidPassword(password);
            }

            System.out.println("Please enter First Name, press Enter to continue: ");
            firstName = in.nextLine();
            boolean isValidFirstName = Checker.isValid(firstName);

            while (!isValidFirstName)
            {
                System.out.println("Invalid First Name was entered, please try again: ");
                firstName = in.nextLine();
                isValidFirstName = Checker.isValid(firstName);
            }

            System.out.println("Please enter Last Name, press Enter to continue: ");
            lastName = in.nextLine();
            boolean isValidLastName = Checker.isValid(lastName);

            while (!isValidLastName)
            {
                System.out.println("Invalid Last Name was entered, please try again: ");
                lastName = in.nextLine();
                isValidLastName = Checker.isValid(lastName);
            }


            boolean firstAdminAdded = adminSystem.addFirstAdmin(password, firstName, lastName, mail);

            if (firstAdminAdded)
            {
                lines.set(0, "isInitiated:True");
                writeLinesToFile(configFilePath, lines);
                System.out.println("System Initiated successfully, press Enter to continue..");
            }
            else
            {
                System.out.println("System Initiation failed, press Enter to continue..");
            }

            try
            {
                System.in.read();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            }

    }

    private static void writeLinesToFile(String filePath, List<String> lines)
    {
        try
        {

            FileWriter writer = new FileWriter(filePath);

            for(String str: lines) {

                if (lines.size() == 1)
                    writer.write(str);
                else
                    writer.write(str + System.lineSeparator());

            }

            writer.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private volatile boolean stop;
    private ServerSocket welcomeSocket = null;
    private ServerSocket notSocket = null;
    private int maxUsers;

    public Server(int port, int maxUsers) {

        userSystem = new UserSystem();
        guestSystem = new GuestSystem();
        notificationSystem = new NotificationSystem();
        adminSystem = new AdminSystem();
        teamSystem = new TeamManagementSystem();
        personalPageSystem = new PersonalPageSystem();
        financeTransactionsSystem = new FinanceTransactionsSystem();
        unionRepresentativeSystem = new UnionRepresentativeSystem();
        refereeSystem = new RefereeSystem();

        proxyAccountingSystem = new ProxyAccountingSystem();

        Database db = new Database();

        this.maxUsers = maxUsers;

        try {

            String server_IP = InetAddress.getLocalHost().getHostAddress();

            System.out.println("Server is Up!");
            System.out.println("IP Address: " + server_IP);
            System.out.println("Ports: " + port + "," + (port+1));
            System.out.println("Max Users: " + maxUsers);

            Logger.logServer("Server is Up!");
            Logger.logServer("IP Address: " + server_IP);
            Logger.logServer("Ports: " + port + "," + (port+1));
            Logger.logServer("Max Users: " + maxUsers);

            welcomeSocket = new ServerSocket(port);
            notSocket = new ServerSocket(port + 1);

        } catch (Exception e) {
            Logger.logError("Server Fail: can't create server");
            e.printStackTrace();
        }
        this.stop = false;
    }

    public void start() {

        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                runNotificationsServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void runServer() {

        ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        exec.setCorePoolSize(maxUsers);
        exec.setMaximumPoolSize(maxUsers);


        while (!stop) {
            try {
                Socket clientSocket = welcomeSocket.accept();

                String ip = clientSocket.getInetAddress().toString();
                Logger.logServer("Client  " + ip + "  connected");

                exec.execute(() -> {
                    handle_Client(clientSocket);

                });
/*
                exec.execute(() -> {

                    while (true)
                    {
                        for (String st : loggedUsersNotifications.keySet())
                            sendNotification(st, "Test");

                        try
                        {
                            Thread.sleep(1000);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                });
*/
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        exec.shutdown();

        try {
            exec.awaitTermination(12, TimeUnit.HOURS);
        } catch (Exception e) {
            Logger.logError("Server timeout");
            e.printStackTrace();
        }

    }

    public void runNotificationsServer() {

        ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        exec.setCorePoolSize(maxUsers);
        exec.setMaximumPoolSize(maxUsers);


        while (!stop) {
            try {
                Socket clientNotSocket = notSocket.accept();

                exec.execute(() -> {
                    handle_notifications(clientNotSocket);

                });
            }

            catch (SocketException se)
                {
                    Logger.logEvent("Guest", "Terminated Program");
                }

            catch (Exception e) {
                e.printStackTrace();
            }


        }

        exec.shutdown();

        try {
            exec.awaitTermination(12, TimeUnit.HOURS);
        } catch (Exception e) {
            Logger.logError("Server timeout");
            e.printStackTrace();
        }

    }

    private void handle_notifications(Socket clientNotSocket) {

        try {
            DataInputStream stream = new DataInputStream(clientNotSocket.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
            String lineReceived = rd.readLine();

            synchronized (loggedUsersMutex)
            {
                loggedUsersNotifications.put(lineReceived.replace("\n", ""), clientNotSocket);

            }

        }

        catch (SocketException se)
        {

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handle_Client(Socket clientSocket) {

        while(clientSocket.isConnected()) {
            try {
                DataInputStream stream = new DataInputStream(clientSocket.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
                String lineReceived = rd.readLine();

                System.out.println("Server Received Packet: " + lineReceived);

                String[] splitLine = lineReceived.replace("\n", "").split("\\|");
                String operation = splitLine[0];


                switch (operation) {

                    // ------------------- GUEST -------------------
                    case "logIn": // Done
                        handle_Login(splitLine, clientSocket);

                        break;

                    case "guestSearch": // Done
                        handle_guestSearch(splitLine, clientSocket);

                        break;

                    case "register": // Done
                        handle_Register(splitLine, clientSocket);

                        break;


                    case "viewInformationAboutTeams": // Done
                        handle_viewInformationAboutTeams(clientSocket);
                        break;

                    case "viewInformationAboutPlayers": // Done
                        handle_viewInformationAboutPlayers(clientSocket);
                        break;

                    case "viewInformationAboutCoaches": // Done
                        handle_viewInformationAboutCoaches(clientSocket);
                        break;

                    case "viewInformationAboutLeagues": // Done
                        handle_viewInformationAboutLeagues(clientSocket);
                        break;

                    case "viewInformationAboutSeasons": // Done
                        handle_viewInformationAboutSeasons(clientSocket);
                        break;

                    case "viewInformationAboutReferees": // Done
                        handle_viewInformationAboutReferees(clientSocket);
                        break;

                    // ------------------- USER -------------------

                    case "logOut": // Done
                        handle_Logout(splitLine, clientSocket);
                        break;

                    case "viewSearchHistory": // Done
                        handle_viewSearchHistory(splitLine, clientSocket);
                        break;

                    case "addComplaint": // Done
                        handle_addComplaint(splitLine, clientSocket);
                        break;

                    case "getFanPages": // Done
                        handle_getFanPages(splitLine, clientSocket);
                        break;

                    case "viewPersonalDetails": // Done
                        handle_viewPersonalDetails(splitLine, clientSocket);
                        break;

                    case "editPersonalInfo": // Done
                        handle_editPersonalInfo(splitLine, clientSocket);
                        break;

                    case "editFanPersonalDetails": // Done
                        handle_editFanPersonalDetails(splitLine, clientSocket);
                        break;

                    case "registrationToFollowUp": // Done
                        handle_registrationToFollowUp(splitLine, clientSocket);
                        break;

                    case "getAllPages": // Done
                        handle_getAllPages(splitLine, clientSocket);
                        break;

                    case "getAllFutureGames": // Done
                        handle_getAllFutureGames(splitLine, clientSocket);
                        break;

                    case "followGames": // Done
                        handle_registrationForGamesAlerts(splitLine, clientSocket);
                        break;

                    case "updateTrainingForCoach": // Done
                        handle_updateTrainingForCoach(splitLine, clientSocket);
                        break;

                    case "updateTrainingForReferee": // Done
                        handle_updateTrainingForReferee(splitLine, clientSocket);
                        break;

                    case "updateRoleForPlayer": // Done
                        handle_updateRoleForPlayer(splitLine, clientSocket);
                        break;

                    case "updateRoleForCoach": // Done
                        handle_updateRoleForCoach(splitLine, clientSocket);
                        break;

                    case "getRoleForPlayer": // Done
                        handle_getRoleForPlayer(splitLine, clientSocket);
                        break;

                    case "getRoleForCoach": // Done
                        handle_getRoleForCoach(splitLine, clientSocket);
                        break;

                    case "userSearch": // Done
                        handle_userSearch(splitLine, clientSocket);
                        break;

                    case "getUserRoles": // Done
                        handle_getUserRoles(splitLine, clientSocket);
                        break;

                    case "getUserInfo":
                        handle_getUserInfo(splitLine, clientSocket);
                        break;


                    // ------------------- ADMIN -------------------

                    case "removeUser": // Done
                        handle_removeUser(splitLine, clientSocket);
                        break;

                    case "addNewPlayer": // Done
                        handle_addNewPlayer(splitLine, clientSocket);

                        break;

                    case "addNewCoach": // Done
                        handle_addNewCoach(splitLine, clientSocket);
                        break;

                    case "addNewTeamOwner": // Done
                        handle_addNewTeamOwner(splitLine, clientSocket);

                        break;

                    case "addNewTeamManager": // Done
                        handle_addNewTeamManager(splitLine, clientSocket);

                        break;

                    case "addNewUnionRepresentative": // Done
                        handle_addNewUnionRepresentative(splitLine, clientSocket);

                        break;

                    case "addNewAdmin": // Done
                        handle_addNewAdmin(splitLine, clientSocket);

                        break;

                    case "permanentlyCloseTeam": // Done
                        handle_permanentlyCloseTeam(splitLine, clientSocket);

                        break;

                    case "responseToComplaint": // Done
                        handle_responseToComplaint(splitLine, clientSocket);

                        break;

                    case "viewLog": // Done
                        handle_viewLog(splitLine, clientSocket);

                        break;

                    case "trainModel": // Done
                        handle_trainModel(splitLine, clientSocket);
                        break;

                    case "getAllDetailsAboutOpenTeams_Admin": // Done
                        handle_getAllDetailsAboutOpenTeams_Admin(splitLine, clientSocket);
                        break;

                    case "getAllOpenTeams_Admin": // Done
                        handle_getAllOpenTeams_Admin(splitLine, clientSocket);
                        break;

                    case "getAllDetailsAboutCloseTeams": // Done
                        handle_getAllDetailsAboutCloseTeams(splitLine, clientSocket);
                        break;

                    case "getAllCloseTeams": // Done
                        handle_getAllCloseTeams(splitLine, clientSocket);
                        break;

                    case "getAllUsers_Admin": // Done
                        handle_getAllUsers_Admin(splitLine, clientSocket);
                        break;

                    case "getAllActiveComplaints": // Done
                        handle_getAllActiveComplaints(splitLine, clientSocket);
                        break;


                    // ------------------- REFEREE SYSTEM -------------------

                    case "addEventToGame":
                        handle_addEventToGame(splitLine, clientSocket);
                        break;

                    case "setScoreInGame":
                        handle_setScoreInGame(splitLine, clientSocket);
                        break;

                    case "getGameReport":
                        handle_getGameReport(splitLine, clientSocket);
                        break;

                    case "changeEvent":
                        handle_changeEvent(splitLine, clientSocket);
                        break;

                    case "getAllPastGames_R":
                        handle_getAllPastGames_R(splitLine, clientSocket);
                        break;

                    case "getOccurringGame":
                        handle_getAllOccurringGame(splitLine, clientSocket);
                        break;
                    case "getAllTeamAssets_R":
                        handle_getAllTeamAssets_R(splitLine, clientSocket);
                        break;
                    // ------------------- TEAM MANAGEMENT -------------------


                    case "addAssetPlayer": // Done
                        handle_addAssetPlayer(splitLine, clientSocket);
                        break;

                    case "addAssetCoach": // Done
                        handle_addAssetCoach(splitLine, clientSocket);
                        break;

                    case "addField": // Done
                        handle_addField(splitLine, clientSocket);
                        break;

                    case "removeField": // Done
                        handle_removeField(splitLine, clientSocket);
                        break;

                    case "removeAssetPlayer": // Done
                        handle_removeAssetPlayer(splitLine, clientSocket);
                        break;

                    case "removeAssetCoach": // Done
                        handle_removeAssetCoach(splitLine, clientSocket);
                        break;

                    case "updateAsset": // Done
                        handle_updateAsset(splitLine, clientSocket);
                        break;

                    case "createTeam": // Done
                        handle_createTeam(splitLine, clientSocket);
                        break;

                    case "appointmentTeamOwner": // Done
                        handle_appointmentTeamOwner(splitLine, clientSocket);
                        break;

                    case "appointmentTeamManager": // Done
                        handle_appointmentTeamManager(splitLine, clientSocket);
                        break;

                    case "removeAppointmentTeamOwner": // Done
                        handle_removeAppointmentTeamOwner(splitLine, clientSocket);
                        break;

                    case "removeAppointmentTeamManager": // Done
                        handle_removeAppointmentTeamManager(splitLine, clientSocket);
                        break;

                    case "closeTeam": // Done
                        handle_closeTeam(splitLine, clientSocket);
                        break;

                    case "reOpeningTeam": // Done
                        handle_reOpeningTeam(splitLine, clientSocket);
                        break;

                    case "getTeams": // Done
                        handle_getTeams(splitLine, clientSocket);
                        break;

                    case "getAllUsers_Team": // Done
                        handle_getAllUsers_Team(splitLine, clientSocket);
                        break;

                    case "getAllPlayers":
                        handle_getAllPlayers(splitLine, clientSocket);
                        break;

                    case "getAllCoaches":
                        handle_getAllCoaches(splitLine, clientSocket);
                        break;

                    case "getAllFields":
                        handle_getAllFields(splitLine, clientSocket);
                        break;

                    case "getAllTeamAssets_Team":
                        handle_getAllTeamAssets(splitLine, clientSocket);
                        break;

                    case "getAllClosedTeam":
                        handle_getAllClosedTeam(splitLine, clientSocket);
                        break;


                    // ------------------- Personal Page -------------------

                    case "uploadToPage": // Done
                        handle_uploadToPage(splitLine, clientSocket);
                        break;

                    case "viewPage":
                        handle_viewPage(splitLine, clientSocket);
                        break;

                    // ------------------- Finance Transactions -------------------

                    case "reportIncome": // Done
                        handle_reportNewIncome(splitLine, clientSocket);
                        break;

                    case "reportExpanse": // Done
                        handle_reportNewExpanse(splitLine, clientSocket);
                        break;

                    case "getBalance": // Done
                        handle_getBalance(splitLine, clientSocket);

                        break;

                    // ------------------- Union Representative System -------------------


                    case "configureNewLeague": // Done
                        handle_configureNewLeague(splitLine, clientSocket);
                        break;

                    case "configureNewSeason": // Done
                        handle_configureNewSeason(splitLine, clientSocket);
                        break;

                    case "configureLeagueInSeason": // Done
                        handle_configureLeagueInSeason(splitLine, clientSocket);
                        break;

                    case "appointReferee": // Done
                        handle_appointReferee(splitLine, clientSocket);
                        break;

                    case "addRefereeToLeague": // Done
                        handle_assignRefToLeague(splitLine, clientSocket);
                        break;

                    case "changeScorePolicy": // Done
                        handle_changeScorePolicy(splitLine, clientSocket);
                        break;

                    case "changeAssignmentPolicy": // Done
                        handle_changeAssignmentPolicy(splitLine, clientSocket);
                        break;

                    case "assignGames": // Done
                        handle_assignGames(splitLine, clientSocket);
                        break;

                    case "changeGameDate": // Done
                        handle_changeGameDate(splitLine, clientSocket);
                        break;

                    case "changeGameLocation": // Done
                        handle_changeGameLocation(splitLine, clientSocket);
                        break;

                    case "addTeamToLeague": // Done
                        handle_addTeamToLeague(splitLine, clientSocket);
                        break;

                    case "calculateLeagueScore":
                        handle_calculateLeagueScore(splitLine, clientSocket);
                        break;

                    case "calculateGameScore":
                        handle_calculateGameScore(splitLine, clientSocket);
                        break;

                    case "changeRegistrationFee":
                        handle_changeRegistrationFee(splitLine, clientSocket);
                        break;

                    case "getRegistrationFee": // Done
                        handle_getRegistrationFee(splitLine, clientSocket);
                        break;

                    case "addTUTUPayment":
                        handle_addTUTUPayment(splitLine, clientSocket);
                        break;

                    case "addPaymentsFromTheTUTU":
                        handle_addPaymentsFromTheTUTU(splitLine, clientSocket);
                        break;

                    case "addFieldToSystem":
                        handle_addFieldToSystem(splitLine, clientSocket);
                        break;

                    case "allLeaguesInSeasons":
                        handle_allLeaguesInSeasons(splitLine, clientSocket);
                        break;

                    case "getAllLeagues":
                        handle_getAllLeagues(splitLine, clientSocket);
                        break;

                    case "getAllSeasons":
                        handle_getAllSeasons(splitLine, clientSocket);
                        break;

                    case "getAllScorePolicies":
                        handle_getAllScorePolicies(splitLine, clientSocket);
                        break;
                    case "getAllAssignmentsPolicies":
                        handle_getAllAssignmentsPolicies(splitLine, clientSocket);
                        break;

                    case "getAllDetailsAboutOpenTeams":
                        handle_getAllDetailsAboutOpenTeams_Union(splitLine, clientSocket);
                        break;

                    case "getAllOpenTeams_Union":
                        handle_getAllOpenTeams_Union(splitLine, clientSocket);
                        break;

                    case "getAllPastGames_UR":
                        handle_getAllPastGames_UR(splitLine, clientSocket);
                        break;

                    case "getAllReferees":
                        handle_getAllReferees(splitLine, clientSocket);
                        break;


                    // ------------------- Default -------------------

                    default:
                        Logger.logError("Server: invalid operation received");
                        //Liat added this line to test presentation stuff
                        sendLineToClient("", clientSocket);
                        System.out.println("Invalid operation received: " + operation);
                        break;


                }


            }

            catch (SocketException se)
            {
                Logger.logServer(clientSocket.getInetAddress().toString() + " Disconnected");
                removeSocket(clientSocket);
                break;
            }
            catch (Exception e) {
                Logger.logError("Server: Data reading");
                e.printStackTrace();
            }

        }

        removeSocket(clientSocket);

    }

    private void removeSocket(Socket clientSocket)
    {

        synchronized (loggedUsersMutex)
        {
            if (loggedUsers.containsValue(clientSocket))
            {
                for (String key : loggedUsers.keySet())
                    if (loggedUsers.get(key) == clientSocket)
                    {
                        loggedUsers.remove(key);
                        loggedUsersNotifications.remove(key);
                    }
            }
        }

    }

    private void handle_getAllAssignmentsPolicies(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = unionRepresentativeSystem.getAllAssignmentsPolicies(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting Assignment Policies", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting Assignment Policies", clientSocket);
        }

    }

    private void handle_getAllTeamAssets_R(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = refereeSystem.getAllTeamAssets(splitLine[1], splitLine[2]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all team's assets", clientSocket);
        }
        catch (Exception e){
            sendLineToClient("Failed getting all team's assets", clientSocket);
        }

    }

    private void handle_getUserInfo(String[] splitLine, Socket clientSocket)
    {
        try{
            String results = userSystem.getUserInfo(splitLine[1]);

            if (results != null)
                sendLineToClient(results, clientSocket);
            else
                sendLineToClient("Failed getting user information", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting user information", clientSocket);
        }

    }

    private void handle_getAllReferees(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllReferees(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all referees", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all referees", clientSocket);
        }

    }

    private void handle_getAllClosedTeam(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = teamSystem.getAllClosedTeam(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all closed teams", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all closed teams", clientSocket);
        }

    }

    private void handle_getAllTeamAssets(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = teamSystem.getAllTeamAssets(splitLine[1], splitLine[2]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all team's assets", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all team's assets", clientSocket);
        }

    }

    private void handle_getAllFields(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = teamSystem.getAllFields(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all fields", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all fields", clientSocket);
        }

    }

    private void handle_getAllCoaches(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = teamSystem.getAllCoaches(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all coaches", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all coaches", clientSocket);
        }

    }

    private void handle_getAllPlayers(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = teamSystem.getAllPlayers(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all players", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all players", clientSocket);
        }

    }

    private void handle_getAllOccurringGame(String[] splitLine, Socket clientSocket)
    {
        try{
            String results = refereeSystem.getAllOccurringGame(splitLine[1]);

            if (results != null)
                sendLineToClient(results, clientSocket);
            else
                sendLineToClient("", clientSocket);
        }catch (Exception e){
            sendLineToClient("", clientSocket);
        }

    }

    private void handle_getAllPastGames_R(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = refereeSystem.getAllPastGames(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all past games", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all past games", clientSocket);
        }

    }

    private void handle_changeEvent(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = refereeSystem.changeEvent(splitLine[1], splitLine[2], splitLine[3], splitLine[4]);

            if (success)
                sendLineToClient("Succeed changing game's event", clientSocket);
            else
                sendLineToClient("Failed changing game's event", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing game's event", clientSocket);
        }

    }

    private void handle_getGameReport(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = refereeSystem.getGameReport(splitLine[1], splitLine[2]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting game report", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting game report", clientSocket);
        }

    }

    private void handle_setScoreInGame(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = refereeSystem.setScoreInGame(splitLine[1], splitLine[2], Integer.parseInt(splitLine[3]), Integer.parseInt(splitLine[4]));

            if (success)
                sendLineToClient("Succeed setting game's score", clientSocket);
            else
                sendLineToClient("Failed setting game's score", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed setting game's score", clientSocket);
        }

    }

    private void handle_addEventToGame(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = refereeSystem.addEventToGame(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5]);

            if (success)
                sendLineToClient("Succeed adding event to game", clientSocket);
            else
                sendLineToClient("Failed adding event to game", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding event to game", clientSocket);
        }

    }

    private void handle_viewPage(String[] splitLine, Socket clientSocket)
    {
        try{
            String res = personalPageSystem.viewPage(splitLine[1], splitLine[2]);

            if (res == null)
                sendLineToClient("No page found", clientSocket);
            else
                sendLineToClient(res, clientSocket);
        }catch (Exception e){
            sendLineToClient("No page found", clientSocket);
        }


    }

    private void handle_addPaymentsFromTheTUTU(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.addPaymentsFromTheTUTU(splitLine[1], splitLine[2], splitLine[3], Double.parseDouble(splitLine[4]), proxyAccountingSystem);

            if (success)
                sendLineToClient("Succeed adding payments from TUTU", clientSocket);
            else
                sendLineToClient("Failed adding payments from TUTU", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding payments from TUTU", clientSocket);
        }

    }

    private void handle_addTUTUPayment(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.addTUTUPayment(splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]));

            if (success)
                sendLineToClient("Succeed adding TUTU Payment", clientSocket);
            else
                sendLineToClient("Failed adding TUTU Payment", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding TUTU Payment", clientSocket);
        }

    }

    private void handle_getAllPastGames_UR(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllPastGames(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all past games", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all past games", clientSocket);
        }

    }

    private void handle_getAllOpenTeams_Union(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllOpenTeams(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all open teams", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all open teams", clientSocket);
        }

    }

    private void handle_getAllDetailsAboutOpenTeams_Union(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllDetailsAboutOpenTeams(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting open teams details", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting open teams details", clientSocket);
        }

    }

    private void handle_getAllScorePolicies(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllScorePolicies(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting Score Policies", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting Score Policies", clientSocket);
        }

    }

    private void handle_getAllSeasons(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllSeasons(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all Seasons", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all Seasons", clientSocket);
        }

    }

    private void handle_getAllLeagues(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.getAllLeagues(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all Leagues", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all Leagues", clientSocket);
        }

    }

    private void handle_allLeaguesInSeasons(String[] splitLine, Socket clientSocket)
    {
        try{
            List<String> results = unionRepresentativeSystem.allLeaguesInSeasons(splitLine[1]);

            if (results != null)
                sendLineToClient(ListToString(results), clientSocket);
            else
                sendLineToClient("Failed getting all leagues in seasons", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting all leagues in seasons", clientSocket);
        }

    }

    private void handle_addFieldToSystem(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.addFieldToSystem(splitLine[1], splitLine[2], splitLine[3], Integer.parseInt(splitLine[4]), Double.parseDouble(splitLine[5]));

            if (success)
                sendLineToClient("Succeed adding field to system", clientSocket);
            else
                sendLineToClient("Failed adding field to system", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding field to system", clientSocket);
        }

    }

    private void handle_changeRegistrationFee(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.changeRegistrationFee(splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]));

            if (success)
                sendLineToClient("Succeed changing Registration Fee", clientSocket);
            else
                sendLineToClient("Failed changing Registration Fee", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing Registration Fee", clientSocket);
        }

    }

    private void handle_calculateGameScore(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.calculateGameScore(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed calculating game's score", clientSocket);
            else
                sendLineToClient("Failed calculating game's score", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed calculating game's score", clientSocket);
        }

    }

    private void handle_calculateLeagueScore(String[] splitLine, Socket clientSocket)
    {
        try{
            boolean success = unionRepresentativeSystem.calculateLeagueScore(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed calculating league's score", clientSocket);
            else
                sendLineToClient("Failed calculating league's score", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed calculating league's score", clientSocket);
        }

    }


    private void handle_getRegistrationFee(String[] splitLine, Socket clientSocket) {
        try{
            double res = unionRepresentativeSystem.getRegistrationFee(splitLine[1], splitLine[2]);
            if (res == -1)
                sendLineToClient("Registration fee is unavailable", clientSocket);
            else
                sendLineToClient("" + res, clientSocket);
        }catch (Exception e){
            sendLineToClient("Registration fee is unavailable", clientSocket);
        }

    }


    private void handle_addTeamToLeague(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.addTeamToLeague(splitLine[1], splitLine[2], splitLine[3], proxyAccountingSystem);

            if (success)
                sendLineToClient("Succeed adding team to league", clientSocket);
            else
                sendLineToClient("Failed adding team to league", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding team to league", clientSocket);
        }

    }

    private void handle_changeGameLocation(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.changeGameLocation(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed changing game's location", clientSocket);
            else
                sendLineToClient("Failed changing game's location", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing game's location", clientSocket);
        }

    }

    private void handle_changeGameDate(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.changeGameDate(splitLine[1], splitLine[2], stringToDate(splitLine[3]));

            if (success)
                sendLineToClient("Succeed changing game's date", clientSocket);
            else
                sendLineToClient("Failed changing game's date", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing game's date", clientSocket);
        }

    }

    private void handle_assignGames(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.assignGames(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed assigning games", clientSocket);
            else
                sendLineToClient("Failed assigning games", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed assigning games", clientSocket);
        }


    }

    private void handle_changeAssignmentPolicy(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.changeAssignmentPolicy(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed changing Assignment Policy", clientSocket);
            else
                sendLineToClient("Failed changing Assignment Policy", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing Assignment Policy", clientSocket);
        }

    }

    private void handle_changeScorePolicy(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.changeScorePolicy(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed changing Score Policy", clientSocket);
            else
                sendLineToClient("Failed changing Score Policy", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed changing Score Policy", clientSocket);
        }

    }

    private void handle_assignRefToLeague(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = unionRepresentativeSystem.assignRefToLeague(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed assigning referee to league", clientSocket);
            else
                sendLineToClient("Failed assigning referee to league", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed assigning referee to league", clientSocket);
        }

    }

    private void handle_appointReferee(String[] splitLine, Socket clientSocket) {
        try{
            String success = unionRepresentativeSystem.appointReferee(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5]);

            if (success != null)
                sendLineToClient("Succeed appointing a new referee", clientSocket);
            else
                sendLineToClient("Failed appointing a new referee", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed appointing a new referee", clientSocket);
        }

    }

    private void handle_configureLeagueInSeason(String[] splitLine, Socket clientSocket) {
        try{
            String success = unionRepresentativeSystem.configureLeagueInSeason(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5], Double.parseDouble(splitLine[6]));

            if (success != null)
                sendLineToClient("Succeed configuring league in season", clientSocket);
            else
                sendLineToClient("Failed configuring league in season", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed configuring league in season", clientSocket);
        }


    }

    private void handle_configureNewSeason(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String success = unionRepresentativeSystem.configureNewSeason(splitLine[1], Integer.parseInt(splitLine[2]), stringToDate(splitLine[3]));

        if (success !=null)
            sendLineToClient("Succeed configuring a new season", clientSocket);
        else
            sendLineToClient("Failed configuring a new season", clientSocket);
    }

    private void handle_configureNewLeague(String[] splitLine, Socket clientSocket) {
        try{
            String success = unionRepresentativeSystem.configureNewLeague(splitLine[1], splitLine[2], splitLine[3]);

            if (success !=null)
                sendLineToClient("Succeed configuring a new league", clientSocket);
            else
                sendLineToClient("Failed configuring a new league", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed configuring a new league", clientSocket);
        }


    }

    private void handle_getAllUsers_Team(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = teamSystem.getAllUsers(splitLine[1]);
            if (results == null)
                sendLineToClient("No teams available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No teams available", clientSocket);
        }


    }

    private void handle_getTeams(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = teamSystem.getTeams(splitLine[1]);
            if (results == null)
                sendLineToClient("No teams available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No teams available", clientSocket);
        }

    }

    private void handle_getAllActiveComplaints(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllActiveComplaints(splitLine[1]);
            if (results == null)
                sendLineToClient("No active complaints", clientSocket);
            else {
                String sendToClient = ListToString(results);
                sendLineToClient(sendToClient, clientSocket);
            }
        }catch (Exception e){
            sendLineToClient("No active complaints", clientSocket);
        }

    }

    private void handle_getAllUsers_Admin(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllUsers(splitLine[1]);
            if (results == null)
                sendLineToClient("No users available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No users available", clientSocket);
        }

    }

    private void handle_getAllCloseTeams(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllCloseTeams(splitLine[1]);
            if (results == null)
                sendLineToClient("No closed teams available", clientSocket);
            else {
                String sendToClient = ListToString(results);
                sendLineToClient(sendToClient, clientSocket);
            }
        }catch (Exception e){
            sendLineToClient("No closed teams available", clientSocket);
        }


    }

    private void handle_getAllDetailsAboutCloseTeams(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllDetailsAboutCloseTeams(splitLine[1]);
            if (results == null)
                sendLineToClient("No closed teams available", clientSocket);
            else {
                String sendToClient = ListToString(results);
                sendLineToClient(sendToClient, clientSocket);
            }
        }catch (Exception e){
            sendLineToClient("No closed teams available", clientSocket);
        }

    }

    private void handle_getAllOpenTeams_Admin(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllOpenTeams(splitLine[1]);
            if (results == null)
                sendLineToClient("No open teams available", clientSocket);
            else {
                String sendToClient = ListToString(results);
                sendLineToClient(sendToClient, clientSocket);
            }
        }catch (Exception e){
            sendLineToClient("No open teams available", clientSocket);
        }


    }

    private void handle_getAllDetailsAboutOpenTeams_Admin(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = adminSystem.getAllDetailsAboutOpenTeams(splitLine[1]);
            if (results == null)
                sendLineToClient("No open teams available", clientSocket);
            else {
                String sendToClient = ListToString(results);
                sendLineToClient(sendToClient, clientSocket);
            }
        }catch (Exception e){
            sendLineToClient("No open teams available", clientSocket);
        }

    }

    private void handle_getUserRoles(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.getUserRoles(splitLine[1]);
            if (results == null)
                sendLineToClient("No roles available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No roles available", clientSocket);
        }

    }

    private void handle_userSearch(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.search(splitLine[1], splitLine[2]);
            if (results == null)
                sendLineToClient("No results returned from search", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No results returned from search", clientSocket);
        }

    }

    private void handle_getRoleForCoach(String[] splitLine, Socket clientSocket) {
        try{
            String res = userSystem.getRoleForCoach(splitLine[1]);
            if (!res.equals(""))
                sendLineToClient(res, clientSocket);
            else
                sendLineToClient("ID doesn't exist", clientSocket);
        }catch (Exception e){
            sendLineToClient("ID doesn't exist", clientSocket);
        }

    }

    private void handle_getRoleForPlayer(String[] splitLine, Socket clientSocket) {
        try{
            String res = userSystem.getRoleForPlayer(splitLine[1]);
            if (!res.equals(""))
                sendLineToClient(res, clientSocket);
            else
                sendLineToClient("ID doesn't exist", clientSocket);
        }catch (Exception e){
            sendLineToClient("ID doesn't exist", clientSocket);
        }

    }

    private void handle_updateRoleForCoach(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.updateRoleForCoach(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed updating coach's role", clientSocket);
            else
                sendLineToClient("Failed updating coach's role", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed updating coach's role", clientSocket);
        }

    }

    private void handle_updateRoleForPlayer(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.updateRoleForPlayer(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed updating player's role", clientSocket);
            else
                sendLineToClient("Failed updating player's role", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed updating player's role", clientSocket);
        }

    }

    private void handle_updateTrainingForReferee(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.updateTrainingForReferee(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed updating referee's training", clientSocket);
            else
                sendLineToClient("Failed updating referee's training", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed updating referee's training", clientSocket);
        }

    }

    private void handle_updateTrainingForCoach(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.updateTrainingForCoach(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed updating coach's training", clientSocket);
            else
                sendLineToClient("Failed updating coach's training", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed updating coach's training", clientSocket);
        }

    }

    private void handle_registrationForGamesAlerts(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.registrationForGamesAlerts(splitLine[1], stringToList(splitLine[2]), stringToBoolean(splitLine[3]));

            if (success)
                sendLineToClient("Succeed registering for game alerts", clientSocket);
            else
                sendLineToClient("Failed registering for game alerts", clientSocket);
        }catch (Exception e){
            e.printStackTrace();
            sendLineToClient("Failed registering for game alerts", clientSocket);
        }

    }

    private void handle_getAllFutureGames(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.getAllFutureGames(splitLine[1]);
            if (results == null)
                sendLineToClient("No future games available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No future games available", clientSocket);
        }


    }

    private void handle_getAllPages(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.getAllPages(splitLine[1]);
            if (results == null)
                sendLineToClient("No pages available", clientSocket);
            else
                sendLineToClient(ListToString(results), clientSocket);
        }catch (Exception e){
            sendLineToClient("No pages available", clientSocket);
        }

    }

    private void handle_registrationToFollowUp(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.registrationToFollowUp(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed registering to follow up a page", clientSocket);
            else
                sendLineToClient("Failed registering to follow up a page", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed registering to follow up a page", clientSocket);
        }

    }

    private void handle_getBalance(String[] splitLine, Socket clientSocket) {
        try{
            double budget = financeTransactionsSystem.getBalance(splitLine[1], splitLine[2]);
            sendLineToClient("" + budget, clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed getting balance!", clientSocket);
        }

    }

    private void handle_reportNewExpanse(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = financeTransactionsSystem.reportNewExpanse(splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]));

            if (success)
                sendLineToClient("Succeed reporting new expanse", clientSocket);
            else
                sendLineToClient("Failed reporting new expanse", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed reporting new expanse", clientSocket);
        }

    }

    private void handle_reportNewIncome(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        boolean success = financeTransactionsSystem.reportNewIncome(splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]));

        if (success)
            sendLineToClient("Succeed reporting new income", clientSocket);
        else
            sendLineToClient("Failed reporting new income", clientSocket);
    }

    private void handle_uploadToPage(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = personalPageSystem.uploadToPage(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed uploading to personal page", clientSocket);
            else
                sendLineToClient("Failed uploading to personal page", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed uploading to personal page", clientSocket);
        }

    }

    private void handle_addAssetPlayer(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.addAssetPlayer(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed adding a Player to team", clientSocket);
            else
                sendLineToClient("Failed adding a Player to team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding a Player to team", clientSocket);
        }

    }

    private void handle_addAssetCoach(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.addAssetCoach(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed adding a Coach to team", clientSocket);
            else
                sendLineToClient("Failed adding a Coach to team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding a Coach to team", clientSocket);
        }

    }

    private void handle_addField(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.addField(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed adding a Field to team", clientSocket);
            else
                sendLineToClient("Failed adding a Field to team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding a Field to team", clientSocket);
        }

    }

    private void handle_removeField(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.removeField(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed removing a Field from team", clientSocket);
            else
                sendLineToClient("Failed removing a Field from team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing a Field from team", clientSocket);
        }

    }

    private void handle_removeAssetPlayer(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.removeAssetPlayer(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed removing a Player from team", clientSocket);
            else
                sendLineToClient("Failed removing a Player from team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing a Player from team", clientSocket);
        }

    }

    private void handle_removeAssetCoach(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.removeAssetCoach(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed removing a Coach from team", clientSocket);
            else
                sendLineToClient("Failed removing a Coach from team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing a Coach from team", clientSocket);
        }

    }

    private void handle_updateAsset(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.updateAsset(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5]);

            if (success)
                sendLineToClient("Succeed updating asset", clientSocket);
            else
                sendLineToClient("Failed updating asset", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed updating asset", clientSocket);
        }

    }

    private void handle_createTeam(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.createTeam(splitLine[1], splitLine[2], stringToList(splitLine[3]), stringToList(splitLine[4]), splitLine[5]);

            if (success)
                sendLineToClient("Succeed creating team", clientSocket);
            else
                sendLineToClient("Failed creating team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed creating team", clientSocket);
        }

    }

    private void handle_appointmentTeamOwner(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.appointmentTeamOwner(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed appointing a Team Owner to team", clientSocket);
            else
                sendLineToClient("Failed appointing a Team Owner to team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed appointing a Team Owner to team", clientSocket);
        }

    }

    private void handle_appointmentTeamManager(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.appointmentTeamManager(splitLine[1], splitLine[2], splitLine[3], Double.parseDouble(splitLine[4]), stringToBoolean(splitLine[5]), stringToBoolean(splitLine[6]));

            if (success)
                sendLineToClient("Succeed appointing a Team Manager to team", clientSocket);
            else
                sendLineToClient("Failed appointing a Team Manager to team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed appointing a Team Manager to team", clientSocket);
        }

    }

    private void handle_removeAppointmentTeamOwner(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.removeAppointmentTeamOwner(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed removing appointed Team Owner from team", clientSocket);
            else
                sendLineToClient("Failed removing appointed Team Owner from team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing appointed Team Owner from team", clientSocket);
        }

    }

    private void handle_removeAppointmentTeamManager(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.removeAppointmentTeamManager(splitLine[1], splitLine[2], splitLine[3]);

            if (success)
                sendLineToClient("Succeed removing appointed Team Manager from team", clientSocket);
            else
                sendLineToClient("Failed removing appointed Team Manager from team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing appointed Team Manager from team", clientSocket);
        }

    }

    private void handle_closeTeam(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.closeTeam(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed closing team", clientSocket);
            else
                sendLineToClient("Failed closing team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed closing team", clientSocket);
        }

    }

    private void handle_reOpeningTeam(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = teamSystem.reOpeningTeam(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed reopening team", clientSocket);
            else
                sendLineToClient("Failed reopening team", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed reopening team", clientSocket);
        }

    }

    private void handle_getFanPages(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.getFanPages(splitLine[1]);
            if(results!=null){
                String res = ListToString(results);

                sendLineToClient(res, clientSocket);
            }
            else
                sendLineToClient("", clientSocket);

        }catch (Exception e){
            sendLineToClient("", clientSocket);
        }

    }

    private void handle_addComplaint(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.addComplaint(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed adding a complaint", clientSocket);
            else
                sendLineToClient("Failed adding a complaint", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed adding a complaint", clientSocket);
        }

    }

    private void handle_editPersonalInfo(String[] splitLine, Socket clientSocket) {
        try{
            userSystem.editPersonalInfo(splitLine[1], splitLine[2], splitLine[3]);

            sendLineToClient("Succeed Editing personal info", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed Editing personal info", clientSocket);
        }

    }

    private void handle_editFanPersonalDetails(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = userSystem.editFanPersonalDetails(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5]);

            if (success)
                sendLineToClient("Succeed Editing fan personal info", clientSocket);
            else
                sendLineToClient("Failed Editing fan personal info", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed Editing fan personal info", clientSocket);
        }

    }

    private void handle_viewSearchHistory(String[] splitLine, Socket clientSocket) {
        try{
            List<String> results = userSystem.viewSearchHistory(splitLine[1]);
            String res = ListToString(results);
            sendLineToClient(res, clientSocket);
        }catch (Exception e){}


    }

    private void handle_removeUser(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = adminSystem.removeUser(splitLine[1], splitLine[2]);

            if (success)
                sendLineToClient("Succeed removing user " + splitLine[2], clientSocket);
            else
                sendLineToClient("Failed removing user " + splitLine[2], clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed removing user " + splitLine[2], clientSocket);
        }

    }

    private void handle_trainModel(String[] splitLine, Socket clientSocket) {
        try{
            boolean success = adminSystem.trainModel(splitLine[1]);

            if (success)
                sendLineToClient("Succeed activating the training model", clientSocket);
            else
                sendLineToClient("Failed activating the training model", clientSocket);
        }catch (Exception e){
            sendLineToClient("Failed activating the training model", clientSocket);
        }



    }

    private void handle_viewLog(String[] splitLine, Socket clientSocket) {
        try{
            List<String> res = adminSystem.viewLog(splitLine[1], splitLine[2]);
            String sendToClient = ListToString(res);

            sendLineToClient(sendToClient, clientSocket);
        }catch (Exception e){}

    }


    // +++++++++++++++++++++++++++ Handle_ Functions +++++++++++++++++++++++++++

    private void handle_guestSearch(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> resultsList = guestSystem.search(splitLine[1]);

        String sendToClient = ListToString(resultsList);

        sendLineToClient(sendToClient, clientSocket);

    }

    private void handle_viewPersonalDetails(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String res = userSystem.viewPersonalDetails(splitLine[1]);
        sendLineToClient(res, clientSocket);
    }


    private void handle_responseToComplaint(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        adminSystem.responseToComplaint(splitLine[1], splitLine[2], splitLine[3]);

    }

    private void handle_permanentlyCloseTeam(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String teamName = adminSystem.permanentlyCloseTeam(splitLine[1], splitLine[2]);
        if (teamName != null)
            sendLineToClient("Succeed closing the team " + teamName, clientSocket);
        else
            sendLineToClient("Failed closing the team ", clientSocket);
    }

    private void handle_addNewAdmin(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedAdminId = adminSystem.addNewAdmin(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5]);

        if (addedAdminId == null)
            sendLineToClient("Failed adding a new Admin", clientSocket);
        else
            sendLineToClient("Succeed adding a new Admin", clientSocket);
    }

    private void handle_addNewUnionRepresentative(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedRepresentativeId = adminSystem.addNewUnionRepresentative(splitLine[1], splitLine[2], splitLine[3], splitLine[4]);

        if (addedRepresentativeId == null)
            sendLineToClient("Failed adding a new Union Representative", clientSocket);
        else
            sendLineToClient("Succeed adding a new Union Representative", clientSocket);
    }

    private void handle_addNewTeamManager(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedTeamManagerId = adminSystem.addNewTeamManager(splitLine[1], splitLine[2], splitLine[3], splitLine[4], Double.parseDouble(splitLine[5]), stringToBoolean(splitLine[6]), stringToBoolean(splitLine[7]));

        if (addedTeamManagerId == null)
            sendLineToClient("Failed adding a new Team Manager", clientSocket);
        else
            sendLineToClient("Succeed adding a new Team Manager", clientSocket);
    }

    private void handle_addNewTeamOwner(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedTeamOwnerId = adminSystem.addNewTeamOwner(splitLine[1], splitLine[2], splitLine[3], splitLine[4]);

        if (addedTeamOwnerId == null)
            sendLineToClient("Failed adding a new Team Owner", clientSocket);
        else
            sendLineToClient("Succeed adding a new Team Owner", clientSocket);
    }

    private void handle_addNewCoach(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedCoachId = adminSystem.addNewCoach(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5], splitLine[6], Double.parseDouble(splitLine[7]));

        if (addedCoachId == null)
            sendLineToClient("Failed adding a new Coach", clientSocket);
        else
            sendLineToClient("Succeed adding a new Coach", clientSocket);
    }

    private void handle_addNewPlayer(String[] splitLine, Socket clientSocket) {
        try{}catch (Exception e){}
        String addedPlayerId = adminSystem.addNewPlayer(splitLine[1], splitLine[2], splitLine[3], splitLine[4], stringToDate(splitLine[5]), splitLine[6], Double.parseDouble(splitLine[7]));
        if (addedPlayerId == null)
            sendLineToClient("Failed adding a new Player", clientSocket);
        else
            sendLineToClient("Succeed adding a new Player", clientSocket);

    }


    private void handle_Register(String[] splitLine, Socket clientSocket) {
        try{
            String userId = guestSystem.register(splitLine[1], splitLine[2], splitLine[3], splitLine[4], splitLine[5], splitLine[6]);

            if (userId == null)
            {
                sendLineToClient("Registration Failed!", clientSocket);
                return;
            }

            List<String> roles = userSystem.getUserRoles(userId); /**/

            synchronized (loggedUsersMutex) {
                if (!loggedUsers.containsKey(userId))
                    loggedUsers.put(userId, clientSocket);
            }

            String sendToClient = userId + "|";

            for (String r : roles)
                sendToClient = sendToClient + r + "|";

            sendLineToClient(sendToClient.substring(0, sendToClient.length() - 1), clientSocket);

            userSystem.logIn(splitLine[1], splitLine[2]);
        }catch (Exception e){
            sendLineToClient("Registration Failed!", clientSocket);
        }


    }

    private void handle_Logout(String[] splitLine, Socket clientSocket) {
        try{
            userSystem.logOut();

            synchronized (loggedUsersMutex) {

                if (loggedUsers.containsKey(splitLine[1])) {
                    loggedUsers.remove(splitLine[1]);
                    loggedUsersNotifications.remove(splitLine[1]);
                }

            }
            sendLineToClient("Logout Successful", clientSocket);
        }catch (Exception e){
            sendLineToClient("Logout Failed", clientSocket);
        }


    }

    private void handle_Login(String[] splitLine, Socket clientSocket) {
        try{
            String loggedUserId = guestSystem.logIn(splitLine[1], splitLine[2]);
            if (loggedUserId == null) {
                sendLineToClient("Login Failed", clientSocket);
            } else {
                List<String> roles = userSystem.getUserRoles(loggedUserId); /**/

                synchronized (loggedUsersMutex) {
                    if (!loggedUsers.containsKey(loggedUserId))
                        loggedUsers.put(loggedUserId, clientSocket);
                }

                String sendToClient = loggedUserId + "|";

                for (String r : roles)
                    sendToClient = sendToClient + r + "|";

                sendLineToClient(sendToClient.substring(0, sendToClient.length() - 1), clientSocket);

                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                List<String> userNotifications = NotificationSystem.getAllNotification(loggedUserId);

                if (userNotifications != null)
                    for (String not : userNotifications)
                        sendNotification(loggedUserId, not);

            }
        }catch (Exception e){
            sendLineToClient("Login Failed", clientSocket);
        }

    }

    private void handle_viewInformationAboutReferees(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutReferees();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);

    }

    private void handle_viewInformationAboutSeasons(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutSeasons();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);
    }

    private void handle_viewInformationAboutLeagues(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutLeagues();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);
    }

    private void handle_viewInformationAboutCoaches(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutCoaches();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);
    }

    private void handle_viewInformationAboutPlayers(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutPlayers();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);
    }

    private void handle_viewInformationAboutTeams(Socket clientSocket) {
        try{}catch (Exception e){}
        List<String> res = guestSystem.viewInformationAboutTeams();
        String result = ListToString(res);
        sendLineToClient(result, clientSocket);
    }


    // +++++++++++++++++++++++++++ Other Functions +++++++++++++++++++++++++++


    private static void sendLineToClient(String stringToSend, Socket client) {
        try {
            DataOutputStream outStream = new DataOutputStream(client.getOutputStream());

            if (stringToSend.length() == 0 || stringToSend.charAt(stringToSend.length() - 1) != '\n')
                stringToSend = stringToSend + "\n";

            //stringToSend = stringToSend + "END REQUEST" + "\n";

            outStream.writeBytes(stringToSend);
            outStream.flush();


        }

        catch (SocketException se)
        {

        }
        catch (Exception e) {
            Logger.logError("Sending " + stringToSend + " Failed");
            e.printStackTrace();
        }
    }

    private List<String> objListToStringList(List<Object> objList) {
        List<String> res = new LinkedList<>();

        for (Object ob : objList)
            res.add(ob.toString());

        return res;
    }

    private List<String> stringToList(String ans) {
        List<String> res = new LinkedList<>();

        String[] split = ans.split("~");

        for (String s : split)
            res.add(s);

        return res;
    }

    private String ListToString(List<String> list) {

        String res = "";

        if (list.size() == 0)
            return res;

        for (String s : list)
            res = res + s + "~";

        res = res.substring(0, res.length() - 1);
        res = res + "\n";

        return res;
    }

    private Date stringToDate(String s) {
        String[] split = s.split("\\.");
        Calendar cal = Calendar.getInstance();

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]) - 1;
        int day = Integer.parseInt(split[2]);

        cal.set(year, month, day);
        Date date = new Date(cal.getTimeInMillis());
        return date;
    }

    public static boolean sendNotification(String id, String message)
    {
        if (!loggedUsers.containsKey(id))
            return false;

        sendLineToClient("Notification: " + message, loggedUsersNotifications.get(id));
        return true;
    }
    
    private boolean stringToBoolean(String s)
    {
        return s.toLowerCase().equals("true");
    }

    public void stop() {

        stop = true;
        Logger.logServer("Server Stopped");
    }



}
