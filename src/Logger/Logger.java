package Logger;


import java.util.List;

public class Logger {

    private static boolean useLogs = false;

    public static void logEvent(String userid, String s)
    {
        if (useLogs)
            EventsLogger.getInstance().log(userid, s);
    }

    public static void logError(String s)
    {
        if (useLogs)
            ErrorsLogger.getInstance().log(s);
    }

    public static void logServer(String s)
    {
        if (useLogs)
            ServerLogger.getInstance().log(s);
    }

    public static List<String> getEventsLog()
    {
        return ServerLogger.getInstance().getServerLog();
    }

    public static List<String> getErrorsLog()
    {
        return ErrorsLogger.getInstance().getErrorsLog();
    }

    public static List<String> getServerLog()
    {
        return ServerLogger.getInstance().getServerLog();
    }

}
