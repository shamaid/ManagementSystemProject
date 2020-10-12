package Data;
import Domain.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class Database //maybe generalize with interface? //for now red layer
{

    private static DataAccess dataAccess;

    public Database() {
        dataAccess = new DataAccess();
    }
    private static String dateToString(Date date){

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String dateString =""+localDate.getDayOfMonth();
        dateString = dateString +"."+ localDate.getMonthValue();
        dateString = dateString +"."+ localDate.getYear();
        dateString = dateString + " " + date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        return dateString;
    }

    public static Date getCurrentDate()
    {
        Date date = new Date();
        return date;
    }

    public static Date getDate(int ... args)
    {

        int numOfArgs = args.length;
        Calendar cal = Calendar.getInstance();
        int year = getCurrentYear();
        int month = getCurrentMonth();
        int day = getCurrentDay();
        int minutes = 0, hours = 0;
        Date date;

        switch (numOfArgs)
        {

            case 3:
                year = args[0];
                month = args[1] - 1;
                day = args[2];
                break;

            case 5:
                year = args[0];
                month = args[1] - 1;
                day = args[2];
                hours = args[3];
                minutes = args[4];
                break;
        }

        cal.set(year, month, day, hours, minutes, 0);
        date = new Date(cal.getTimeInMillis());
        return date;

    }

    public static int getCurrentYear()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
        Date date = getCurrentDate();
        int year = Integer.parseInt(sdf.format(date));
        return year;
    }

    public static int getCurrentMonth()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = getCurrentDate();
        int month = Integer.parseInt(sdf.format(date));
        return month;
    }

    public static int getCurrentDay()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = getCurrentDate();
        int day = Integer.parseInt(sdf.format(date));
        return day;
    }

    public static boolean updateObject(Object object){


        if(object instanceof Coach){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true;
            /**
             * [Training] [varchar](50) NOT NULL,
             * [RoleInTeam] [varchar](50) NOT NULL,
             * 	[Teams] [varchar](255) NOT NULL,
             * 	[isActive] [bit] NOT NULL,
             * 	[Price] [real] NOT NULL,
             * 	*/
            ans1 = dataAccess.updateCellValue("Coaches" ,"Training" ,((Coach) object).getID() ,((Coach) object).getTraining() );
            ans2 = dataAccess.updateCellValue("Coaches" ,"RoleInTeam" ,((Coach) object).getID() ,((Coach) object).getRoleInTeam() );
            ans3 = dataAccess.updateCellValue("Coaches" ,"Teams" ,((Coach) object).getID() , listOfTeamsToStringIDs(((Coach) object).getTeams()) );
            ans4 = dataAccess.updateCellValue("Coaches" ,"isActive" ,((Coach) object).getID() ,""+((Coach) object).isActive() );
            ans5 = dataAccess.updateCellValue("Coaches" ,"Price" ,((Coach) object).getID() ,""+((Coach) object).getPrice() );

            return ans1 && ans2 && ans3 && ans4 && ans5;
        }
        else if(object instanceof Complaint){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true;
            /**
             * [ComplaintDate] [date] NOT NULL,
             * 	[isActive] [bit] NOT NULL,
             * 	[Description] [varchar] (1000)NOT NULL ,
             * 	[ComplainedFanID] [char](50) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Complaints" ,"ComplaintDate" ,((Complaint) object).getId() ,dateToString(((Complaint) object).getDate()) );
            ans2 = dataAccess.updateCellValue("Complaints" ,"isActive" ,((Complaint) object).getId() ,""+((Complaint) object).getIsActive());
            ans3 = dataAccess.updateCellValue("Complaints" ,"Description" ,((Complaint) object).getId() ,((Complaint) object).getDescription() );
            ans4 = dataAccess.updateCellValue("Complaints" ,"ComplainedFanID" ,((Complaint) object).getId() ,((Complaint) object).getFanComplained().getID() );
            ans5 = dataAccess.updateCellValue("Complaints" ,"Response" ,((Complaint) object).getId() ,((Complaint) object).getResponse() );

            return ans1 && ans2 && ans3 && ans4 && ans5;
        }

        else if(object instanceof Event){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             *
             * [EventType] [char](50) NOT NULL,
             * 	[EventDate] [date] NOT NULL,
             * 	[MinutesInGame] [real] NOT NULL,
             * 	[Description] [varchar](max) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Events","EventType",((Event) object).getId() ,""+((Event) object).getType());
            ans2 = dataAccess.updateCellValue("Events","EventDate", ((Event) object).getId(),dateToString(((Event) object).getDate()));
            ans3 = dataAccess.updateCellValue("Events","MinutesInGame",((Event) object).getId(),""+((Event) object).getMinuteInGame());
            ans4 = dataAccess.updateCellValue("Events","Description" ,((Event) object).getId(),((Event) object).getDescription());


            return ans1 && ans2 && ans3 && ans4 ;
        }
        else if(object instanceof EventReport){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             *
             * 	[EventsIDs] [varchar](max) NOT NULL,
             * */

            ans2 = dataAccess.updateCellValue("EventReports","EventsIDs",((EventReport) object).getId() , getEventsId(((EventReport)object).getEvents()));


            return ans1 && ans2 && ans3 && ans4 ;
        }
        else if(object instanceof Fan){
            boolean ans1=true,ans2=true,ans3=true,ans4=true ,ans5=true;
            /**
             * [Address] [varchar](255) NOT NULL,
             * 	[Phone] [varchar](50) NOT NULL unique,
             * 	[FollowedPagesIDs] [varchar](255) ,
             *[ComplaintsIDs] [varchar](255) ,
             * */

            ans2 = dataAccess.updateCellValue("Fans" ,"Address" , ((Fan)object).getID(),((Fan) object).getAddress() );
            ans3 = dataAccess.updateCellValue("Fans" ,"Phone" ,((Fan)object).getID() , ((Fan) object).getPhone());
            ans4 = dataAccess.updateCellValue("Fans" ,"FollowedPagesIDs" , ((Fan)object).getID(),getFollowPagesId(((Fan) object).getFollowPages()));
            ans4 = dataAccess.updateCellValue("Fans" ,"ComplaintsIDs" , ((Fan)object).getID(), getComplaintsId(((Fan) object).getComplaintsId()));

            return ans1 && ans2 && ans3 && ans4 && ans5;
        }
        else if(object instanceof Field){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true,ans6=true;
            /**
             *
             * [Location] [char](50) NOT NULL,
             * 	[Name] [char](50) NOT NULL,
             * 	[Capacity] [int] NOT NULL,
             * 	[Teams] [varchar](255) NOT NULL,
             * 	[isActive] [bit] NOT NULL,
             * 	[Price] [real] NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Fields","Location",((Field) object).getID() ,((Field) object).getLocation());
            ans2 = dataAccess.updateCellValue("Fields","Name",((Field) object).getID() ,((Field) object).getName());
            ans3 = dataAccess.updateCellValue("Fields","Capacity", ((Field) object).getID(),""+((Field) object).getCapacity());
            ans4 = dataAccess.updateCellValue("Fields","Teams", ((Field) object).getID(),listOfTeamsToStringIDs(((Field) object).getTeams()) );
            ans5 = dataAccess.updateCellValue("Fields","isActive" ,((Field) object).getID(),""+((Field) object).isActive());
            ans6 = dataAccess.updateCellValue("Fields","Price" ,((Field) object).getID(),""+((Field) object).getPrice());


            return ans1 && ans2 && ans3 && ans4  && ans5 && ans6;
        }
        else if(object instanceof Game){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,
                    ans5=true,ans6=true,ans7=true,ans8=true,
                    ans9=true,ans10=true ,ans11=true;
            /**
             *
             [GameDate] [date] NOT NULL,
             [HostScore] [int] NOT NULL,
             [GuestScore] [int] NOT NULL,
             [FieldID] [char] (50) NOT NULL,
             [MainRefereeID] [char] (50) NOT NULL,
             [SideRefereesIDs] [char] (50) NOT NULL,
             [HostTeamID] [char] (50) NOT NULL,
             [GuestTeamID] [char] (50) NOT NULL,
             [AlertsFansIDs] [varchar](max) NOT NULL,
             [EventReportID] [char] (50) NOT NULL,
             [LeagueInSeasonID] [char] (50) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Games","GameDate",((Game) object).getId(), dateToString(((Game) object).getDate()));
            ans2 = dataAccess.updateCellValue("Games","HostScore", ((Game) object).getId(),""+((Game) object).hostScore());
            ans3 = dataAccess.updateCellValue("Games","GuestScore", ((Game) object).getId(),""+((Game) object).guestScore());
            ans4 = dataAccess.updateCellValue("Games","FieldID" ,((Game) object).getId(),((Game) object).getField().getID());
            ans5 = dataAccess.updateCellValue("Games","MainRefereeID" ,((Game) object).getId(),((Game) object).getMainReferee().getID());
            ans6 = dataAccess.updateCellValue("Games","SideRefereesIDs" ,((Game) object).getId(),((Game) object).getSideRefereesId() );
            ans7 = dataAccess.updateCellValue("Games","HostTeamID" ,((Game) object).getId(),((Game) object).getHostTeam().getID());
            ans8 = dataAccess.updateCellValue("Games","GuestTeamID" ,((Game) object).getId(),((Game) object).getGuestTeam().getID());
            ans9 = dataAccess.updateCellValue("Games","AlertsFansIDs" ,((Game) object).getId(),getFansForAlerts(((Game) object).getFansForAlerts()));
            ans10 = dataAccess.updateCellValue("Games","EventReportID" ,((Game) object).getId(),((Game) object).getEventReport().getId());
            ans11 = dataAccess.updateCellValue("Games","LeagueInSeasonID" ,((Game) object).getId(),((Game) object).getLeague().getId());


            return ans1 && ans2 && ans3 && ans4  && ans5 &&
                    ans6 && ans7 && ans8 && ans9  && ans10 && ans11;
        }
        else if(object instanceof League){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             [Name] [varchar](50) NOT NULL,
             [LeagueLevel] [varchar](50) NOT NULL,
             [LeaguesInSeasonsIDs] [varchar](255) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Leagues" ,"Name" , ((League) object).getId() , ((League) object).getName());
            ans2 = dataAccess.updateCellValue("Leagues" ,"LeagueLevel" ,((League) object).getId() ,((League) object).getLevel() );
            ans3 = dataAccess.updateCellValue("Leagues" ,"LeaguesInSeasonsIDs" ,((League) object).getId() ,leagueInSeasonToStringIDs(((League) object).getLeagueInSeasons()) );
            //ans4 = dataAccess.updateCellValue("Leagues" ,"SeasonsIDs" , ((League) object).getId(),getSeasonsFromLeagueInSeasons(((League) object).getLeagueInSeasons()) );

            return ans1 && ans2 && ans3 && ans4 ;
        }
        else if(object instanceof LeagueInSeason){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true ,ans6=true,ans7=true,ans8=true,ans9=true;
            /**
             *
             [AssignmentPolicy] [char](255) NOT NULL,
             [ScorePolicy] [char](255) NOT NULL,
             [GamesIDs] [varchar](255) NOT NULL,
             [RefereesIDs] [varchar](255) NOT NULL,
             [TeamsIDs] [varchar](255) NOT NULL,
             [RegistrationFee] [real] NOT NULL,
             [Records] [varchar](1000) NOT NULL,
             [LeagueID] [varchar](1000) NOT NULL,
             [SeasonID] [varchar](1000) NOT NULL,
             * */
            String assignmentPolicy ="null";
            String scorePolicy ="null";
            if(((LeagueInSeason) object).getAssignmentPolicy() instanceof PlayOnceWithEachTeamPolicy){
                assignmentPolicy ="PlayOnceWithEachTeamPolicy";
            }
            else if(((LeagueInSeason) object).getAssignmentPolicy() instanceof PlayTwiceWithEachTeamPolicy){
                assignmentPolicy = "PlayTwiceWithEachTeamPolicy";
            }

            if(((LeagueInSeason) object).getScorePolicy() instanceof StandardScorePolicy){
                scorePolicy ="StandardScorePolicy";
            }
            else if(((LeagueInSeason) object).getScorePolicy() instanceof CupScorePolicy){
                scorePolicy = "CupScorePolicy";
            }
            ans1 = dataAccess.updateCellValue("LeaguesInSeasons","AssignmentPolicy", ((LeagueInSeason) object).getId() ,assignmentPolicy);
            ans2 = dataAccess.updateCellValue("LeaguesInSeasons","ScorePolicy", ((LeagueInSeason) object).getId(),scorePolicy);
            ans3 = dataAccess.updateCellValue("LeaguesInSeasons","GamesIDs", ((LeagueInSeason) object).getId(), listToString(((LeagueInSeason) object).getGamesId()));
            ans4 = dataAccess.updateCellValue("LeaguesInSeasons","RefereesIDs" ,((LeagueInSeason) object).getId(),getRefereesId(((LeagueInSeason) object).getReferees()));

            ans5 = dataAccess.updateCellValue("LeaguesInSeasons","TeamsIDs" ,((LeagueInSeason) object).getId(),listOfTeamsToStringIDs(((LeagueInSeason) object).getTeams()));

            //    ans5 = dataAccess.updateCellValue("LeaguesInSeasons","TeamsIDs" ,((LeagueInSeason) object).getId(),((LeagueInSeason) object).getTeamsId());

            ans6 = dataAccess.updateCellValue("LeaguesInSeasons","RegistrationFee" ,((LeagueInSeason) object).getId(),""+((LeagueInSeason) object).getRegistrationFee());
            ans7 = dataAccess.updateCellValue("LeaguesInSeasons","Records" ,((LeagueInSeason) object).getId(), createScoreTable(((LeagueInSeason) object).getScoreTable()));
            ans8 = dataAccess.updateCellValue("LeaguesInSeasons","LeagueID" ,((LeagueInSeason) object).getId(), ((LeagueInSeason) object).getLeague().getId() );
            ans9 = dataAccess.updateCellValue("LeaguesInSeasons","SeasonID" ,((LeagueInSeason) object).getId(), ((LeagueInSeason) object).getSeason().getId() );


            return ans1 && ans2 && ans3 && ans4  && ans5 && ans6  && ans7 && ans8  && ans9;
        }
        else if(object instanceof PersonalPage){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             [OwnerID] [char](30) NOT NULL unique,
             [PageData] [varchar](max) ,
             [Followers] [varchar](max) ,
             * */

            ans1 = dataAccess.updateCellValue("PersonalPages" ,"OwnerID" , ((PersonalPage) object).getId() ,((PersonalPage) object).getUser().getID() );
            ans2 = dataAccess.updateCellValue("PersonalPages" ,"PageData" ,((PersonalPage) object).getId() , ((PersonalPage) object).getData() );
            ans3 = dataAccess.updateCellValue("PersonalPages" ,"Followers" , ((PersonalPage) object).getId(), getFollowersIds(((PersonalPage) object).getFollowers()));

            return ans1 && ans2 && ans3 && ans4 ;
        }
        else  if(object instanceof Player){
            boolean ans1=true,ans2=true,ans3=true,ans4=true ,ans5=true;
            /**
             [Birthdate] [date] NOT NULL,
             [Teams] [varchar](255) NOT NULL,
             [RoleInTeam] [varchar](255) NOT NULL,
             [isActive] [bit] NOT NULL,
             [Price] [real] NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Players" ,"Birthdate" ,((Player) object).getID() , dateToString(((Player) object).getBirthDate()));
            ans2 = dataAccess.updateCellValue("Players" ,"Teams" , ((Player) object).getID(), listOfTeamsToStringIDs(((Player) object).getTeams()));
            ans3 = dataAccess.updateCellValue("Players" ,"RoleInTeam" , ((Player) object).getID(), ((Player) object).getRole());
            ans4 = dataAccess.updateCellValue("Players" ,"isActive" , ((Player) object).getID(), ""+((Player) object).isActive());
            ans5 = dataAccess.updateCellValue("Players" ,"Price" , ((Player) object).getID(), ""+((Player) object).getPrice());

            return ans1 && ans2 && ans3 && ans4 && ans5 ;
        }
        else if(object instanceof Referee){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             [Training] [varchar](50) NOT NULL,
             [Games] [varchar](255) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Referees" ,"Training" , ((Referee) object).getID() ,((Referee) object).getTraining() );
            ans2 = dataAccess.updateCellValue("Referees" ,"Games" , ((Referee) object).getID(), getGamesId(((Referee) object).viewGames()));

            return ans1 && ans2 && ans3 && ans4 ;
        }
        else if(object instanceof Season){
            boolean ans1=true,ans2=true,ans3=true,ans4=true;
            /**
             [SeasonYear] [int] NOT NULL,
             [StartDate] [date] NOT NULL,
             [LeaguesInSeasonsIDs] [varchar](255) NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Seasons" ,"SeasonYear" , ((Season) object).getId() ,""+((Season) object).getYear() );
            ans2 = dataAccess.updateCellValue("Seasons" ,"StartDate" , ((Season) object).getId(), dateToString(((Season) object).getStartDate() ));
            ans3 = dataAccess.updateCellValue("Seasons" ,"LeaguesInSeasonsIDs" , ((Season) object).getId(), leagueInSeasonToStringIDs(((Season) object).getLeagueInSeasons()) );
            //ans3 = dataAccess.updateCellValue("Seasons" ,"LeaguesIDs" , ((Season) object).getId(), ((Season) object).getLeaguesId() );

            return ans1 && ans2 && ans3 && ans4 ;
        }
        else if(object instanceof Team){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,
                    ans5=true,ans6=true,ans7=true,ans8=true,
                    ans9=true,ans10=true ,ans11=true, ans12=true,
                    ans13=true,ans14=true ,ans15=true;
            /**
             *
             [Name] [varchar](50) NOT NULL,
             [Wins] [int] NOT NULL,
             [Losses] [int] NOT NULL,
             [Draws] [int] NOT NULL,
             [PersonalPageID] [char] (30) NOT NULL,
             [TeamOwners] [varchar](255) NOT NULL ,
             [TeamManagers] [varchar](255) NOT NULL,
             [Players] [varchar](255) NOT NULL,
             [Coaches] [varchar](255) NOT NULL,
             [Budget] [real] NOT NULL,
             [GamesIDs] [varchar] (255) NOT NULL,
             [Fields] [varchar] (255) NOT NULL,
             [LeaguesInSeasons] [varchar] (255) NOT NULL,
             [isActive] [bit] NOT NULL,
             [isPermanentlyClosed] [bit] NOT NULL,
             * */

            ans1 = dataAccess.updateCellValue("Teams","Name", ((Team) object).getID(), ((Team) object).getName());
            ans2 = dataAccess.updateCellValue("Teams","Wins", ((Team) object).getID(), ""+((Team) object).getWins());
            ans3 = dataAccess.updateCellValue("Teams","Losses", ((Team) object).getID(), ""+((Team) object).getLosses());
            ans4 = dataAccess.updateCellValue("Teams","Draws", ((Team) object).getID(), ""+((Team) object).getDraws());
            ans5 = dataAccess.updateCellValue("Teams","PersonalPageID", ((Team) object).getID(),((Team) object).getPage().getId() );
            ans6 = dataAccess.updateCellValue("Teams","TeamOwners", ((Team) object).getID(), getUserId(((Team) object).getTeamOwners()));
            ans7 = dataAccess.updateCellValue("Teams","TeamManagers", ((Team) object).getID(), getUserId(((Team) object).getTeamManagers()));
            ans8 = dataAccess.updateCellValue("Teams","Players", ((Team) object).getID(), getUserId(((Team) object).getPlayers())  );
            ans9 = dataAccess.updateCellValue("Teams","Coaches",((Team) object).getID() , getUserId(((Team) object).getCoaches()));
            ans10 = dataAccess.updateCellValue("Teams","Budget" , ((Team) object).getID(),
                    ""+((Team) object).getBudget().getIncome()+","+((Team) object).getBudget().getExpanses());
            ans11 = dataAccess.updateCellValue("Teams","GamesIDs" , ((Team) object).getID(), listToString(((Team) object).getGamesId()));
            ans12 = dataAccess.updateCellValue("Teams","Fields" , ((Team) object).getID(), getFieldsIds(((Team) object).getFields()) );
            ans13 = dataAccess.updateCellValue("Teams","LeaguesInSeasons" , ((Team) object).getID(), listToString(((Team) object).getLeaguesInSeason()));
            ans14 = dataAccess.updateCellValue("Teams","isActive" ,((Team) object).getID() , ""+((Team) object).isActive());
            ans15 = dataAccess.updateCellValue("Teams","isPermanentlyClosed" , ((Team) object).getID(), ""+((Team) object).isPermanentlyClosed());


            return ans1 && ans2 && ans3 && ans4  && ans5 &&
                    ans6 && ans7 && ans8 && ans9  && ans10 && ans11
                    && ans12 && ans13  && ans14 && ans15;
        }
        else if(object instanceof TeamManager){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true;
            /**
             [Teams] [varchar](255) NOT NULL,
             [isActive] [bit] NOT NULL,
             [Price] [real] NOT NULL,
             [ManageAssets] [bit] NOT NULL,
             [Finance] [bit] ,
             * */


            ans1 = dataAccess.updateCellValue("TeamManagers" ,"Teams" , ((TeamManager) object).getID() , listOfTeamsToStringIDs(((TeamManager) object).getTeams()));
            ans2 = dataAccess.updateCellValue("TeamManagers" ,"isActive" ,((TeamManager) object).getID() , ""+((TeamManager) object).isActive());
            ans3 = dataAccess.updateCellValue("TeamManagers" ,"Price" , ((TeamManager) object).getID(), ""+((TeamManager) object).getPrice());

            //ManageAssets and Finance are permissionManageAssets and permissionFinance
            ans4 = dataAccess.updateCellValue("TeamManagers" ,"ManageAssets" , ((TeamManager) object).getID(), ""+((TeamManager) object).isPermissionManageAssets());
            ans5 = dataAccess.updateCellValue("TeamManagers" ,"Finance" , ((TeamManager) object).getID(), ""+((TeamManager) object).isPermissionFinance());

            return ans1 && ans2 && ans3 && ans4 && ans5 ;
        }
        else if(object instanceof TeamOwner){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true;
            /**
             [Teams] [varchar](255) NOT NULL,
             [ClosedTeams] [varchar](255) ,
             [AppointedTeamOwners] [varchar] ,
             [AppointedTeamManagers] [varchar](255) ,
             [PersonalPageIDs] [varchar](255) ,
             * */

            ans1 = dataAccess.updateCellValue("TeamOwners" ,"Teams" , ((TeamOwner) object).getID(), listOfTeamsToStringIDs(((Manager) object).getTeamsToManage()));
            ans2 = dataAccess.updateCellValue("TeamOwners" ,"ClosedTeams" , ((TeamOwner) object).getID(), listOfTeamsToStringIDs(((TeamOwner) object).getClosedTeams()) );

            //HashMap for user and team, need to save them together
            ans3 = dataAccess.updateCellValue("TeamOwners" ,"AppointedTeamOwners" , ((TeamOwner) object).getID(), appointmentUsersIds(((TeamOwner) object).getAppointedTeamOwners()));
            ans4 = dataAccess.updateCellValue("TeamOwners" ,"AppointedTeamManagers" , ((TeamOwner) object).getID(), appointmentUsersIds(((TeamOwner) object).getAppointedTeamManagers()));
            ans5 = dataAccess.updateCellValue("TeamOwners" ,"PersonalPageIDs" , ((TeamOwner) object).getID(), personalPagesOfTeamOwner(((TeamOwner) object).getPersonalPages()));

            return ans1 && ans2 && ans3 && ans4 && ans5;
        }
        else if(object instanceof User){
            boolean ans1=true,ans2=true,ans3=true,ans4=true,ans5=true ,ans6=true;
            /**
             *
             [FirstName] [varchar](255) NOT NULL,
             [LastName] [varchar](255) NOT NULL,
             [Mail] [varchar](255) NOT NULL unique,
             [isActive] [bit] NOT NULL,
             [Roles] [varchar](255) ,
             [searchHistories] [varchar](1000) ,
             * */

            ans1 = dataAccess.updateCellValue("Users","FirstName",((User) object).getID() , ((User) object).getFirstName());
            ans2 = dataAccess.updateCellValue("Users","LastName", ((User) object).getID(),((User) object).getLastName());
            ans3 = dataAccess.updateCellValue("Users","Mail", ((User) object).getID(), ((User) object).getMail());
            ans4 = dataAccess.updateCellValue("Users","isActive" ,((User) object).getID(), ""+((User) object).isActive());
            ans5 = dataAccess.updateCellValue("Users","Roles" ,((User) object).getID(), listToString(((User) object).getStringRoles()));
            ans6 = dataAccess.updateCellValue("Users","searchHistories" ,((User) object).getID(), listToString(((User) object).getSearchHistory()));


            return ans1 && ans2 && ans3 && ans4  && ans5 && ans6  ;
        }
        return false;

    }


    private static String getFansForAlerts(HashMap<Fan, Boolean> fansForAlerts) {
        String listOfStrings="";

        for (HashMap.Entry<Fan, Boolean> entry : fansForAlerts.entrySet()) {

            Fan fan = entry.getKey();
            Boolean bool = entry.getValue();
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings + fan.getID() +":"+ bool;
            }else {
                listOfStrings = listOfStrings + "," + fan.getID() +":"+ bool;
            }
        }
        return listOfStrings;
    }

    private static String appointmentUsersIds(HashMap<User, Team> appointedTeamOwners) {
        String listOfStrings="";

        for (HashMap.Entry<User, Team> entry : appointedTeamOwners.entrySet()) {

            User user = entry.getKey();
            Team team = entry.getValue();
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings + user.getID() +":"+ team.getID();
            }else {
                listOfStrings = listOfStrings + "," + user.getID() +":"+ team.getID();
            }
        }
        return listOfStrings;
    }

    private static String personalPagesOfTeamOwner(HashMap<Team, PersonalPage> personalPageAndTeam) {
        String listOfStrings="";

        for (HashMap.Entry<Team, PersonalPage> entry : personalPageAndTeam.entrySet()) {

            Team team = entry.getKey();
            PersonalPage personalPage = entry.getValue();
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings + team.getID() +":"+ personalPage.getId();
            }else {
                listOfStrings = listOfStrings + "," + team.getID() +":"+ personalPage.getId();
            }
        }
        return listOfStrings;
    }

    private static String getLeagueInSeasonIds(List<LeagueInSeason> leaguesInSeason) {
        String listOfStringsID="";
        for (LeagueInSeason league:leaguesInSeason) {
            if(listOfStringsID.equals("")){
                listOfStringsID= listOfStringsID +league.getId();
            }else {
                listOfStringsID = listOfStringsID + "," + league.getId();
            }
        }
        return listOfStringsID;
    }

    private static String getFieldsIds(List<Field> fields) {
        String listOfStringsID="";
        for (Field field:fields) {
            if(listOfStringsID.equals("")){
                listOfStringsID= listOfStringsID +field.getID();
            }else {
                listOfStringsID = listOfStringsID + "," + field.getID();
            }
        }
        return listOfStringsID;
    }

    private static String getUserId(List<User> users) {
        String listOfStringsID="";
        for (User user:users) {
            if(listOfStringsID.equals("")){
                listOfStringsID= listOfStringsID +user.getID();
            }else {
                listOfStringsID = listOfStringsID + "," + user.getID();
            }
        }
        return listOfStringsID;
    }

    private static String createScoreTable(Queue<ScoreTableRecord> scoreTable) {
        String listOfStrings="";
        for (ScoreTableRecord scoreTableRecord : scoreTable) {
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings +scoreTableRecord.getTeam().getID() +":"+scoreTableRecord.getTotalScore();
            }else {
                listOfStrings = listOfStrings + "," +scoreTableRecord.getTeam().getID() +":"+scoreTableRecord.getTotalScore();
            }
        }
        return listOfStrings;

    }

    private static String getComplaintsId(List<String> complaints){
        String listOfId = "";
        for (String comp: complaints) {
            if(listOfId.equals("")){
                listOfId = listOfId + comp;
            }
            else {
                listOfId = listOfId + ","+comp;
            }
        }
        return listOfId;
    }

    private static String getFollowPagesId(List<PersonalPage> followPages){
        String listOfId = "";
        for (PersonalPage page: followPages) {
            if(listOfId.equals("")){
                listOfId = listOfId + page.getId();
            }
            else {
                listOfId = listOfId + ","+page.getId();
            }
        }
        return listOfId;
    }


    private static String getEventsId(List<Event> events){
        String listOfId = "";
        for (Event event: events) {
            if(listOfId.equals("")){
                listOfId = listOfId + event.getId();
            }
            else {
                listOfId = listOfId + ","+event.getId();
            }
        }
        return listOfId;
    }


    private static String getFollowersIds(List<Fan> followers){
        String listOfId = "";
        for (Fan fan: followers) {
            if(listOfId.equals("")){
                listOfId = listOfId + fan.getID();
            }
            else {
                listOfId = listOfId + ","+fan.getID();
            }
        }
        return listOfId;
    }

    private static String getGamesId(List<Game> games){
        String listOfId = "";
        for (Game game: games) {
            if(listOfId.equals("")){
                listOfId = listOfId + game.getId();
            }
            else {
                listOfId = listOfId + ","+game.getId();
            }
        }
        return listOfId;
    }

    private static String getRefereesId(List<Referee> referees){
        String listOfId = "";
        for (Referee referee: referees) {
            if(listOfId.equals("")){
                listOfId = listOfId+referee.getID();
            }
            else {
                listOfId = listOfId + ","+referee.getID();
            }
        }
        return listOfId;
    }

    private static String getGamesId(HashSet<String> games){
        String listOfId = "";
        for (String gameId: games) {
            if(listOfId.equals("")){
                listOfId = listOfId+gameId;
            }
            else {
                listOfId = listOfId + ","+gameId;
            }
        }
        return listOfId;
    }

    private static String getLeaguesId(List<LeagueInSeason> leagueInSeasons){
        String listOfId = "";
        for (LeagueInSeason leagueInSeason: leagueInSeasons) {
            if(listOfId.equals("")){
                listOfId = listOfId + leagueInSeason.getLeague().getId();
            }
            else {
                listOfId = listOfId + ","+leagueInSeason.getLeague().getId();
            }
        }
        return listOfId;
    }

    private static Queue<ScoreTableRecord> getScoreTableQueue(String scoreTable) {

        Queue<ScoreTableRecord> scoreTableQueue = new LinkedList<>();
        List<String> temp;
        //scoreTable= teamID1:score,teamID2:score,..
        /**
         * after split =>
         * scoreTableList.get(0) = teamID1:score
         * scoreTableList.get(1) = teamID2:score
         * ...
         */
        List<String> scoreTableList = split(scoreTable);
        for(String s : scoreTableList){
            if(!s.equals("")) {
                temp = splitHashMap(s);
                Team team = getTeam(temp.get(0));
                int score = Integer.parseInt(temp.get(1));
                if (team != null) {
                    ScoreTableRecord scoreTableRecord = new ScoreTableRecord(team, score);
                    ((LinkedList<ScoreTableRecord>) scoreTableQueue).add(scoreTableRecord);
                }
            }
        }
        return scoreTableQueue;
    }

    private static String getSeasonsFromLeagueInSeasons(List<LeagueInSeason> leagueInSeasons) {
        String listOfStrings="";
        for (LeagueInSeason leagueInSeason : leagueInSeasons) {
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings +leagueInSeason.getSeason().getId();
            }else {
                listOfStrings = listOfStrings + "," + leagueInSeason.getSeason().getId();
            }
        }
        return listOfStrings;
    }


    private static String listToString(Collection objects){
        String listOfStrings="";
        for (Object object:objects) {
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings +object;
            }else {
                listOfStrings = listOfStrings + "," + object;
            }
        }
        return listOfStrings;

    }

    private static String listOfTeamsToStringIDs(Collection<Team> teams){
        String listOfStringsID="";
        for (Team team:teams) {
            if(listOfStringsID.equals("")){
                listOfStringsID= listOfStringsID +team.getID();
            }else {
                listOfStringsID = listOfStringsID + "," + team.getID();
            }
        }
        return listOfStringsID;

    }

    private static String leagueInSeasonToStringIDs(List<LeagueInSeason> leagueInSeasons){
        String listOfStringsID="";
        for (LeagueInSeason leagueInSeason:leagueInSeasons) {
            if(listOfStringsID.equals("")){
                listOfStringsID= listOfStringsID +leagueInSeason.getId();
            }else {
                listOfStringsID = listOfStringsID + "," + leagueInSeason.getId();
            }
        }
        return listOfStringsID;

    }
   /* private static String listToStringForEventsID(List<Event> events){
        String listOfStrings="";
        for (Event event:events) {
            if(listOfStrings.equals("")){
                listOfStrings= listOfStrings +event.getId();
            }else {
                listOfStrings = listOfStrings + "," + event.getId();
            }
        }
        return listOfStrings;

    }*/





    public static PartOfATeam getAsset(String type,String assetId){
        //choose the asset by type
        //switch case: look for the id in: Player, coach, TM

        switch (type){
            case "Player":
                return getPlayer(assetId);
            case "Coach":
                return getCoach(assetId);
            case "TeamManager":
                return getTeamManager(assetId);
            case "Field":
                return getField(assetId);
        }

        return null;

    }


    public static User getUserByMail(String mail , String password){
        try {
            List<User> users = getAllActiveUsers();
            String userPassword = "";
            for (User user : users) {
                if (user.isActive()) {
                    if (user.getMail().equals(mail)) {
                        userPassword = dataAccess.getCellValue("Passwords", "Password", user.getID());
                        if (userPassword.equals(sha1(password))) {
                            return user;
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /*
    this function perform a authentication check for username an password
    returns true if this is the correct credentials and false otherwise
     */

    public static boolean changePassword(String mail,String oldPassword , String newPassword){
       /* if(authenticationCheck(mail , oldPassword)) {
            if (mailsAndPasswords.containsKey(mail)) {
                String encryptedPassword = encrypt(newPassword);
                mailsAndPasswords.replace(mail, encryptedPassword);
                return true;
            }
        }
        return false;*/

        return true;
    }

    public static String removeUser(String userId) {
        User user = getUser(userId);
        user.deactivate();
        List<Role> userRoles = user.getRoles();
        for(Role role : userRoles){
            if(role!=null){
                switch (role.myRole()){
                    case "Coach":
                        ((Coach)role).deactivate();
                        updateObject(role);
                        break;
                    case "Player":
                        ((Player)role).deactivate();
                        updateObject(role);
                        break;
                    case "TeamManager":
                        ((TeamManager)role).deactivate();
                        updateObject(role);
                        break;
                }
            }
        }
        updateObject(user);

        return user.getMail();

    }

    public static void removeFromTables(String id){
        dataAccess.deleteIdFromAllTables(id);
    }

    public static void removeRowFromTable(String tableName , String id){
        dataAccess.deleteRow(tableName, id);
    }

    public static void removeField(String assetId) {
        Field field = getField(assetId);

        if(field != null){
            field.deactivate();
            updateObject(field);
        }
    }

    public static void removeAsset(String type ,String assetId) {

        switch (type){
            case "Player":
                Player player = getPlayer(assetId);
                if(player != null) {
                    player.deactivate();
                    updateObject(player);
                }
                break;
            case "Coach":
                Coach coach = getCoach(assetId);
                if(coach != null){
                    coach.deactivate();
                    updateObject(coach);
                }
                break;
            case "TeamManager":
                TeamManager teamManager = getTeamManager(assetId);
                if(teamManager != null){
                    teamManager.deactivate();
                    updateObject(teamManager);
                }
                break;
            case "Field":
                Field field = getField(assetId);
                if(field != null){
                    field.deactivate();
                    updateObject(field);
                }
                break;
        }

    }

    public static List<Object> searchObject(String searchWord){

        //search page

        return null;
    }

    public static List<Team> getOpenTeams() {
        List<Team> openTeams = new LinkedList<>();
        List<Team> teams = getAllTeams();

        for(Team team : teams){
            if(team.isActive() && !team.isPermanentlyClosed())
                openTeams.add(team);
        }
        return openTeams;
    }

    public static List<Team> getCloseTeams() {
        List<Team> closeTeams = new LinkedList<>();
        List<Team> teams = getAllTeams();

        for(Team team : teams){
            if(team.isActive() && !team.isPermanentlyClosed())
                closeTeams.add(team);
        }
        return closeTeams;
    }

    public static Object createObject(String type,List<String> object){
        User user;
        switch (type){
            case "Complaint":
                Complaint complaint = new Complaint(object.get(0),
                        stringToDateJAVA(object.get(1)) ,stringToBoolean(object.get(2)),
                        object.get(3),getFan(object.get(4)), object.get(5));
                return complaint;
            case "Event":
                Event event = new Event(object.get(0) ,object.get(1) , stringToDateJAVA(object.get(2)),
                        Double.parseDouble(object.get(3)) ,object.get(4));
                return event;
            case "EventReport":
                EventReport eventReport = new EventReport(object.get(0),listOfEvents(object.get(1)));
                return eventReport;
            case "Field":
                Field field = new Field(object.get(0) , object.get(1) , object.get(2),
                        Integer.parseInt(object.get(3)) ,teamHashSet(object.get(4)) ,stringToBoolean(object.get(5)) ,
                        Double.parseDouble(object.get(6)));
                return field;
            case "Game":
                Game game = new Game(object.get(0),stringToDateJAVA(object.get(2)),
                        Integer.parseInt(object.get(3)) , Integer.parseInt(object.get(4)),
                        getField(object.get(5)) ,getReferee(object.get(6))
                        ,listOfReferees(object.get(7)),getTeam(object.get(8)), getTeam(object.get(9)),
                        getFansForAlertsHashMap(object.get(10)) ,getEventReport(object.get(11)) ,
                        getLeagueInSeason(object.get(12)));
                return game;
            case "League":
                League league = new League(object.get(0) , object.get(1) ,getEnumLevelLeague(object.get(2)),
                        listOfLeagueInSeason(object.get(3)));
                return league;
            case "LeagueInSeason":
                LeagueInSeason leagueInSeason = new LeagueInSeason(object.get(0) ,getGameAssignmentPolicy(object.get(1)),
                        getScorePolicy(object.get(2)) ,split(object.get(3)) ,listOfReferees(object.get(4)),
                        listOfTeams(object.get(5)) , Double.parseDouble(object.get(6)) ,getScoreTableQueue(object.get(7)),
                        createLeague(object.get(8)),createSeason(object.get(9)));
                return leagueInSeason;
            case "PersonalPage":
                user=createUser(object.get(1));
                PersonalPage personalPage = new PersonalPage(object.get(0),
                        user,object.get(2),listOfFans(object.get(3)));
                return personalPage;
            case "Season":
                Season season = new Season(object.get(0),Integer.parseInt(object.get(1)),
                        stringToDateJAVA(object.get(2)),
                        listOfLeagueInSeason(object.get(3)));
                return season;
            case "Team":
                List<String> budgetList = split(object.get(10));
                Budget budget = new Budget(Double.parseDouble(budgetList.get(0)),
                        Double.parseDouble(budgetList.get(1)));
                Team team = new Team(object.get(0) , object.get(1) ,
                        Integer.parseInt(object.get(2)),Integer.parseInt(object.get(3)),
                        Integer.parseInt(object.get(4)),getPersonalPage(object.get(5)),
                        listOfUsers(object.get(6)) ,listOfUsers(object.get(7)),
                        listOfUsers(object.get(8)),listOfUsers(object.get(9)),budget,
                        split(object.get(11)),listOfFields(object.get(12)),
                        split(object.get(13)),stringToBoolean(object.get(14)),
                        stringToBoolean(object.get(15)));
                return team;
            case "User":
                user = new User(object.get(0) ,object.get(1),object.get(2),object.get(3),
                        stringToBoolean(object.get(4)), split(object.get(6)));
                user.addRoles(createListOfRoles(object.get(5), object.get(0)));
                return user;

        }
        return null;

    }

    public static Object createRole(String type,List<String> object) {
        switch (type){
            case "Admin":
                Admin admin = new Admin(object.get(0));
                return admin;
            case "Coach":
                Coach coach = new Coach(object.get(0), getEnumTraining(object.get(1)) ,getEnumRole(object.get(2)),
                        teamHashSet(object.get(3)), stringToBoolean(object.get(4)),Double.parseDouble(object.get(5)));
                return coach;
            case "Fan":
                Fan fan = new Fan(object.get(0) , object.get(1),object.get(2) ,listOfPersonalPage(object.get(3)), split(object.get(4)));
                return fan;
            case "Player":
                Player player = new Player(object.get(0), stringToDateJAVA(object.get(1)),
                        teamHashSet(object.get(2)),object.get(3),
                        stringToBoolean(object.get(4)),Double.parseDouble(object.get(5)));
                return player;
            case "Referee":
                Referee referee = new Referee(object.get(0), object.get(1),
                        hashSetOfGames(object.get(2)));
                return referee;
            case "TeamManager":
                TeamManager teamManager = new TeamManager(object.get(0),
                        teamHashSet(object.get(1)),stringToBoolean(object.get(2)),
                        Double.parseDouble(object.get(3)),
                        stringToBoolean(object.get(4)),stringToBoolean(object.get(5)));
                return teamManager;
            case "TeamOwner":
                TeamOwner teamOwner = new TeamOwner(object.get(0),listOfTeams(object.get(1)),
                        listOfTeams(object.get(2)),hashMapUserAndTeam(object.get(3)),
                        hashMapUserAndTeam(object.get(4)),
                        hashMapTeamAndPersonalPage(object.get(5)));
                return teamOwner;
            case "UnionRepresentative":
                UnionRepresentative union = new UnionRepresentative(object.get(0));
                return union;
        }
        return null;
    }

    public static java.util.Date stringToDateJAVA(String st) {

        String[] dateTimeSplit = st.split(" ");
        int year, month, day;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        Calendar cal = Calendar.getInstance();

        String[] split = dateTimeSplit[0].split("-");

        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        if (dateTimeSplit.length > 1)
        {
            String time = dateTimeSplit[1];
            String[] timeSplit = time.split(":");
            hours = Integer.parseInt(timeSplit[0]);
            minutes = Integer.parseInt(timeSplit[1]);
            seconds = Integer.parseInt(timeSplit[2].substring(0, timeSplit[2].indexOf(".")));
        }

        cal.set(year, month, day, hours, minutes, seconds);
        return new java.util.Date(cal.getTimeInMillis());
    }

    private static HashMap<Team, PersonalPage> hashMapTeamAndPersonalPage(String personalPageForTeam) {
        HashMap<Team ,PersonalPage> hashMapPersonalPageForTeam = new HashMap<>();
        List<String> temp;
        //appointmentTeamUser= userId:teamId,userId:teamId,..

        /**
         * after split =>
         * appointments.get(0) = userId:teamId
         * appointments.get(1) = userId:teamId
         * ...
         */
        List<String> teamspersonalPage = split(personalPageForTeam);

        for(String s : teamspersonalPage){
            if(!s.equals("")) {
                temp = splitHashMap(s);
                Team team = getTeam(temp.get(0));
                PersonalPage page = getPersonalPage(temp.get(1));
                hashMapPersonalPageForTeam.put(team, page);
            }
        }
        return hashMapPersonalPageForTeam;
    }

    private static HashMap<User, Team> hashMapUserAndTeam(String appointmentTeamUser)
    {
        HashMap<User ,Team> hashMapAppointmentTeamUser = new HashMap<>();
        List<String> temp;
        //appointmentTeamUser= userId:teamId,userId:teamId,..

        /**
         * after split =>
         * appointments.get(0) = userId:teamId
         * appointments.get(1) = userId:teamId
         * ...
         */
        List<String> appointments = split(appointmentTeamUser);

        for(String s : appointments){
            if(!s.equals("")) {
                temp = splitHashMap(s);
                User user = getUser(temp.get(0));
                Team team = getTeam(temp.get(1));
                hashMapAppointmentTeamUser.put(user, team);
            }
        }
        return hashMapAppointmentTeamUser;
    }


    private static boolean stringToBoolean(String s) {
        if(s.equals("1") || s.equals("true")){
            return true;
        }else{
            return false;
        }

    }


    private static List<Role> createListOfRoles(String roles ,String userId) {
        List<String> listOfRoles = split(roles);
        List<Role> allRoles = new LinkedList<>();

        for(String s : listOfRoles){
            switch (s){
                case "Admin":
                    allRoles.add(getAdmin(userId));
                    break;
                case "Coach":
                    allRoles.add(getCoach(userId));
                    break;
                case "Fan":
                    allRoles.add(getFan(userId));
                    break;
                case "Player":
                    allRoles.add(getPlayer(userId));
                    break;
                case "Referee":
                    allRoles.add(getReferee(userId));
                    break;
                case "TeamManager":
                    allRoles.add(getTeamManager(userId));
                    break;
                case "TeamOwner":
                    allRoles.add(getTeamOwner(userId));
                    break;
                case "UnionRepresentative":
                    allRoles.add(getUnionRepresentative(userId));
                    break;
            }
        }
        return allRoles;

    }

    private static List<LeagueInSeason> listOfLeagueInSeason(String leagueInSeasonIds) {
        List<String> leagueInSeason = split(leagueInSeasonIds);
        List<LeagueInSeason> allLeagueInSeason = new LinkedList<>();

        for (String leagueId : leagueInSeason){
            LeagueInSeason league = getLeagueInSeason(leagueId);
            if(league !=null)
                allLeagueInSeason.add(league);
        }
        return allLeagueInSeason;
    }

    private static List<Referee> listOfReferees(String referees){
        List<String> listOfReferees = split(referees);
        List<Referee> allReferees = new LinkedList<>();

        for (String refereeId : listOfReferees){
            Referee referee = getReferee(refereeId);
            if(referee != null) {
                allReferees.add(referee);
            }
        }

        return allReferees;
    }

    private static List<Game> listOfGames(String games){
        List<String> listOfGames = split(games);
        List<Game> allGames = new LinkedList<>();

        for (String gameId : listOfGames){
            Game game = getGame(gameId);
            if(game != null) {
                allGames.add(game);
            }
        }

        return allGames;
    }

    private static HashSet<String> hashSetOfGames(String games){
        List<String> listOfGames = split(games);
        HashSet<String> allGames = new HashSet<>();

        for (String gameId : listOfGames){
            if(gameId != null) {
                allGames.add(gameId);
            }
        }
        return allGames;
    }

    private static List<Fan> listOfFans(String fans){
        List<String> listOfFans = split(fans);
        List<Fan> allFans = new LinkedList<>();

        for (String fanId : listOfFans){
            Fan fan = getFan(fanId);
            if(fan != null) {
                allFans.add(fan);
            }
        }

        return allFans;
    }

    private static List<User> listOfUsers(String users){
        List<String> listOfUsers = split(users);
        List<User> allUsers = new LinkedList<>();

        for (String userId : listOfUsers){
            User user = createUser(userId);
            if(user != null) {
                allUsers.add(user);
            }
        }

        return allUsers;
    }

    private static HashSet<Team> teamHashSet(String teams){
        List<String> listOfTeams = split(teams);
        HashSet<Team> allTeams = new HashSet<>();

        for(String teamId : listOfTeams){
            Team team = getTeam(teamId);
            if(team != null){
                allTeams.add(team);
            }
        }
        return allTeams;
    }

    private static List<Team> listOfTeams(String teams){
        List<String> listOfTeams = split(teams);
        List<Team> allTeams = new LinkedList<>();

        for(String teamId : listOfTeams){
            Team team = getTeam(teamId);
            if(team != null){
                allTeams.add(team);
            }
        }
        return allTeams;
    }

    private static List<Event> listOfEvents(String events){
        List<String> listOfEvents = split(events);
        List<Event> allEvents = new LinkedList<>();

        for (String eventId : listOfEvents){
            Event event = getEvent(eventId);
            if(event != null){
                allEvents.add(event);
            }
        }

        return allEvents;

    }

    private static List<Field> listOfFields(String fields){
        List<String> listOfFields = split(fields);
        List<Field> allFields = new LinkedList<>();

        for (String fieldId : listOfFields){
            Field field = getField(fieldId);
            if(field != null){
                allFields.add(field);
            }
        }

        return allFields;

    }


    private static List<PersonalPage> listOfPersonalPage(String personalPage){
        List<String> listOfPersonalPage = split(personalPage);
        List<PersonalPage> allPersonalPage = new LinkedList<>();

        for (String pageId : listOfPersonalPage){
            PersonalPage page = getPersonalPage(pageId);
            if (page != null){
                allPersonalPage.add(page);
            }
        }

        return allPersonalPage;

    }

    private static List<Complaint> listOfComplaints(String complaintId){
        List<String> listOfComplaint = split(complaintId);
        List<Complaint> allComplaints = new LinkedList<>();

        for (String s : listOfComplaint){
            Complaint complaint = getComplaints(complaintId);
            if (complaint != null){
                allComplaints.add(complaint);
            }
        }

        return allComplaints;
    }

    private static List<String> split(String roles){
        List<String> listOfRoles = new LinkedList<>();
        String[] split = roles.split(",");

        for (String s: split){
            listOfRoles.add(s);
        }
        return listOfRoles;
    }

    private static List<String> splitHashMap(String fansForAlerts){
        List<String> listOfFansForAlerts = new LinkedList<>();
        String[] split = fansForAlerts.split(":");

        for (String s: split){
            listOfFansForAlerts.add(s);
        }
        return listOfFansForAlerts;
    }

    private static HashMap<Fan ,Boolean> getFansForAlertsHashMap(String fansForAlerts) {

        HashMap<Fan ,Boolean> hashMapFansForAlerts = new HashMap<>();
        List<String> temp;
        //fansForAlerts= fanId1:boolean,fanId2:boolean,..

        /**
         * after split =>
         * fansAndAlerts.get(0) = fanId1:boolean
         * fansAndAlerts.get(0) = fanId2:boolean
         * ...
         */
        List<String> fansAndAlerts = split(fansForAlerts);

        for(String s : fansAndAlerts){
            if(!s.equals("")) {
            temp = splitHashMap(s);
            Fan fan = getFan(temp.get(0));
            Boolean bool = stringToBoolean(temp.get(1));
            hashMapFansForAlerts.put(fan, bool);
            }
        }
        return hashMapFansForAlerts;
    }


    private static Coach.TrainingCoach getEnumTraining(String enumTraining) {
        switch (enumTraining){
            case "IFA_C":
                return Coach.TrainingCoach.IFA_C;
            case "UEFA_A":
                return Coach.TrainingCoach.UEFA_A;
            case "UEFA_B":
                return Coach.TrainingCoach.UEFA_B;
            case "UEFA_PRO":
                return Coach.TrainingCoach.UEFA_PRO;
        }
        return Coach.TrainingCoach.IFA_C;
    }

    private static Coach.RoleCoach getEnumRole(String enumRoleCoach) {
        switch (enumRoleCoach){
            case "main":
                return Coach.RoleCoach.main;
            case "assistantCoach":
                return Coach.RoleCoach.assistantCoach;
            case "fitness":
                return Coach.RoleCoach.fitness;
            case "goalkeeperCoach":
                return Coach.RoleCoach.goalkeeperCoach;
        }
        return Coach.RoleCoach.assistantCoach;
    }

    private static League.LevelLeague getEnumLevelLeague(String enumLevelLeague) {
        switch (enumLevelLeague){
            case "level1":
                return League.LevelLeague.level1;
            case "level2":
                return League.LevelLeague.level2;
            case "level3":
                return League.LevelLeague.level3;
            case "level4":
                return League.LevelLeague.level4;
            case "level5":
                return League.LevelLeague.level5;
        }
        return League.LevelLeague.level1;
    }


    /*******************GET OBJECT FROM DATABASE START****************/

    public static ScorePolicy getScorePolicy(String scorePolicyName) {

        if(scorePolicyName.equals("StandardScorePolicy")){
            return new StandardScorePolicy();
        }else{
            return new CupScorePolicy();
        }
    }

    public static GameAssignmentPolicy getGameAssignmentPolicy(String gameAssignmentPolicy) {

        if(gameAssignmentPolicy.equals("PlayOnceWithEachTeamPolicy")){
            return new PlayOnceWithEachTeamPolicy();
        }else{
            return new PlayTwiceWithEachTeamPolicy();
        }
    }

    public static LeagueInSeason getLeagueInSeason(String leagueInSeasonId){
        //return leaguesInSeasons.get(leagueInSeasonId);
        if(dataAccess.isIDExists("LeaguesInSeasons" ,leagueInSeasonId)) {
            List<String> leagueInSeason;
            leagueInSeason = dataAccess.getAllCellValues("LeaguesInSeasons", leagueInSeasonId);
            return (LeagueInSeason) createObject("LeagueInSeason", leagueInSeason);
        }
        return null;
    }

    public static Field getField(String fieldId) {
        if(dataAccess.isIDExists("Fields" ,fieldId)) {
            List<String> field;
            field = dataAccess.getAllCellValues("Fields", fieldId);
            return (Field) createObject("Field", field);
        }
        return null;

    }
    public static Complaint getComplaints(String complaintId) {
        if(dataAccess.isIDExists("Complaints" ,complaintId)) {
            List<String> complaint;
            complaint = dataAccess.getAllCellValues("Complaints" ,complaintId );
            return (Complaint) createObject("Complaint" , complaint);
        }
        return null;
    }

    public static PersonalPage getPersonalPage(String pageId){
        if(dataAccess.isIDExists("PersonalPages" ,pageId)) {
            List<String> personalPage;
            personalPage = dataAccess.getAllCellValues("PersonalPages" ,pageId );
            return (PersonalPage) createObject("PersonalPage" , personalPage);
        }
        return null;
    }

    public static Event getEvent(String eventId){
        if(dataAccess.isIDExists("Events" ,eventId)) {
            List<String> event;
            event = dataAccess.getAllCellValues("Events" ,eventId);
            return (Event) createObject("Event" , event);
        }
        return null;
    }

    public static EventReport getEventReport(String eventReportId){
        if(dataAccess.isIDExists("EventReports" ,eventReportId)) {
            List<String> eventReport;
            eventReport = dataAccess.getAllCellValues("EventReports" ,eventReportId);
            return (EventReport) createObject("EventReport" , eventReport);
        }
        return null;
    }

    public static Admin getAdmin(String userId){
        if(dataAccess.isIDExists("Admins" ,userId)) {
            List<String> admin;
            admin = dataAccess.getAllCellValues("Admins", userId);
            return (Admin) createRole("Admin", admin);
        }
        return null;

    }

    public static Game getGame(String gameId){
        if(dataAccess.isIDExists("Games" ,gameId)) {
            List<String> game;
            game = dataAccess.getAllCellValues("Games", gameId);
            return (Game) createObject("Game", game);
        }
        return null;
    }

    public static League getLeague(String leagueId){
        if(dataAccess.isIDExists("Leagues" ,leagueId)) {
            List<String> league;
            league = dataAccess.getAllCellValues("Leagues", leagueId);
            return (League) createObject("League", league);
        }
        return null;
    }

    public static Season getSeason(String seasonId){
        if(dataAccess.isIDExists("Seasons" ,seasonId)) {
            List<String> season;
            season = dataAccess.getAllCellValues("Seasons", seasonId);
            return (Season) createObject("Season", season);
        }
        return null;
    }


    public static Team getTeam(String teamId){

        if(dataAccess.isIDExists("Teams" ,teamId)) {
            List<String> team;
            team = dataAccess.getAllCellValues("Teams", teamId);
            return (Team) createObject("Team", team);
        }
        return null;
    }

    public static Coach getCoach(String userId){
        if(dataAccess.isIDExists("Coaches" ,userId)) {
            List<String> coach;
            coach = dataAccess.getAllCellValues("Coaches", userId);
            return (Coach) createRole("Coach", coach);
        }
        return null;
    }

    public static Fan getFan(String userId){
        if(dataAccess.isIDExists("Fans" ,userId)) {
            List<String> fan;
            fan = dataAccess.getAllCellValues("Fans", userId);
            return (Fan) createRole("Fan", fan);
        }
        return null;
    }

    public static Player getPlayer(String userId){
        if(dataAccess.isIDExists("Players" ,userId)) {
            List<String> player;
            player = dataAccess.getAllCellValues("Players", userId);
            return (Player) createRole("Player", player);
        }
        return null;
    }

    public static Referee getReferee(String userId){
        if(dataAccess.isIDExists("Referees" ,userId)) {
            List<String> referee;
            referee = dataAccess.getAllCellValues("Referees", userId);
            return (Referee) createRole("Referee", referee);
        }
        return null;
    }

    public static TeamManager getTeamManager(String userId){
        if(dataAccess.isIDExists("TeamManagers" ,userId)) {
            List<String> teamManager;
            teamManager = dataAccess.getAllCellValues("TeamManagers", userId);
            return (TeamManager) createRole("TeamManager", teamManager);
        }
        return null;
    }

    public static TeamOwner getTeamOwner(String userId){
        if(dataAccess.isIDExists("TeamOwners" ,userId)) {
            List<String> teamOwner;
            teamOwner = dataAccess.getAllCellValues("TeamOwners", userId);
            return (TeamOwner) createRole("TeamOwner", teamOwner);
        }
        return null;
    }

    public static UnionRepresentative getUnionRepresentative(String userId){
        if(dataAccess.isIDExists("UnionRepresentatives" ,userId)) {
            List<String> unionRepresentative;
            unionRepresentative = dataAccess.getAllCellValues("UnionRepresentatives", userId);
            return (UnionRepresentative) createRole("UnionRepresentative", unionRepresentative);
        }
        return null;
    }

    private static User createUser(String userId){
        if(dataAccess.isIDExists("Users" ,userId)) {
            List<String> userString;
            userString = dataAccess.getAllCellValues("Users", userId);
            User user = new User(userString.get(0),userString.get(1),userString.get(2),
                    userString.get(3),stringToBoolean(userString.get(4)), split(userString.get(6)));
            return user;
        }
        return null;
    }

    private static League createLeague(String leagueId){
        if(dataAccess.isIDExists("Leagues" ,leagueId)) {
            List<String> leagueString;
            leagueString = dataAccess.getAllCellValues("Leagues", leagueId);
            League league = new League(leagueString.get(0),leagueString.get(1),
                    getEnumLevelLeague(leagueString.get(2)));
            return league;
        }
        return null;
    }

    private static Season createSeason(String seasonId){
        if(dataAccess.isIDExists("Seasons" ,seasonId)) {
            List<String> seasonString;
            seasonString = dataAccess.getAllCellValues("Seasons", seasonId);
            Season season = new Season(seasonString.get(0),Integer.parseInt(seasonString.get(1)),
                    stringToDateJAVA(seasonString.get(2)));
            return season;
        }
        return null;
    }

   /* private static LeagueInSeason createLeagueInSeason(String leagueInSeasonId){
        if(dataAccess.isIDExists("LeagueInSeasons" ,leagueInSeasonId)) {
            List<String> leagueString;
            leagueString = dataAccess.getAllCellValues("LeagueInSeasons", leagueInSeasonId);
            LeagueInSeason leagueInSeason = new LeagueInSeason(leagueString.get(0),
                    getGameAssignmentPolicy(leagueString.get(1)),getScorePolicy(leagueString.get(2)),
                    listOfGames(leagueString.get(3)),listOfReferees(leagueString.get(4)),
                    listOfTeams(leagueString.get(5)),Double.parseDouble(leagueString.get(6)),
                    getScoreTableQueue(leagueString.get(7)),createLeague(leagueString.get(8)));
            return leagueInSeason;
        }
        return null;
    }*/

    public static User getUser(String userId) {
        if(dataAccess.isIDExists("Users" ,userId)) {
            List<String> user;
            user = dataAccess.getAllCellValues("Users", userId);
            return (User) createObject("User", user);
        }
        return null;
    }

    /*******************GET OBJECT FROM DATABASE END****************/



    /*******************MESSAGES STSRT****************/


    public static void addNotificationToUser(String userId , String message){
        String oldMessages = "";
        //if userId exsist
        if(dataAccess.isIDExists("OfflineUsersNotifications",userId)){
            oldMessages = getNotifications(userId);
            dataAccess.updateCellValue("OfflineUsersNotifications" ,"Notifications" ,
                    userId ,oldMessages +"," +message);
        }else{
            dataAccess.addCell("OfflineUsersNotifications" ,
                    userId , message);
        }

    }

    private static String getNotifications(String userId){
        if(dataAccess.isIDExists("OfflineUsersNotifications",userId)){
            return dataAccess.getCellValue("OfflineUsersNotifications" ,"Notifications" ,userId);
        }
        return "";
    }

    public static List<String> getAllNotifications(String userId){
        if(dataAccess.isIDExists("OfflineUsersNotifications",userId)) {
            List<String> allMessages = split(getNotifications(userId));
            dataAccess.updateCellValue("OfflineUsersNotifications", "Notifications", userId, "");
            return allMessages;
        }
        return null;

    }


/******************* MESSAGES END ****************/


    /*************** GET ALL FUNCTION BEGIN ******************/


    public static List<Team> getAllTeams() {
        List<String> teams;
        List<Team> allTeams = new LinkedList<>();

        teams = dataAccess.getAllFieldValues("Teams", "ID");

        for(String string : teams){
            allTeams.add(getTeam(string));
        }
        return allTeams;
    }

    public static List<User> getAllUsers(){

        List<String> users;
        List<User> allUsers = new LinkedList<>();
        users = dataAccess.getAllFieldValues("Users", "ID");

        for(String userString : users){
            allUsers.add(getUser(userString));
        }

        return allUsers;
    }

    private static List<User> getAllActiveUsers() {
        List<User> activeUsers = new LinkedList<>();
        List<User> allUsers = getAllUsers();

        for(User user : allUsers){
            if(user.isActive())
                activeUsers.add(user);
        }
        return activeUsers;
    }


    public static List<TeamOwner> getAllTeamOwners(){

        List<User> allUsers = new LinkedList<>();
        List<TeamOwner> allTeamOwners = new LinkedList<>();
        List<String> teamOwners = dataAccess.getAllFieldValues("TeamOwners", "ID");

        for(String userString : teamOwners){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allTeamOwners.add((TeamOwner) checkUser.checkUserRole("TeamOwner"));
            }
        }
        return allTeamOwners;
    }

    public static List<Fan> getAllFans(){

        List<User> allUsers = new LinkedList<>();
        List<Fan> allFans = new LinkedList<>();
        List<String> fans = dataAccess.getAllFieldValues("Fans", "ID");

        for(String userString : fans){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }


        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allFans.add((Fan) checkUser.checkUserRole("Fan"));
            }
        }
        return allFans;
    }

    public static List<UnionRepresentative> getAllUnions(){

        List<User> allUsers = new LinkedList<>();
        List<UnionRepresentative> allUnions = new LinkedList<>();
        List<String> unions = dataAccess.getAllFieldValues("UnionRepresentatives", "ID");

        for(String userString : unions){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allUnions.add((UnionRepresentative) checkUser.checkUserRole("UnionRepresentative"));
            }
        }
        return allUnions;
    }

    public static List<League> getLeagues(){
        List<String> objects;
        List<League> allObjects = new LinkedList<>();
        objects = dataAccess.getAllFieldValues("Leagues", "ID");

        for(String object : objects){
            List<String> league = dataAccess.getAllCellValues("Leagues", object);
            allObjects.add((League) createObject("League" , league));
        }

        return allObjects;
    }

    public static List<LeagueInSeason> getAllLeaguesInSeasons(){
        List<String> objects;
        List<LeagueInSeason> allObjects = new LinkedList<>();
        objects = dataAccess.getAllFieldValues("LeaguesInSeasons", "ID");

        for(String object : objects){
            allObjects.add(getLeagueInSeason(object));
        }

        return allObjects;
    }

    public static List<Season> getSeasons() {
        List<String> objects;
        List<Season> allObjects = new LinkedList<>();

        objects = dataAccess.getAllFieldValues("Seasons", "ID");

        for(String object : objects){
            List<String> season = dataAccess.getAllCellValues("Seasons", object);
            allObjects.add((Season) createObject("Season" , season));
        }

        return allObjects;
    }

    public static List<String> getAllPastGames() {
        List<String> games;
        Date currentDate = new Date();
        List<String> pastGames = new LinkedList<>();

        games = dataAccess.getAllFieldValues("Games", "ID");

        for(String gameString : games){
            List<String> game = dataAccess.getAllCellValues("Games", gameString);
            if(currentDate.after(stringToDateJAVA(game.get(2)))){
                pastGames.add(game.get(0)+","+game.get(1)+","+game.get(2));
            }
        }
        return pastGames;
    }

    public static List<String> getAllFutureGames() {
        List<String> games;
        Date currentDate = new Date();
        List<String> futureGames = new LinkedList<>();

        games = dataAccess.getAllFieldValues("Games", "ID");

        for(String gameString : games){
            List<String> game = dataAccess.getAllCellValues("Games", gameString);
            if(currentDate.before(stringToDateJAVA(game.get(2)))){
                if(game.get(9).equals(""))
                    futureGames.add(game.get(0)+","+game.get(1)+","+game.get(2).substring(0, game.get(2).indexOf(".")));
                else
                    futureGames.add(game.get(0)+","+game.get(1)+","+game.get(2).substring(0, game.get(2).indexOf("."))+","+game.get(10));
            }
        }
        return futureGames;
    }

    public static List<Complaint> getAllActiveComplaints() {
        List<Complaint> complaints = getAllComplaints();
        List<Complaint> activeComplaints = new LinkedList<>();

        for(Complaint complaint : complaints){
            if(complaint.getIsActive()){
                activeComplaints.add(complaint);
            }
        }
        return activeComplaints;
    }

    public static List<Complaint> getAllComplaints() {
        List<String> complaints;
        List<Complaint> allComplaints = new LinkedList<>();

        complaints = dataAccess.getAllFieldValues("Complaints", "ID");

        for(String string : complaints){
            List<String> complaint = dataAccess.getAllCellValues("Complaints", string);
            allComplaints.add((Complaint) createObject("Complaint" , complaint));
        }

        return allComplaints;
    }

    public static String getAllOccurringGame() {
        List<String> games = dataAccess.getAllFieldValues("Games", "ID");

        for(String gameString : games){
            List<String> game = dataAccess.getAllCellValues("Games", gameString);
            String currentDate = dateToString(new Date());
            currentDate = currentDate.substring(0, currentDate.indexOf(" "));
            String gameDate = dateToString(stringToDateJAVA(game.get(2)));
            gameDate = gameDate.substring(0, gameDate.indexOf(" "));
            if(currentDate.equals(gameDate)){
                   return game.get(0)+","+game.get(1)+","+game.get(2).substring(0, game.get(2).indexOf("."));
            }
        }
        return null;
    }

    public static List<Game> getAllGames() {
        List<String> games;
        List<Game> allGames = new LinkedList<>();

        games = dataAccess.getAllFieldValues("Games", "ID");

        for(String string : games){
            allGames.add(getGame(string));
        }

        return allGames;
    }

    public static List<GameAssignmentPolicy> getAllAssignmentsPolicies() {
        List<GameAssignmentPolicy> allGameAssignments = new LinkedList<>();
        GameAssignmentPolicy playOnce = new PlayOnceWithEachTeamPolicy();
        GameAssignmentPolicy playTwice = new PlayTwiceWithEachTeamPolicy();

        allGameAssignments.add(playOnce);
        allGameAssignments.add(playTwice);

        return allGameAssignments;
    }

    public static List<ScorePolicy> getAllScorePolicies() {
        List<ScorePolicy> allScorePolicies = new LinkedList<>();

        ScorePolicy cup = new CupScorePolicy();
        ScorePolicy standard = new StandardScorePolicy();

        allScorePolicies.add(cup);
        allScorePolicies.add(standard);

        return allScorePolicies;
    }


    public static List<TeamManager> getAllTeamManagers() {
        List<User> allUsers = new LinkedList<>();
        List<TeamManager> allTeamManagers = new LinkedList<>();

        List<String> teamManagers = dataAccess.getAllFieldValues("TeamManagers", "ID");

        for(String userString : teamManagers){
            List<String> user = dataAccess.getAllCellValues("Users",userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allTeamManagers.add((TeamManager) checkUser.checkUserRole("TeamManager"));
            }
        }

        return allTeamManagers;
    }

    public static List<PersonalPage> getAllPages() {
        List<String> personalPages;
        List<PersonalPage> allPersonalPages = new LinkedList<>();

        personalPages = dataAccess.getAllFieldValues("PersonalPages", "ID");

        for(String string : personalPages){
            List<String> personalPage = dataAccess.getAllCellValues("PersonalPages", string);
            allPersonalPages.add((PersonalPage) createObject("PersonalPage" , personalPage));
        }

        return allPersonalPages;
    }

    public static List<Player> getAllPlayers() {
        List<User> allUsers = new LinkedList<>();
        List<Player> allPlayers = new LinkedList<>();

        List<String> players = dataAccess.getAllFieldValues("Players", "ID");

        for(String userString : players){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User)createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allPlayers.add((Player)checkUser.checkUserRole("Player"));
            }
        }
        return allPlayers;
    }
    public static List<Admin> getAllAdmins() {
        List<User> allUsers = new LinkedList<>();
        List<Admin> allAdmins = new LinkedList<>();

        List<String> admins = dataAccess.getAllFieldValues("Admins", "ID");

        for(String userString : admins){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allAdmins.add((Admin)checkUser.checkUserRole("Admin"));
            }
        }
        return allAdmins;
    }

    public static List<Coach> getAllCoaches() {
        List<User> allUsers = new LinkedList<>();
        List<Coach> allCoaches = new LinkedList<>();

        List<String> coaches = dataAccess.getAllFieldValues("Coaches", "ID");

        for(String userString : coaches){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allCoaches.add((Coach) checkUser.checkUserRole("Coach"));
            }
        }

        return allCoaches;
    }

    public static List<Field> getAllFields() {
        List<String> fields;
        List<Field> allFields = new LinkedList<>();

        fields = dataAccess.getAllFieldValues("Fields", "ID");

        for(String string : fields){
            List<String> field = dataAccess.getAllCellValues("Fields", string);
            allFields.add((Field) createObject("Field" , field));
        }

        return allFields;
    }

    public static List<Field> getAllActiveFields(){
        List<Field> allFields = getAllFields();
        List<Field> activeFields = new LinkedList<>();
        for(Field field : allFields) {
            if (field.isActive())
                activeFields.add(field);
        }
        return activeFields;
    }

    public static List<Referee> getAllReferees() {
        List<User> allUsers = new LinkedList<>();
        List<Referee> allReferees = new LinkedList<>();

        List<String> referees = dataAccess.getAllFieldValues("Referees", "ID");

        for(String userString : referees){
            List<String> user = dataAccess.getAllCellValues("Users", userString);
            allUsers.add((User) createObject("User" , user));
        }

        for(User checkUser : allUsers){
            if(checkUser.isActive()){
                allReferees.add((Referee) checkUser.checkUserRole("Referee"));
            }
        }
        return allReferees;
    }

    /**GET ALL FUNCTION END*/

    /**ADD FUNCTION BEGIN*/
    public static boolean addReferee( Referee referee){

        if(!dataAccess.isIDExists("Referees" ,referee.getID())) {
            dataAccess.addCell("Referees", referee.getID(),
                    referee.getTraining(), getGamesId(referee.viewGames()));
            return true;
        }
        return false;
    }

    public static boolean addLeague(League league) {

        if(!dataAccess.isIDExists("Leagues" ,league.getId() )) {

            dataAccess.addCell("Leagues", league.getId(), league.getName(),
                    league.getLevel(), listToString(league.getLeagueInSeasons()));
            return true;
        }
        return false;
    }

    public static boolean addSeason(Season season) {

        List<String> years = dataAccess.getAllFieldValues("Seasons", "SeasonYear");
        for(String year : years){
            if(year.equals(""+season.getYear()))
                return false;
        }
        if(!dataAccess.isIDExists("Seasons" ,season.getId())){
            dataAccess.addCell("Seasons", season.getId(), "" + season.getYear(),
                    dateToString(season.getStartDate()), getLeagueInSeasonIds(season.getLeagueInSeasons()));
            return true;
        }
        return false;
    }


    public static boolean addTeam(Team team){

        if(!dataAccess.isIDExists("Teams" ,team.getID() )) {

            dataAccess.addCell("Teams",team.getID(), team.getName(),
                    "" + team.getWins(), "" + team.getLosses(),
                    "" + team.getDraws(), team.getPage().getId(),
                    getUserId(team.getTeamOwners()),
                    getUserId(team.getTeamManagers()),
                    getUserId(team.getPlayers()),
                    getUserId(team.getCoaches()),
                    "" + team.getBudget().getIncome()+","+team.getBudget().getExpanses()
                    , listToString(team.getGamesId())
                    , getFieldsIds(team.getFields())
                    , listToString(team.getLeaguesInSeason())
                    , "" + team.isActive(),
                    "" + team.isPermanentlyClosed());
            return true;
        }
        return false;

    }

    public static boolean addLeagueInSeason(LeagueInSeason leagueInSeason){
        if(!dataAccess.isIDExists("LeaguesInSeasons" ,leagueInSeason.getId())) {
            dataAccess.addCell("LeaguesInSeasons", leagueInSeason.getId(), leagueInSeason.getAssignmentPolicy().getName(),
                    leagueInSeason.getScorePolicy().getName(), listToString(leagueInSeason.getGamesId()), getRefereesId(leagueInSeason.getReferees()),
                    listOfTeamsToStringIDs(leagueInSeason.getTeams()), "" + leagueInSeason.getRegistrationFee(),
                    createScoreTable(leagueInSeason.getScoreTable()),
                    leagueInSeason.getLeague().getId(),
                    leagueInSeason.getSeason().getId());
            return true;
        }
        return false;
    }

    public static boolean addComplaint(Complaint complaint){

        if(!dataAccess.isIDExists("Complaints" , complaint.getId())) {
            dataAccess.addCell("Complaints", complaint.getId(), dateToString(complaint.getDate()),
                    "" + complaint.getIsActive(), complaint.getDescription(), complaint.getFanComplained().getID(), complaint.getResponse());
            return true;
        }
        return false;
    }


    public static boolean addGame(Game game){

        if(!dataAccess.isIDExists("Games",game.getId())) {
            dataAccess.addCell("Games", game.getId(),game.getName(),
                    dateToString(game.getDate()),
                    "" + game.hostScore(), "" + game.guestScore(), game.getField().getID(),
                    game.getMainReferee().getID(), game.getSideRefereesId(),
                    game.getHostTeam().getID(), game.getGuestTeam().getID(),
                    game.getAlertsFansId(), game.getEventReport().getId(), game.getLeague().getId());

            addTeam(game.getHostTeam());
            addTeam(game.getGuestTeam());
            return true;
        }
        return false;

    }

    public static boolean addPersonalPage(PersonalPage page){

        if(!dataAccess.isIDExists("PersonalPages", page.getId())){
            dataAccess.addCell("PersonalPages",page.getId(),page.getUser().getID(),
                    page.getData(),getFollowersIds(page.getFollowers()));
            return true;
        }
        return false;
    }

    public static boolean addUser(String password, User user){
        //add to User SQL Tables
        //and his Role to the correct table: player to Players

        boolean flag = false;

        if(!dataAccess.isMailExists(user.getMail())){

            //users table
            dataAccess.addCell("Users" ,user.getID(),
                    user.getFirstName(),user.getLastName(),
                    user.getMail(),""+user.isActive(),listToString(user.getStringRoles()),
                    listToString(user.getSearchHistory()));

            try {
                //passwords table
                String encryptPassword = sha1(password);
                dataAccess.addCell("Passwords", user.getID(), encryptPassword);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            flag = true;
            //every role table
            List<Role> userRoles = user.getRoles();

            for (Role role : userRoles){
                switch (role.myRole()){
                    case "Admin":
                        dataAccess.addCell("Admins",user.getID());
                        flag=true;
                        break;
                    case "Coach":
                        addAsset((Coach)role);
                        flag=true;
                        break;
                    case "Fan":
                        addFan((Fan)role);
                        flag=true;
                        break;
                    case "Player":
                        addAsset((Player)role);
                        flag=true;
                        break;
                    case "Referee":
                        addReferee((Referee)role);
                        flag=true;
                        break;
                    case "TeamManager":
                        addTeamManager((TeamManager)role);
                        flag=true;
                        break;
                    case "TeamOwner":
                        addTeamOwner((TeamOwner) role);
                        flag=true;
                        break;
                    case "UnionRepresentative":
                        dataAccess.addCell("UnionRepresentatives", user.getID());
                        flag=true;
                        break;
                }
            }
        }

        return flag;
    }

    public static boolean addAsset(PartOfATeam asset){

        if(asset instanceof Field){
            return addField((Field) asset);
        }
        else if(asset instanceof Player){
            return addPlayer((Player)asset);
        }
        else if(asset instanceof Coach){
            return addCoach((Coach)asset);
        }
        else if(asset instanceof TeamManager){
            return addTeamManager((TeamManager)asset);
        }
        return false;

    }

    public static boolean addField(Field field){

        if(!dataAccess.isIDExists("Fields", field.getID())){
            dataAccess.addCell("Fields" ,field.getID(),field.getLocation(),field.getName(),
                    ""+field.getCapacity(),listOfTeamsToStringIDs(field.getTeams()),
                    ""+field.isActive(),""+field.getPrice() );

            return true;
        }
        return false;
    }


    public static boolean addTeamOwner(TeamOwner teamOwner){

        if(!dataAccess.isIDExists("TeamOwners", teamOwner.getID())){
            dataAccess.addCell("TeamOwners", teamOwner.getID(),
                    listOfTeamsToStringIDs(teamOwner.getTeamsToManage()),
                    listOfTeamsToStringIDs(teamOwner.getClosedTeams()),
                    appointmentUsersIds(teamOwner.getAppointedTeamOwners()),
                    appointmentUsersIds(teamOwner.getAppointedTeamManagers()),personalPagesOfTeamOwner(teamOwner.getPersonalPages()));
            return true;
        }
        return false;
    }

    public static boolean addPlayer(Player player){

        if(!dataAccess.isIDExists("Players", player.getID())){
            dataAccess.addCell("Players",player.getID(),dateToString(player.getBirthDate()),
                    listOfTeamsToStringIDs(player.getTeams()) , player.getRole(),
                    ""+player.isActive() , ""+player.getPrice());
            return true;
        }
        return false;
    }

    public static boolean addTeamManager(TeamManager teamManager){

        if(!dataAccess.isIDExists("TeamManagers", teamManager.getID())){
            dataAccess.addCell("TeamManagers",teamManager.getID(),
                    listOfTeamsToStringIDs(teamManager.getTeams()),
                    ""+teamManager.isActive(),""+teamManager.getPrice()
                    , ""+teamManager.isPermissionManageAssets() ,""+ teamManager.isPermissionFinance());
            return true;
        }
        return false;
    }

    public static boolean addCoach(Coach coach){

        if(!dataAccess.isIDExists("Coaches", coach.getID())){
            dataAccess.addCell("Coaches",coach.getID(),coach.getTraining(),
                    coach.getRoleInTeam(), listOfTeamsToStringIDs(coach.getTeams()),
                    ""+coach.isActive() , ""+coach.getPrice());
            return true;
        }
        return false;
    }

    public static boolean addFan(Fan fan){

        if(!dataAccess.isIDExists("Fans", fan.getID())){
            dataAccess.addCell("Fans",fan.getID(),
                    fan.getAddress() , fan.getPhone() , getFollowPagesId(fan.getFollowPages()),
                    getComplaintsId(fan.getComplaintsId()));
            return true;
        }
        return false;
    }

    public static boolean addAdmin(Admin admin){

        if(!dataAccess.isIDExists("Admins", admin.getID())){
            dataAccess.addCell("Admins",admin.getID());
            return true;
        }
        return false;
    }

    public static boolean addEventReport(EventReport eventReport) {
        if(!dataAccess.isIDExists("EventReports", eventReport.getId())){
            dataAccess.addCell("EventReports",eventReport.getId(), getEventsId(eventReport.getEvents()));
            return true;
        }
        return false;
    }

    public static boolean addEvent(Event event) {
        if(!dataAccess.isIDExists("Events", event.getId())){
            dataAccess.addCell("Events",event.getId(), ""+event.getType(), dateToString(event.getDate()), ""+event.getMinuteInGame(), event.getDescription());
            return true;
        }
        return false;
    }


    /**ADD FUNCTION END*/

}



