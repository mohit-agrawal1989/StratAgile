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
    static int brokenImageCounter = 0, brokenSrcSetTagCounter = 0;

    public static void brokenImageValidationCheck(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "BrokenImagesReport");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");
            writer = new PrintWriter(directoryPath + "" + File.separator + "BrokenImagesReport" + File.separator + "BrokenImageReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                    String newUrl = countryURLToNavigate[x].replace("\"", "");
                    writer.println("Validating the Page : " + newUrl);
                    writer.println();
                    driver.navigate().to(newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    List<WebElement> allLinks = driver.findElements(By.xpath("//img[@src]"));
                    ExecutionLog.log("There are " + allLinks.size() + " image url to be verified for broken images");
                    Iterator<WebElement> iterator = allLinks.iterator();
                    while (iterator.hasNext()) {

                        url = iterator.next().getAttribute("src");

                        if (url == null || url.isEmpty()) {
                            imageCheck = true;
                            brokenImageCounter += 1;
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
                                brokenImageCounter += 1;
                                ExecutionLog.log(url + " is a broken image");
                                writer.println(url + " is a broken image");
                            } else {
                                ExecutionLog.log(url + " is a valid image");
                            }
                        } catch (Exception e) {
                            imageCheck = true;
                            brokenImageCounter += 1;
                            ExecutionLog.log(url + " This page threw the error");
                            writer.println(url + " This page threw the error");
                            e.printStackTrace();
                        }
                    }
                    if (!imageCheck) {
                        writer.println("No broken image found in the execution");
                    }
                } catch (Exception e) {
                    brokenImageCounter += 1;
                    writer.println("Error occur during execution");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            brokenImageCounter += 1;
            writer.println("Exception occurred in the main try block");
            e.printStackTrace();
        }
    }

    public static void brokenSrcSetValidationCheck(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");
            writer.println();
            writer.println("****************************************************************************************");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    writer.println();
                    System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                    String newUrl = countryURLToNavigate[x].replace("\"", "");
                    writer.println("Validating the Page : " + newUrl);
                    driver.navigate().to(newUrl);
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    List<WebElement> allLinks = driver.findElements(By.xpath("//*[@srcSet]"));
                    ExecutionLog.log("There are " + allLinks.size() + " url to be verified for broken srcSet");
                    Iterator<WebElement> iterator = allLinks.iterator();
                    imageCheck = false;
                    while (iterator.hasNext()) {
                        String[] splitAllURL = iterator.next().getAttribute("srcSet").split(",");
                        String[] splitURLPixels;
                        for (int i = 0; i < splitAllURL.length; i++) {
                            if (splitAllURL[i].contains("png")) {
                                splitURLPixels = splitAllURL[i].split("png");
                                url = splitURLPixels[0] + "png";
                            } else if (splitAllURL[i].contains("jpg")) {
                                splitURLPixels = splitAllURL[i].split("jpg");
                                url = splitURLPixels[0] + "jpg";
                            }
                            else{
                                writer.println("This url contains neither png nor jpg" + splitAllURL[i]);
                            }
                            if (url == null || url.isEmpty()) {
                                imageCheck = true;
                                brokenSrcSetTagCounter += 1;
                                ExecutionLog.log(url + " URL is either not configured for srcSet attribute or it is empty");
                                writer.println(url + " URL is either not configured for srcSet attribute or it is empty");
                                continue;
                            }

                            try {
                                huc = (HttpURLConnection) (new URL(url).openConnection());

                                huc.setRequestMethod("HEAD");

                                huc.connect();

                                responseCode = huc.getResponseCode();

                                if (responseCode >= 400) {
                                    imageCheck = true;
                                    brokenSrcSetTagCounter += 1;
                                    ExecutionLog.log(url + " is a broken srcSet");
                                    writer.println(url + " is a broken srcSet");
                                } else {
                                    ExecutionLog.log(url + " is a valid srcSet");
                                }
                            } catch (Exception e) {
                                imageCheck = true;
                                brokenSrcSetTagCounter += 1;
                                ExecutionLog.log(url + " This page threw the error");
                                writer.println(url + " This page threw the error");
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!imageCheck) {
                        writer.println("No broken srcSet found in the execution");
                    }
                } catch (Exception e) {
                    brokenSrcSetTagCounter += 1;
                    writer.println("Error occur during execution");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            brokenSrcSetTagCounter += 1;
            writer.println("Exception occurred in the main try block");
            e.printStackTrace();
        } finally {
            writer.println("*************************************************");
            writer.println();
            writer.println("Total error found in broken image validation : " + brokenImageCounter);
            writer.println();
            writer.println("*************************************************");
            writer.println();
            writer.println("Total error found in broken srcSet validation : " + brokenSrcSetTagCounter);
            writer.close();
        }
    }
}

