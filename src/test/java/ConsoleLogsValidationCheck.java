import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utility.ExecutionLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ConsoleLogsValidationCheck extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    private static PrintWriter writer;
    static boolean consoleErrorCheck = false;
    static int brokenConsoleLogsCounter = 0;
    public static void consoleLogsValidation(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "Console Errors");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");
            writer = new PrintWriter(directoryPath + "" + File.separator + "Console Errors" + "" + File.separator + "Console Error Report.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                    String newUrl = countryURLToNavigate[x].replace("\"", "");
                    writer.println("Validating the Page : " + newUrl);
                    driver.navigate().to(newUrl);
                    writer.println();
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    if(driver.findElements(By.cssSelector(".sc-nav__wrapper")).size() > 0){
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.position='initial'", driver.findElement(By.cssSelector(".sc-nav__wrapper")));
                    }else if(driver.findElements(By.cssSelector(".sc-hdr__wrapper.sc-hdr__container")).size() > 0){
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.position='initial'", driver.findElement(By.cssSelector(".sc-hdr__wrapper.sc-hdr__container")));
                    }

//                    dir = new File(directoryPath + "" + File.separator + "Desktop Screenshots");
//                    dir.mkdir();
//                    File src = ((FirefoxDriver)driver).getFullPageScreenshotAs(OutputType.FILE);
//                    FileHandler.copy(src, new File(directoryPath + "" + File.separator + "Desktop Screenshots" + File.separator + newUrl.replaceAll("[^a-zA-Z0-9]", "_")+".png"));


//                    if(driver.findElements(By.xpath("//div[@class='sc-alert']//button | //div[@class='m-warning-alert active']//a[@class='close-btn']")).size() > 0){
//                        driver.findElement(By.xpath("//div[@class='sc-alert']//button | //div[@class='m-warning-alert active']//a[@class='close-btn']")).click();
//                    }

                    if(driver.findElements(By.cssSelector(".sc-alert")).size() > 0)
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", driver.findElement(By.cssSelector(".sc-alert")));
                    else                     if(driver.findElements(By.cssSelector(".m-warning-alert.active, .m-warning-alert.visible")).size() > 0)
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", driver.findElement(By.cssSelector(".m-warning-alert.active, .m-warning-alert.visible")));


                    Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(2f), 1000)).takeScreenshot(driver);
                    BufferedImage image = screenshot.getImage();
                    dir = new File(directoryPath + "" + File.separator + "Desktop Screenshots");
                    dir.mkdir();
                    ImageIO.write(image, "png",
                            new File(directoryPath + "" + File.separator + "Desktop Screenshots" + File.separator + newUrl.replaceAll("[^a-zA-Z0-9]", "_")+".png"));
                    List<WebElement> allLinks = driver.findElements(By.xpath("//a[@href]"));
                    ExecutionLog.log("There are " + allLinks.size() + " hyperlinks");
                    writer.println("There are " + allLinks.size() + " hyperlinks in this url");
                    String url = "";
                    try {
                        url = newUrl;
                        try {
                            Logs logs = driver.manage().logs();
                            LogEntries logEntries = logs.get(LogType.BROWSER);
                            List<LogEntry> errorLogs = logEntries.filter(Level.SEVERE);
                            if (errorLogs.size() != 0) {
                                Thread.sleep(2000);
                                brokenConsoleLogsCounter += 1;
                                for (LogEntry logEntry : logEntries) {
                                    Thread.sleep(2000);
                                    consoleErrorCheck = true;
                                    ExecutionLog.log("Found error in logs for url \"" + url + "\" and message : " + logEntry.getMessage());
                                    writer.println("Found error in logs for url \"" + url + "\" and message : " + logEntry.getMessage());
                                }
                                writer.println();
                                ExecutionLog.log(errorLogs.size() + " Console error found");
                            }
                        } catch (Exception e) {
                            Thread.sleep(2000);
                            consoleErrorCheck = true;
                            brokenConsoleLogsCounter += 1;
                            ExecutionLog.log("Error occur during execution of url: " + url);
                            writer.println("Error occur during execution of url: " + url);
                            e.printStackTrace();
                        }
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        consoleErrorCheck = true;
                        brokenConsoleLogsCounter += 1;
                        ExecutionLog.log("Invalid URL found: " + url);
                        e.printStackTrace();
                    }
                    if (!consoleErrorCheck) {
                        writer.println("No console error found in the execution");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.println("*************************************************");
            writer.println();
            writer.println("Total error found in console logs validation : "+ brokenConsoleLogsCounter);
            writer.close();
        }
    }
}
