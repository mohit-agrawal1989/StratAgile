import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ConsoleLogsValidationCheck extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    private static PrintWriter writer;

    public static void consoleLogsValidation(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "Console Errors");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(", ");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    System.out.println("URL to be hit: " + countryURLToNavigate[x]);
                    String newUrl = countryURLToNavigate[x];
                    driver.navigate().to(newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    List<WebElement> allLinks = driver.findElements(By.xpath("//a[@href]"));
                    ExecutionLog.log("There are " + allLinks.size() + " hyperlinks");
                    String url = "";
                    for (int i = 0; i <= 5; i++) {
                        try {
                            if(i == 0){
                                url = newUrl;
                            }else{
                                url = driver.findElement(By.xpath("(//a[@href])[" + i + "]")).getAttribute("href");
                            }
                            String[] urlExtension = url.split("/");
                            System.out.println("URL Extension: " + urlExtension[urlExtension.length - 1]);
                            writer = new PrintWriter(directoryPath + "" + File.separator + "Console Errors" + "" + File.separator + "" + urlExtension[urlExtension.length - 1].replaceAll("/ " + File.separator + " : * ? \" < > |", "") + ".txt", "UTF-8");
                            String parentWindow = driver.getWindowHandle();
                            js.executeScript("window.open();");
                            Set<String> handles = driver.getWindowHandles();
                            for (String childWindow : handles) {
                                try {
                                    if (!childWindow.equals(parentWindow)) {
                                        driver.switchTo().window(childWindow);
                                        driver.get(url);
                                        new WebDriverWait(driver, 60).until(webDriver ->
                                                js.executeScript("return document.readyState").equals("complete"));
                                        ExecutionLog.log("Navigated to the new URL: " + url);
                                        try {
                                            Logs logs = driver.manage().logs();
                                            LogEntries logEntries = logs.get(LogType.BROWSER);
                                            List<LogEntry> errorLogs = logEntries.filter(Level.SEVERE);

                                            if (errorLogs.size() != 0) {
                                                for (LogEntry logEntry : logEntries) {
                                                    ExecutionLog.log("Found error in logs for this url: " + logEntry.getMessage());
                                                    writer.println("Found error in logs for this url: " + logEntry.getMessage());
                                                }
                                                writer.println();
                                                ExecutionLog.log(errorLogs.size() + " Console error found");
                                            }
                                            driver.switchTo().defaultContent();
                                        } catch (Exception e) {
                                            ExecutionLog.log("Error occur during execution of url: " + url);
                                            writer.println("Error occur during execution of url: " + url);
                                            e.printStackTrace();
                                        }
                                        driver.close(); //closing child window
                                        driver.switchTo().window(parentWindow); //cntrl to parent window
                                    }
                                } catch (Exception e) {
                                    if (!childWindow.equals(parentWindow)) {
                                        driver.close(); //closing child window
                                    }
                                    driver.switchTo().window(parentWindow); //cntrl to parent window
                                    e.printStackTrace();
                                    ExecutionLog.log("Invalid URL found: " + url);
                                    writer.println("Invalid URL found: " + url);
                                }
                            }
                            writer.close();
                        } catch (Exception e) {
                            ExecutionLog.log("Invalid URL found: " + url);
                            writer.println("Invalid URL found: " + url);
                            writer.close();
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            writer.close();
        } catch (Exception e) {
            writer.println("Error occur during execution");
            writer.close();
            e.printStackTrace();
        }
    }
}
