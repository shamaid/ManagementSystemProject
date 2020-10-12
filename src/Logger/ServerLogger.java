package Logger;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ServerLogger
{
    private final Logger serverLogger = LogManager.getLogger(ServerLogger.class);
    private static volatile ServerLogger serverLog;
    private static final Object serverMutex = new Object();

    private ServerLogger()
    {
        PropertyConfigurator.configure("log4jServer.properties");
    }

    public static ServerLogger getInstance() {
        ServerLogger result = serverLog;
        if (result == null) {
            synchronized (serverMutex) {
                result = serverLog;
                if (result == null)
                    serverLog = result = new ServerLogger();
            }
        }
        return result;
    }

    public void log(String s)
    {
        serverLogger.info(s);
    }

    public List<String> getServerLog()
    {
        List<String> lines = new LinkedList<String>();
        try
        {
            lines = Files.readAllLines(Paths.get("./logs/Server.log"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return lines;
    }
}