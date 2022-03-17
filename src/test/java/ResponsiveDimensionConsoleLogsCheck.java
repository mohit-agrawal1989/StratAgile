import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
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

public class ResponsiveDimensionConsoleLogsCheck extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    private static PrintWriter writer;

    public static void responsiveDimensionConsoleLogsCheck(String[] countryURL) {
        try {
            String[] splitResponsiveDeviceAndResolution = null;
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "ResponsiveUI");
            dir.mkdir();
            // Starting here the loop for each responsive UI
            for (int index = 1; index < splitResponsiveData.length; index++) {
                splitResponsiveDeviceAndResolution = splitResponsiveData[index].split("%%");
                splitResponsiveDeviceAndResolution[0] = splitResponsiveDeviceAndResolution[0].replaceAll("/", " Or ");
                dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0]);
                dir.mkdir();
                dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "ConsoleErrorsReport");
                dir.mkdir();
                String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");

                for (int x = 0; x < countryURLToNavigate.length; x++) {
                    try {
                        System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                        String newUrl = countryURLToNavigate[x].replace("\"", "");
                        driver.navigate().to(newUrl);
                        new WebDriverWait(driver, 60).until(webDriver ->
                                js.executeScript("return document.readyState").equals("complete"));
                        dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "ConsoleErrorsReport" + File.separator + newUrl.replaceAll("[^a-zA-Z0-9]", "."));
                        dir.mkdir();
                        try {
                            url = newUrl;
                            String[] urlExtension = url.split("/");
                            //System.out.println("URL Extension: " + urlExtension[urlExtension.length - 1]);
                            writer = new PrintWriter(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "ConsoleErrorsReport" + "" + File.separator + newUrl.replaceAll("[^a-zA-Z0-9]", ".") + File.separator + "" + urlExtension[urlExtension.length - 1].replaceAll("/ " + File.separator + " : * ? \" < > |", "") + ".txt", "UTF-8");
                            try {
                                Logs logs = driver.manage().logs();
                                LogEntries logEntries = logs.get(LogType.BROWSER);
                                List<LogEntry> errorLogs = logEntries.filter(Level.SEVERE);

                                if (errorLogs.size() != 0) {
                                    Thread.sleep(2000);
                                    for (LogEntry logEntry : logEntries) {
                                        Thread.sleep(2000);
                                        ExecutionLog.log("Found error in logs for url \"" + url + "\" and message : " + logEntry.getMessage());
                                        writer.println("Found error in logs for url \"" + url + "\" and message : " + logEntry.getMessage());
                                    }
                                    writer.println();
                                    ExecutionLog.log(errorLogs.size() + " Console error found");
                                }

                            } catch (Exception e) {
                                Thread.sleep(2000);
                                ExecutionLog.log("Error occur during execution of url: " + url);
                                writer.println("Error occur during execution of url: " + url);
                                e.printStackTrace();
                            }
                            Thread.sleep(2000);
                            writer.close();
                        } catch (Exception e) {
                            ExecutionLog.log("Invalid URL found: " + url);
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

