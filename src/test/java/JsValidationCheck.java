import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class JsValidationCheck extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static ParentClass parentClass = new ParentClass();
    private static PrintWriter writer;
    static int errorCounter = 0;

    public static void jsValidation(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "JsValidationSummary");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");
            writer = new PrintWriter(directoryPath + "" + File.separator + "JsValidationSummary" + File.separator + "JsValidationReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    writer.println();
                    System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                    String newUrl = countryURLToNavigate[x].replace("\"", "");
                    driver.manage().deleteAllCookies();
                    driver.quit();
                    ChromeOptions options = new ChromeOptions();


                    if (readApplicationFile("browserStackExecution", path).equalsIgnoreCase("yes")) {
                        DesiredCapabilities caps = new DesiredCapabilities();
                        caps.setCapability("os", readApplicationFile("os", path));
                        caps.setCapability("os_version", readApplicationFile("os_version", path));
                        caps.setCapability("browser", readApplicationFile("browser", path));
                        caps.setCapability("browser_version", readApplicationFile("browser_version", path));
                        caps.setCapability("project", "Standard Chartered");
                        caps.setCapability("build", "1");
                        caps.setCapability("name", "ST");
                        caps.setCapability("browserstack.local", "true");
                        caps.setCapability("browserstack.debug", "true");
                        caps.setCapability("browserstack.networkLogs", "true");
                        caps.setCapability("browserstack.selenium_version", "3.14.0");
                        caps.setCapability(ChromeOptions.CAPABILITY, options);

                        driver = new RemoteWebDriver(new URL(URL), caps);
                    } else {
                        if (headless.equalsIgnoreCase("yes")) {
                            options.addArguments("--headless");
                        }
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-gpu");
                        options.addArguments("--incognito");
                        options.addArguments("--disable-dev-shm-usage");
                        options.addArguments("--window-size=1600x900");
                        driver = new ChromeDriver(options);
                    }
                    ExecutionLog.log("Browser has been initiated successfully");
                    driver.manage().window().maximize();
                    ExecutionLog.log("Window has been maximized to full screen");
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    wait = new WebDriverWait(driver, 30);
                    driver.navigate().to("https://seositecheckup.com/analysis");
//                    new WebDriverWait(driver, 60).until(webDriver ->
//                            js.executeScript("return document.readyState").equals("complete"));

                    ExecutionLog.log("Validating the page for JS error check : " + newUrl);
                    writer.println("Validating the page for JS error check  : " + newUrl);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@placeholder='Website URL'])[1]")));
                    Thread.sleep(3000);
                    driver.findElement(By.xpath("(//input[@placeholder='Website URL'])[1]")).sendKeys(newUrl);
                    ExecutionLog.log("Entered the standard chartered url for js validation into the address bar for validation");
                    driver.findElement(By.xpath("(//button[@type='submit' and text()='Checkup'])[1]")).click();
                    ExecutionLog.log("Clicked on the check button to validate the standard chartered url address");
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h2[text()='Loading results']")));
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//div[@class='card-header']//div[@class='ant-progress-inner'])[1]")));
                    ExecutionLog.log("Progress bar under the card header is complete");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='report-summary']")));
                    ExecutionLog.log("Report summary has been created successfully");
                    writer.println("Report summary has been created successfully");
                    String jsFailedCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item failed']//span")).getText();
                    ExecutionLog.log("JS failed count : " + jsFailedCount);
                    writer.println("JS failed count : " + jsFailedCount);
                    String jsWarningCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item warning']//span")).getText();
                    ExecutionLog.log("JS warning count : " + jsWarningCount);
                    writer.println("JS warning count : " + jsWarningCount);
                    String jsPassedCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item passed']//span")).getText();
                    ExecutionLog.log("JS passed count : " + jsPassedCount);
                    writer.println("JS passed count : " + jsPassedCount);
                    errorCounter = errorCounter + driver.findElements(By.xpath("//div[@class='report-element failed']//div[@class='report-element-title']//strong")).size();
                    for(int i = 1; i <= driver.findElements(By.xpath("//div[@class='report-element failed']//div[@class='report-element-title']//strong")).size(); i++){
                        String errorHeading = driver.findElement(By.xpath("(//div[@class='report-element failed']//div[@class='report-element-title']//strong)["+i+"]")).getText();
                        String errorDescription = driver.findElement(By.xpath("(//div[@class='report-element failed']//div[@class='report-element-description']//li//div)["+i+"]")).getText();
                        writer.println();
                        ExecutionLog.log("Error Heading "+i+" : " + errorHeading);
                        writer.println("Error Heading "+i+" : " + errorHeading);
                        ExecutionLog.log("Error Description "+i+" : " + errorDescription);
                        writer.println("Error Description "+i+" : " + errorDescription);
                        writer.println();
                    }
                } catch (Exception e) {
                    writer.println("Error occur during execution inside the loop iteration");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            writer.println("Exception occurred in the main try block");
            e.printStackTrace();
        }
        finally{
            writer.println("*************************************************");
            writer.println();
            writer.println("Total error found in Js validation : "+ errorCounter);
            writer.close();
        }
    }
}
