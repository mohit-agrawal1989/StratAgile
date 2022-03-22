import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utility.ExecutionLog;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    public static void responsiveDimensionBrokenImagesValidation(String[] countryURL) {
        try {
            String[] splitResponsiveDeviceAndResolution = null;
            String[] splitResponsiveData = countryURL[0].split("@@");
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "ResponsiveUI");
            dir.mkdir();
            // Starting here the loop for each responsive UI
            for(int index = 1; index < splitResponsiveData.length; index++) {
                splitResponsiveDeviceAndResolution = splitResponsiveData[index].split("%%");
                splitResponsiveDeviceAndResolution[0] = splitResponsiveDeviceAndResolution[0].replaceAll("/", " Or ");
                dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0]);
                dir.mkdir();
                dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "BrokenImagesReport");
                dir.mkdir();
                //String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(", ");
                String[] countryURLToNavigate = splitResponsiveData[0].replaceAll(country[0] + "# ", "").split(",");
                writer = new PrintWriter(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "BrokenImagesReport" + File.separator + "BrokenImagesReport.txt", "UTF-8");
                for (int x = 0; x < countryURLToNavigate.length; x++) {
                    try {
                        writer.println("________________________________________________________________________________________");
                        System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                        String newUrl = countryURLToNavigate[x].replace("\"", "");
                        driver.navigate().to(newUrl);
                        writer.println("Validating the Page : " + newUrl);
                        new WebDriverWait(driver, 60).until(webDriver ->
                                js.executeScript("return document.readyState").equals("complete"));
                        String[] splitResolution = splitResponsiveDeviceAndResolution[1].split("\\*");
                        Dimension dimension = new Dimension(Integer.parseInt(splitResolution[0]), Integer.parseInt(splitResolution[1]));
                        ExecutionLog.log("Current window has been resized to the dimension of "+splitResponsiveDeviceAndResolution[0]+": "+splitResolution[0]+"*"+splitResolution[0]+"");
                        driver.manage().window().setSize(dimension);
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'hamburger only-mobile')]|//button[contains(@class,'hamburger no-desktop')]")));
//                        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
//                        BufferedImage image = screenshot.getImage();
//                        dir = new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "BrokenImagesReport" + File.separator + "Screenshots");
//                        dir.mkdir();
//                        ImageIO.write(image, "png",
//                                new File(directoryPath + "" + File.separator + "ResponsiveUI" + File.separator + splitResponsiveDeviceAndResolution[0] + File.separator + "BrokenImagesReport" + File.separator + "Screenshots" + File.separator + newUrl.replaceAll("[^a-zA-Z0-9]", "_")+".png"));
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
                        e.printStackTrace();
                    }
                }
                writer.close();
            }
        } catch (Exception e) {
            writer.println("Exception occurred in the main try block");
            e.printStackTrace();
        }
    }
}
