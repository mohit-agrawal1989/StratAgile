import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ConsoleLogsValidationCheck extends ParentClass{
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    public static void consoleLogsValidation () throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("ConsoleErrorReport.txt", "UTF-8");
        driver.navigate().to("https://www.sc.com/sg/");
        new WebDriverWait(driver, 60).until(webDriver ->
                js.executeScript("return document.readyState").equals("complete"));
        List<WebElement> allLinks = driver.findElements(By.xpath("//a[@href]"));
        ExecutionLog.log("There are "+allLinks.size()+" hyperlinks");
            for(int i = 1; i <= allLinks.size(); i++){
            String url = driver.findElement(By.xpath("(//a[@href])["+i+"]")).getAttribute("href");
            String parentWindow  = driver.getWindowHandle();
            js.executeScript("window.open();");
            Set<String> handles  = driver.getWindowHandles();
            for(String childWindow  : handles){
                {
                    try
                    {
                        if(!childWindow.equals(parentWindow))
                        {
                            driver.switchTo().window(childWindow);
                            driver.get(url);
                            new WebDriverWait(driver, 60).until(webDriver ->
                                    js.executeScript("return document.readyState").equals("complete"));
                            ExecutionLog.log("Navigated to the new URL: "+url);
                            try{
                                Logs logs = driver.manage().logs();
                                LogEntries logEntries = logs.get(LogType.BROWSER);
                                List<LogEntry> errorLogs = logEntries.filter(Level.SEVERE);

                                if (errorLogs.size() != 0) {
                                    for (LogEntry logEntry: logEntries) {
                                        ExecutionLog.log("Found error in logs for this url: " + logEntry.getMessage());
                                        writer.println("Found error in logs for this url: " + logEntry.getMessage());
                                    }
                                    writer.println();
                                    ExecutionLog.log(errorLogs.size() + " Console error found");
                                }
                                driver.switchTo().defaultContent();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            driver.close(); //closing child window
                            driver.switchTo().window(parentWindow); //cntrl to parent window
                        }
                    }
                    catch (Exception e)
                    {
                        if(!childWindow.equals(parentWindow))
                        {
                            driver.close(); //closing child window
                        }
                        driver.switchTo().window(parentWindow); //cntrl to parent window
                        e.printStackTrace();
                    }
                }
            }
        }
        writer.close();
    }
}
