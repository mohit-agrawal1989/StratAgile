import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.ExecutionLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class ParentClass {

    enum WebBrowser {Chrome}
    public static WebDriver driver;
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    String driverType = "chrome";
    static String url = "https://www.sc.com/sg/";
    String os = "windows";
    String type = "no";
    static WebDriverWait wait;


    @Test (priority = 0)
    public void w3cValidationCheck() {
        W3cValidationErrorCheck.w3cValidation(); // Checking the w3c validation of warnings and errors
    }

    @Test (priority = 1)
    public void brokenLinkCheck() throws FileNotFoundException, UnsupportedEncodingException {
        BrokenLinkTest.brokenLinkValidationCheck(); // Checking for broken links on Standard Chartered home page
    }

    @Test (priority = 2)
    public void jsValidationCheck(){
        JsValidationCheck.jsValidation(); // Checking for javascript warnings and errors
    }

    @Test (priority = 3)
    public void consoleLogsCheck() throws FileNotFoundException, UnsupportedEncodingException {
        ConsoleLogsValidationCheck.consoleLogsValidation(); // Checking the console log errors
    }

    @Test (priority = 4)
    public void responseDimensionCheck(){
        ResponsiveDimensionCheck.responsiveDimensionValidation(); // Checking for responsive dimension
    }

    @BeforeTest
    public void setup() {

        if (driver == null) {

            if (os.equalsIgnoreCase("Windows")) {
                if (driverType.equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + "//drivers//chromedriver.exe");
            } else if (os.equalsIgnoreCase("Mac")) {
                if (driverType.equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + "//drivers//chromedriver_mac");
            }

            //Check if desired browser is Chrome
            if (WebBrowser.Chrome.toString().equalsIgnoreCase(driverType)) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1600x900");
                if (type.equalsIgnoreCase("yes"))
                    driver = new ChromeDriver(options);
                else
                    driver = new ChromeDriver();
            }

            //If browser type is not matched, exit from the system
            else {
                String path = getPath();
                System.setProperty("webdriver.chrome.driver", path + "//drivers//chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1325x744");
                if (type.equalsIgnoreCase("yes"))
                    driver = new ChromeDriver(options);
                else
                    driver = new ChromeDriver();
            }
        }
        ExecutionLog.log("Browser has been initiated successfully");
        driver.manage().window().maximize();
        ExecutionLog.log("Window has been maximized to full screen");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 30);
    }

    @AfterTest
    public void exit(){
        driver.quit();
    }

    public String getPath() {
        String path = "";
        File file = new File("");
        String absolutePathOfFirstFile = file.getAbsolutePath();
        path = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
        return path;
    }
}

