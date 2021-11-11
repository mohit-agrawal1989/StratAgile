package utility;

import org.testng.Reporter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExecutionLog {

    public static void log(String text)
    {
        ExecutionLog executionLog = new ExecutionLog();
        String dateTime = executionLog.getDate();
        String fileName = executionLog.getFileName();
        try
        {
            // Create file
            FileWriter fstream = new FileWriter(System.getProperty("user.dir")+"//ExecutionLog//"+fileName+".txt",true);

            BufferedWriter out = new BufferedWriter(fstream);
            text = dateTime +" [info]  "+ text;
            out.write(text);
            out.newLine();
            out.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("[Log] "+text);
        Reporter.log(""+text);
    }

    public static void logExceptionMessage(Exception e) throws IOException
    {
        ExecutionLog executionLog = new ExecutionLog();
        String dateTime = executionLog.getDate();
        ExecutionLog.log(dateTime +" [info]  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Error message >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String fileName = executionLog.getFileName();
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"//ExecutionLog//"+fileName+".txt", true));
            e.printStackTrace(pw);
            pw.close();
        }
        catch (FileNotFoundException e1)
        { e1.printStackTrace(); }
    }

    public static void logErrorMessage(Error e) throws IOException
    {
        ExecutionLog executionLog = new ExecutionLog();
        String dateTime = executionLog.getDate();
        ExecutionLog.log(dateTime +" [info]  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Error message >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String fileName = executionLog.getFileName();
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(new FileWriter(System.getProperty("user.dir")+"//ExecutionLog//"+fileName+".txt", true));
            e.printStackTrace(pw);
            pw.close();
        }
        catch (FileNotFoundException e1)
        { e1.printStackTrace(); }
    }

    public  String getFileName()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String fileName = "Report-"+dateFormat.format(cal.getTime());
        return fileName;
    }

    public String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dateTime =  dateFormat.format(cal.getTime());
        return dateTime;
    }

    /*
        this method will add log in file to mark starting of scenario
     */
    public static void logAddClass(String text)
    {
        ExecutionLog executionLog = new ExecutionLog();
        String dateTime = executionLog.getDate();
        String fileName = executionLog.getFileName();
        try
        {
            // Create file
            FileWriter fstream = new FileWriter(System.getProperty("user.dir")+"//ExecutionLog//"+fileName+".txt",true);
            BufferedWriter out = new BufferedWriter(fstream);
            text = dateTime +" [info]  "+ text;
            out.newLine();
            out.write("*****************************************************************************");
            out.newLine();
            out.write(text);
            out.newLine();
            out.write("*****************************************************************************");
            out.newLine();
            //Close the output stream
            out.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage()); }
    }

    /*
        this method will add log in file to mark ending of scenario
     */
    public static void logEndClass(String text)
    {
        ExecutionLog executionLog = new ExecutionLog();
        String dateTime = executionLog.getDate();
        String fileName = executionLog.getFileName();
        try
        {
            // Create file
            FileWriter fstream = new FileWriter(System.getProperty("user.dir")+"//ExecutionLog//"+fileName+".txt",true);
            BufferedWriter out = new BufferedWriter(fstream);
            text = dateTime +" [info]  "+ "-----End of scenario-----";
            out.newLine();
            out.write("*****************************************************************************");
            out.newLine();
            out.write(text);
            out.newLine();
            out.write("*****************************************************************************");
            //Close the output stream
            out.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
