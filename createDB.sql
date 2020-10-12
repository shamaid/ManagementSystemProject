USE [ManagementSystemDB]
GO

CREATE TABLE [Admins](
	[ID] [char](30)  Primary key,
);

CREATE TABLE [Passwords](
	[ID] [char](30)  Primary key,
	[Password] [varchar](max) NOT NULL,

);

CREATE TABLE [Users](
	[ID] [char](30)  Primary key,
	[FirstName]  [varchar](max) NOT NULL,
	[LastName]  [varchar](max) NOT NULL,
	[Mail]  [varchar](300) NOT NULL unique,
	[isActive] [bit] NOT NULL,
	[Roles]  [varchar](max) ,
	[searchHistories]  [varchar](max) ,
	--FOREIGN KEY (ID) REFERENCES [Passwords] (ID)
	--FOREIGN KEY([ID]) REFERENCES [dbo].[Passwords] ([ID]) ON UPDATE CASCADE ON DELETE CASCADE ,
);

CREATE TABLE [Referees](
	[ID] [char](30)  Primary key,
	[Training]  [varchar](max) NOT NULL,
	[Games]  [varchar](max) NOT NULL,
);

CREATE TABLE [UnionRepresentatives](
	[ID] [char](30)  Primary key,
);

CREATE TABLE [Coaches](
	[ID] [char](30)  Primary key,
	[Training]  [varchar](max) NOT NULL,
	[RoleInTeam]  [varchar](max) NOT NULL,
	[Teams]  [varchar](max) NOT NULL,
	[isActive] [bit] NOT NULL,
	[Price] [real] NOT NULL,
);

CREATE TABLE [Fans](
	[ID] [char](30)  Primary key,
	[Address]  [varchar](max) NOT NULL,
	[Phone]  [varchar](300) NOT NULL unique,
	[FollowedPagesIDs]  [varchar](max) ,
	[ComplaintsIDs]  [varchar](max) ,
	--[isMailAlerts] [bit] ,
);

CREATE TABLE [Fields](
	[ID] [char](30)  Primary key,
	[Location] [char](50) NOT NULL,
	[Name] [char](50) NOT NULL,
	[Capacity] [int] NOT NULL,
	[Teams]  [varchar](max) NOT NULL,
	[isActive] [bit] NOT NULL,
	[Price] [real] NOT NULL,
	--FOREIGN KEY([Teams]) REFERENCES [dbo].[Teams] ([ID]) ON UPDATE CASCADE ON DELETE CASCADE ,
);

CREATE TABLE [Players](
	[ID] [char](30)  Primary key,
	[Birthdate] [date] NOT NULL,
	[Teams]  [varchar](max) NOT NULL,
	[RoleInTeam]  [varchar](max) NOT NULL,
	[isActive] [bit] NOT NULL,
	[Price] [real] NOT NULL,
	--FOREIGN KEY([Teams]) REFERENCES [dbo].[Teams] ([ID]) ON UPDATE CASCADE ON DELETE CASCADE ,
);

CREATE TABLE [TeamManagers](
	[ID] [char](30)  Primary key,
	[Teams]  [varchar](max) NOT NULL,
	[isActive] [bit] NOT NULL,
	[Price] [real] NOT NULL,
	[ManageAssets] [bit] NOT NULL,
	[Finance] [bit] ,
);

CREATE TABLE [TeamOwners](
	[ID] [char](30)  Primary key,
	[Teams]  [varchar](max) NOT NULL,
	[ClosedTeams]  [varchar](max) ,
	[AppointedTeamOwners] [varchar](max) ,
	[AppointedTeamManagers]  [varchar](max) ,
	[PersonalPageIDs]  [varchar](max) ,
);

CREATE TABLE [PersonalPages](
	[ID] [char](30)  Primary key,
	[OwnerID] [char](30) NOT NULL unique,
	[PageData] [varchar](max) ,
	[Followers] [varchar](max) ,
);

CREATE TABLE [Teams](
	[ID] [char](30)  Primary key,
	[Name] [varchar](max) NOT NULL,
	[Wins] [int] NOT NULL,
	[Losses] [int] NOT NULL,
	[Draws] [int] NOT NULL,
	[PersonalPageID] [varchar] (max) NOT NULL,
	[TeamOwners] [varchar](max) NOT NULL ,
	[TeamManagers] [varchar](max) NOT NULL,
	[Players] [varchar](max) NOT NULL,
	[Coaches] [varchar](max) NOT NULL,
	[Budget] [varchar](max) NOT NULL,
	[GamesIDs] [varchar] (max) NOT NULL,
	[Fields] [varchar] (max) NOT NULL,
	[LeaguesInSeasons] [varchar] (max) NOT NULL,
	[isActive] [bit] NOT NULL,
	[isPermanentlyClosed] [bit] NOT NULL,
);

CREATE TABLE [Leagues](
	[ID] [char](30)  Primary key,
	[Name]  [varchar](max) NOT NULL,
	[LeagueLevel]  [varchar](max) NOT NULL,
	[LeaguesInSeasonsIDs]  [varchar](max) NOT NULL,
);

CREATE TABLE [Seasons](
	[ID] [char](30)  Primary key,
	[SeasonYear] [int] NOT NULL,
	[StartDate] [date] NOT NULL,
	[LeaguesInSeasonsIDs]  [varchar](max) NOT NULL,
);

CREATE TABLE [LeaguesInSeasons](
	[ID] [char](30)  Primary key,
	[AssignmentPolicy] [char](255) NOT NULL,
	[ScorePolicy] [char](255) NOT NULL,
	[GamesIDs]  [varchar](max) NOT NULL,
	[RefereesIDs]  [varchar](max) NOT NULL,
	[TeamsIDs]  [varchar](max) NOT NULL,
	[RegistrationFee] [real] NOT NULL,
	[Records]  [varchar](max) NOT NULL,
	[LeagueID]  [varchar](max) NOT NULL,
	[SeasonID]  [varchar](max) NOT NULL,
);

CREATE TABLE [Complaints](
	[ID] [char](30)  Primary key,
	[ComplaintDate] [date] NOT NULL,
	[isActive] [bit] NOT NULL,
	[Description] [varchar] (1000)NOT NULL ,
	[ComplainedFanID] [char](50) NOT NULL,
);

CREATE TABLE [Games](
	[ID] [char](30)  Primary key,
	[Name] [varchar](max) NOT NULL,
	[GameDate] [datetime] NOT NULL,
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
);

CREATE TABLE [EventReports](
	[ID] [char](30)  Primary key,
	--[GameID] [char](30) NOT NULL,
	[EventsIDs] [varchar](max) NOT NULL,
);

CREATE TABLE [OfflineUsersNotifications](
	[ID] [char](30)  Primary key,
	[Notifications] [varchar](max) NOT NULL,
);

CREATE TABLE [Events](
	[ID] [char](30)  Primary key,
	[EventType] [char](50) NOT NULL,
	[EventDate] [date] NOT NULL,
	[MinutesInGame] [real] NOT NULL,
	[Description] [varchar](max) NOT NULL,
);