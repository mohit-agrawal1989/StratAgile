import au.com.bytecode.opencsv.CSVReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utility.ExecutionLog;
import java.io.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ParentClass {

//    enum WebBrowser {Chrome, Firefox}

    public static WebDriver driver;
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    static String url = "https://www.sc.com/sg/";
    public static WebDriverWait wait, waitForAlert, longWait;
    public String country = "", filePath = "";
    Object[][] data = null;
    String[] countryArray;
    public static String directoryPath = "", date;
    public static String path = "config.properties";
    public static String headless = readApplicationFile("headless", path);
    static String responsivePath = "responsiveDevices.properties";
    public Map<Integer, String> map = new LinkedHashMap<Integer, String>();
    public Map<Object, Object> responsiveDeviceData = new LinkedHashMap<>();
    public static final String browserStackUserName = readApplicationFile("browserStackUserName", path);
    public static final String browserStackAccessKey = readApplicationFile("browserStackAccessKey", path);
    public static final String URL = "https://" + browserStackUserName + ":" + browserStackAccessKey + "@hub-cloud.browserstack.com/wd/hub";
    public String responsiveDataCollection = "";


    @Test(priority = 0, dataProvider = "testData")
    public void w3cValidationCheck(String[] countryURL) {
//        W3cValidationErrorCheck.w3cValidation(countryURL); // Checking the w3c validation of warnings and errors
    }

    @Test (priority = 1, dataProvider = "testData")
    public void consoleLogsCheck(String[] countryURL) {
        ConsoleLogsValidationCheck.consoleLogsValidation(countryURL); // Checking the console log errors
    }

    @Test (priority = 2, dataProvider = "testData")
    public void jsValidationCheck(String[] countryURL){
        JsValidationCheck.jsValidation(countryURL); // Checking for javascript warnings and errors
    }

    @Test (priority = 3, dataProvider = "testData")
    public void brokenLinkCheck(String[] countryURL){
        BrokenLinkTest.brokenLinkValidationCheck(countryURL); // Checking for broken links
    }

    @Test (priority = 4, dataProvider = "testData")
    public void brokenImagesCheck(String[] countryURL)  {
        BrokenImageTest.brokenImageValidationCheck(countryURL); // Checking for broken images
        BrokenImageTest.brokenSrcSetValidationCheck(countryURL); // Checking for broken srcSet
    }

    @Test (priority = 5, dataProvider = "testData")
    public void responseDimensionCheckBrokenLinks(String[] countryURL){
        ResponseDimensionCheckBrokenLinks.responsiveDimensionBrokenLinksValidation(countryURL); // Checking for responsive dimension
    }

    @Test (priority = 6, dataProvider = "testData")
    public void responseDimensionCheckBrokenImages(String[] countryURL){
        ResponseDimensionCheckBrokenImages.responsiveDimensionBrokenImagesValidation(countryURL); // Checking for responsive dimension
    }

    @Test (priority = 7, dataProvider = "testData")
    public void responsiveDimensionConsoleLogs(String[] countryURL) {
        ResponsiveDimensionConsoleLogsCheck.responsiveDimensionConsoleLogsCheck(countryURL); // Checking the console log errors
    }


    @BeforeTest
    public void setup() throws MalformedURLException {

        if (driver == null) {

            if (readApplicationFile("os", path).equalsIgnoreCase("Windows")) {
                if (readApplicationFile("browser", path).equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + File.separator + "drivers" + File.separator + "chromedriver.exe");
                if (readApplicationFile("browser", path).equalsIgnoreCase("firefox"))
                    System.setProperty("webdriver.gecko.driver", getPath() + File.separator + "drivers" + File.separator + "geckodriver.exe");
            } else if (readApplicationFile("os", path).equalsIgnoreCase("Mac")) {
                if (readApplicationFile("browser", path).equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + "" + File.separator + "drivers" + File.separator + "chromedriver_mac");
            }else if(readApplicationFile("os", path).equalsIgnoreCase("linux")){
                if (readApplicationFile("browser", path).equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + "" + File.separator + "drivers" + File.separator + "chromedriver_linux");
            }

            //Check if desired browser is Chrome
            if (readApplicationFile("browser", path).equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                //Check if execution is meant to be run or browserStackExecution or in a natural way
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
                }else {
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
            }
//            else if (readApplicationFile("browser", path).equalsIgnoreCase("firefox")) {
//                FirefoxOptions options = new FirefoxOptions();
//                    //Check if execution is meant to be run or browserStackExecution or in a natural way
//                    if (readApplicationFile("browserStackExecution", path).equalsIgnoreCase("yes")) {
//                        options.setCapability("os", readApplicationFile("os", path));
//                        options.setCapability("os_version", readApplicationFile("os_version", path));
//                        options.setCapability("browser", readApplicationFile("browser", path));
//                        options.setCapability("browser_version", readApplicationFile("browser_version", path));
//                        options.setCapability("project", "Standard Chartered");
//                        options.setCapability("build", "1");
//                        options.setCapability("name", "ST");
//                        options.setCapability("browserstack.local", "true");
//                        options.setCapability("browserstack.debug", "true");
//                        options.setCapability("browserstack.networkLogs", "true");
//                        options.setCapability("browserstack.selenium_version", "3.14.0");
//                        options.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
//                        driver = new RemoteWebDriver(new URL(URL), options);
//                    }else {
//                        if (headless.equalsIgnoreCase("yes")) {
//                            options.addArguments("--headless");
//                        }
//                        options.addArguments("--no-sandbox");
//                        options.addArguments("--disable-gpu");
//                        options.addArguments("--incognito");
//                        options.addArguments("--disable-dev-shm-usage");
//                        options.addArguments("--window-size=1600x900");
//                        driver = new FirefoxDriver(options);
//                    }
//                }
            //country = "sg".toLowerCase();
            country = System.getProperty("country").toLowerCase();
            System.out.println("Country: " + country);
        }
        ExecutionLog.log("Browser has been initiated successfully");
        driver.manage().window().maximize();
        ExecutionLog.log("Window has been maximized to full screen");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 30);
        waitForAlert = new WebDriverWait(driver, 3);
        longWait = new WebDriverWait(driver, 120);

        countryArray = country.split(",");
        for (int i = 0; i < countryArray.length; i++) {
            File file = new File( readApplicationFile("outputPath", path) + File.separator + "" + countryArray[i]);
            file.mkdir();
            Date d = new java.util.Date(System.currentTimeMillis());
            String format = "yyyy-MM-dd hh-mm-ss";
            DateFormat dateFormatter = new SimpleDateFormat(format);
            date = dateFormatter.format(d);
            directoryPath = readApplicationFile("outputPath", path) + File.separator + "" + countryArray[i] + "" + File.separator + "" + date;
            File dir = new File(readApplicationFile("outputPath", path) + File.separator + "" + countryArray[i] + "" + File.separator + "" + date);
            dir.mkdir();
        }
        // Adding the responsive dimension data along with the other data
        readResponsiveDeviceFile(responsivePath);
    }


    @DataProvider(name = "testData")
    public Object[][] userData() {
        try {
            File dir = new File(readApplicationFile("inputPath", path));
            int numberOfSubFolders = 0;
            File listDir[] = dir.listFiles();
            for (int i = 0; i < listDir.length; i++) {
                if (listDir[i].isDirectory()) {
                    numberOfSubFolders++;
                }
            }
            System.out.println("No of directories: " + numberOfSubFolders);



            String line;
            int pageCount = 0;
            for (int i = 0; i < numberOfSubFolders; i++) {
            //parsing a CSV file into Scanner class constructor
                BufferedReader br = new BufferedReader(new FileReader(readApplicationFile("inputPath", path) + File.separator + countryArray[i] + File.separator + "url.csv"));
                while ((line = br.readLine()) != null)   //returns a Boolean value
                {
                    line = line.replaceAll("\"\"", "\"");
                    line = line.replaceAll(" ", "");
                    String[] countryURLToNavigate = line.split(",");    // use comma as separator
                    if (countryURLToNavigate.length > pageCount) {
                            pageCount = countryURLToNavigate.length;
                    }
                    map.put(i, countryArray[i] + "# " + line);
                    System.out.print(line);
                }
                System.out.print("\n");
            }


//            int pageCount = 0;
//            for (int i = 0; i < numberOfSubFolders; i++) {
//                CSVReader reader = new CSVReader(new FileReader(readApplicationFile("inputPath", path) + File.separator + countryArray[i] + File.separator + "url.csv"));
//                String[] nextLine;
//                //reads one line at a time
//                while ((nextLine = reader.readNext()) != null) {
//                    for (String token : nextLine) {
//                        String[] countryURLToNavigate = token.split(",");
//                        if (countryURLToNavigate.length > pageCount) {
//                            pageCount = countryURLToNavigate.length;
//                        }
//                        map.put(i, countryArray[i] + "# " + token);
//                        System.out.print(token);
//                    }
//                    System.out.print("\n");
//
//                }
//            }

            data = new Object[countryArray.length][pageCount + responsiveDeviceData.size()];
            for (int m = 0; m < map.size(); m++) {
                data[m][0] = map.get(m) + responsiveDataCollection;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    @AfterTest
    public void exit() {
        driver.quit();
    }

    public static String readApplicationFile(String key, String file){
        String value = "";
        String path = getPath();
        try{
            Properties prop = new Properties();
            File f = new File(path + "/src/main/resources/configuration/" +file);
            if(f.exists()){
                prop.load(new FileInputStream(f));
                value = prop.getProperty(key);
            }
        }
        catch(Exception e){
            System.out.println("Failed to read from config.properties file.");
        }
        return value;
    }

    public void readResponsiveDeviceFile(String filePath){
        String path = getPath();
        try{
            Properties prop = new Properties();
            File file = new File(path + "/src/main/resources/configuration/" +filePath);
            if(file.exists()){
                prop.load(new FileInputStream(file));
                int i = 0;
                for(Map.Entry<Object, Object> entry: prop.entrySet()){
                    String responsiveDevice = entry.getKey().toString();
                    String responsiveDimension = entry.getValue().toString();
                    responsiveDeviceData.put(i, entry.getKey()+ "%%" +entry.getValue());
                    responsiveDataCollection = responsiveDataCollection + "@@" + entry.getKey().toString() + "%%" + entry.getValue().toString().trim();
                    System.out.println(responsiveDevice+" : "+responsiveDimension);
                    i++;
                }
            }
        }
        catch(Exception e){
            System.out.println("Failed to read from responsiveDevices.properties file.");
        }
    }
    public static String getPath() {
        String path = "";
        File file = new File("");
        String absolutePathOfFirstFile = file.getAbsolutePath();
        path = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
        return path;
    }
}

