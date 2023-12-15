package Logger;

import java.io.FileWriter;
import java.io.IOException;


/**
 * This class implements the ILogger interface to save the program logs to a persistence storage
 * which in this case is a .txt file
 */
public class Logger implements ILogger
{
    /**
     * This method writes the log information to a file or prints an error if the log information
     * could not be written to a file
     * @param logInfo to be written to a file
     */
    @Override
    public void log(String logInfo)
    {
       String logFilePath="programLogs.txt";
       try(FileWriter fileWriter=new FileWriter(logFilePath,true))
       {
           fileWriter.write(logInfo);
       }
       catch (IOException e)
       {
           System.out.println(e.getMessage());
       }
    }
}
