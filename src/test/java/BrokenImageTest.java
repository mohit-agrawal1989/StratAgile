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

public class BrokenImageTest extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static HttpURLConnection huc = null;
    static int responseCode = 200;
    static boolean imageCheck = false;
    private static PrintWriter writer;

    public static void brokenImageValidationCheck(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "BrokenImagesReport");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(", ");
            writer = new PrintWriter(directoryPath + "" + File.separator + "BrokenImagesReport" + File.separator + "BrokenImageReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x]);
                    String newUrl = countryURLToNavigate[x];
                    writer.println("Validating the Page : " + newUrl);
                    driver.navigate().to(newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));

//                Dimension dimension = new Dimension(375,812);
//                ExecutionLog.log("Current window has been resized to the dimension of iphone X: 375*812");
//                driver.manage().window().setSize(dimension);
                    List<WebElement> allLinks = driver.findElements(By.xpath("//img[@src]"));
                    ExecutionLog.log("There are " + allLinks.size() + " image url to be verified for broken images");
                    Iterator<WebElement> iterator = allLinks.iterator();
                    while (iterator.hasNext()) {

                        url = iterator.next().getAttribute("src");

                        if (url == null || url.isEmpty()) {
                            imageCheck = true;
                            ExecutionLog.log(url + " URL is either not configured for img tag or it is empty");
                            writer.println(url + " URL is either not configured for img tag or it is empty");
                            continue;
                        }

                        try {
                            huc = (HttpURLConnection) (new URL(url).openConnection());

                            huc.setRequestMethod("HEAD");

                            huc.connect();

                            responseCode = huc.getResponseCode();

                            if (responseCode >= 400) {
                                imageCheck = true;
                                ExecutionLog.log(url + " is a broken image");
                                writer.println(url + " is a broken image");
                            } else {
                                ExecutionLog.log(url + " is a valid image");
                            }
                        } catch (Exception e) {
                            imageCheck = true;
                            ExecutionLog.log(url + " This page threw the error");
                            writer.println(url + " This page threw the error");
                            e.printStackTrace();
                        }
                    }
                    if (!imageCheck) {
                        writer.println("No broken image found in the execution");
                    }
                } catch (Exception e) {
                    writer.println("Error occur during execution");
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

