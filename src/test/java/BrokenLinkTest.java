import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;
import java.io.File;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class BrokenLinkTest extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static HttpURLConnection huc = null;
    static int responseCode = 200;
    static boolean linkCheck = false;
    private static PrintWriter writer;

    public static void brokenLinkValidationCheck(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "BrokenLinkReport");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(", ");
            writer = new PrintWriter(directoryPath + "" + File.separator + "BrokenLinkReport" + File.separator + "BrokenLinkReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x]);
                    String newUrl = countryURLToNavigate[x];
                    driver.navigate().to(newUrl);
                    writer.println("Validating the Page : " + newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));

//                Dimension dimension = new Dimension(375,812);
//                ExecutionLog.log("Current window has been resized to the dimension of iphone X: 375*812");
//                driver.manage().window().setSize(dimension);
                    List<WebElement> allLinks = driver.findElements(By.xpath("//a[@href]"));
                    ExecutionLog.log("There are " + allLinks.size() + " url to be verified for broken links");
                    Iterator<WebElement> iterator = allLinks.iterator();
                    while (iterator.hasNext()) {

                        url = iterator.next().getAttribute("href");

                        if (url == null || url.isEmpty()) {
                            linkCheck = true;
                            ExecutionLog.log(url + " URL is either not configured for anchor tag or it is empty");
                            writer.println(url + " URL is either not configured for anchor tag or it is empty");
                            continue;
                        }

                        try {
                            huc = (HttpURLConnection) (new URL(url).openConnection());

                            huc.setRequestMethod("HEAD");

                            huc.connect();

                            responseCode = huc.getResponseCode();

                            if (responseCode >= 400) {
                                linkCheck = true;
                                ExecutionLog.log(url + " is a broken link");
                                writer.println(url + " is a broken link");
                            } else {
                                ExecutionLog.log(url + " is a valid link");
                            }
                        } catch (Exception e) {
                            linkCheck = true;
                            ExecutionLog.log(url + " This url threw the error");
                            writer.println(url + " This url threw the error");
                            e.printStackTrace();
                        }
                    }
                    if (!linkCheck) {
                        writer.println("No broken link found in the execution");
                    }
                } catch (Exception e) {
                    writer.println("Error occur during execution while testing url : " + url);
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