import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.ExecutionLog;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BrokenLinkTest extends ParentClass{
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static HttpURLConnection huc = null;
    static int responseCode = 200;

    public static void brokenLinkValidationCheck() throws FileNotFoundException, UnsupportedEncodingException {
        driver.navigate().to("https://www.sc.com/sg/");
        new WebDriverWait(driver, 60).until(webDriver ->
                js.executeScript("return document.readyState").equals("complete"));
                List<WebElement> allLinks = driver.findElements(By.xpath("//a[@href]"));
                ExecutionLog.log("There are "+allLinks.size()+" url to be verified for broken links");
        Iterator<WebElement> iterator = allLinks.iterator();
        PrintWriter writer = new PrintWriter("BrokenLinkReport.txt", "UTF-8");
        while(iterator.hasNext()) {

            url = iterator.next().getAttribute("href");

            if (url == null || url.isEmpty()) {
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
                    ExecutionLog.log(url + " is a broken link");
                    writer.println(url + " is a broken link");
                } else {
                    ExecutionLog.log(url + " is a valid link");
                }
            } catch (Exception e) {
                ExecutionLog.log(url + " This url threw the error");
                writer.println(url + " This url threw the error");
                e.printStackTrace();
            }
        }
        writer.close();
    }
}
