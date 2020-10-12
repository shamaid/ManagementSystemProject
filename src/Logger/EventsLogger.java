package Logger;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class EventsLogger
{
    private final Logger eventsLogger = LogManager.getLogger(EventsLogger.class);
    private static volatile EventsLogger eventsLog;
    private static final Object eventsMutex = new Object();

    private EventsLogger()
    {
        PropertyConfigurator.configure("log4jEvents.properties");
    }

    public static EventsLogger getInstance() {
        EventsLogger result = eventsLog;
        if (result == null) {
            synchronized (eventsMutex) {
                result = eventsLog;
                if (result == null)
                    eventsLog = result = new EventsLogger();
            }
        }
        return result;
    }

    public void log(String userid, String s)
    {
        eventsLogger.info(userid + "\t" + "Operation: " + s);
    }

    public List<String> getEventsLog()
    {
        List<String> lines = new LinkedList<String>();
        try
        {
            lines = Files.readAllLines(Paths.get("./logs/Events.log"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return lines;
    }
}