import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;
import java.io.File;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class ResponseDimensionCheckBrokenImages extends ParentClass {
    private static PrintWriter writer;
    static boolean linkCheck = false;
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static HttpURLConnection huc = null;
    static int responseCode = 200;

    public static void responsiveDimensionBrokenLinksValidation(String[] countryURL) {
        try {
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "Screenshots");
            dir.mkdir();
            dir = new File(directoryPath + "" + File.separator + "Screenshots" + File.separator + "Iphone X");
            dir.mkdir();
            dir = new File(directoryPath + "" + File.separator + "Screenshots" + File.separator + "Iphone X" + File.separator + "BrokenImagesReport");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(", ");
            writer = new PrintWriter(directoryPath + "" + File.separator + "Screenshots" + File.separator + "Iphone X" + File.separator + "BrokenImagesReport" + File.separator + "BrokenImagesReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x]);
                    String newUrl = countryURLToNavigate[x];
                    driver.navigate().to(newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    Dimension dimension = new Dimension(375, 812);
                    ExecutionLog.log("Current window has been resized to the dimension of iphone X: 375*812");
                    driver.manage().window().setSize(dimension);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'hamburger only-mobile')]|//button[contains(@class,'hamburger no-desktop')]")));
                    driver.findElement(By.xpath("//button[contains(@class,'hamburger only-mobile')]|//button[contains(@class,'hamburger no-desktop')]")).click();
                    ExecutionLog.log("Clicked on the hamburger menu of mobile view");
                    driver.findElement(By.xpath("//*[contains(@class,'menu')]")).isDisplayed();
                    ExecutionLog.log("Menu list is visible");
                    driver.findElement(By.xpath("(//button[contains(@class,'close-button sc-menu-visible')]|//button[contains(@class,'close-button no-desktop')])[1]")).isDisplayed();
                    driver.findElement(By.xpath("(//button[contains(@class,'close-button sc-menu-visible')]|//button[contains(@class,'close-button no-desktop')])[1]")).click();
                    ExecutionLog.log("Menu list close button is clicked");
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(@class,'menu')]")));
                    ExecutionLog.log("Menu list is closed successfully");
//                Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
//                ImageIO.write(screenshot.getImage(),"PNG",new File("D://Screen//FullPageScreenshot.png"));

                    try {
                        List<WebElement> allLinks = driver.findElements(By.xpath("//img[@src]"));
                        ExecutionLog.log("There are " + allLinks.size() + " url to be verified for broken links");
                        Iterator<WebElement> iterator = allLinks.iterator();
                        while (iterator.hasNext()) {

                            url = iterator.next().getAttribute("src");

                            if (url == null || url.isEmpty()) {
                                linkCheck = true;
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
                                    linkCheck = true;
                                    ExecutionLog.log(url + " is a broken image");
                                    writer.println(url + " is a broken image");
                                } else {
                                    ExecutionLog.log(url + " is a valid image");
                                }
                            } catch (Exception e) {
                                linkCheck = true;
                                ExecutionLog.log(url + " This page threw the error");
                                writer.println(url + " This page threw the error");
                                e.printStackTrace();
                            }
                        }
                        if (!linkCheck) {
                            writer.println("No broken image found in the execution");
                        }
                    } catch (Exception e) {
                        writer.println("Error occur during execution of url : " + url);
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    writer.println("Error occur during execution of url : " + url);
                    writer.close();
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
