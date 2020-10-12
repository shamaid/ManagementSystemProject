package Logger;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ErrorsLogger
{
    private final Logger errorsLogger = LogManager.getLogger(ErrorsLogger.class);
    private static volatile ErrorsLogger errorsLog;
    private static final Object errorsMutex = new Object();

    private ErrorsLogger()
    {
        PropertyConfigurator.configure("log4jErrors.properties");
    }

    public static ErrorsLogger getInstance() {
        ErrorsLogger result = errorsLog;
        if (result == null) {
            synchronized (errorsMutex) {
                result = errorsLog;
                if (result == null)
                    errorsLog = result = new ErrorsLogger();
            }
        }
        return result;
    }

    public void log(String s)
    {
        errorsLogger.info(s);
    }

    public List<String> getErrorsLog()
    {
        List<String> lines = new LinkedList<String>();
        try
        {
            lines = Files.readAllLines(Paths.get("./logs/Errors.log"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return lines;
    }
}
