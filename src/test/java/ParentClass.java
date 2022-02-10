import au.com.bytecode.opencsv.CSVReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utility.ExecutionLog;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;;
import java.util.concurrent.TimeUnit;

public class ParentClass {

    enum WebBrowser {Chrome}

    public static WebDriver driver;
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    String driverType = "chrome";
    static String url = "https://www.sc.com/sg/";
    String os = "windows";
    String type = "yes";
    public static WebDriverWait wait, waitForAlert;
    public String country = "", filePath = "";
    Object[][] data = null;

    String[] countryArray;
    public static String directoryPath = "", date;
    static String path = "config.properties";
    static String responsivePath = "responsiveDevices.properties";
    public Map<Integer, String> map = new LinkedHashMap<Integer, String>();
    public Map<Object, Object> responsiveDeviceData = new LinkedHashMap<>();

    public String responsiveDataCollection = "";


    @Test(priority = 0, dataProvider = "testData")
    public void w3cValidationCheck(String[] countryURL) {
        W3cValidationErrorCheck.w3cValidation(countryURL); // Checking the w3c validation of warnings and errors
    }

    @Test (priority = 1, dataProvider = "testData")
    public void jsValidationCheck(String[] countryURL){
        JsValidationCheck.jsValidation(countryURL); // Checking for javascript warnings and errors
    }

    @Test (priority = 2, dataProvider = "testData")
    public void brokenLinkCheck(String[] countryURL){
        BrokenLinkTest.brokenLinkValidationCheck(countryURL); // Checking for broken links on Standard Chartered home page
    }

    @Test (priority = 3, dataProvider = "testData")
    public void brokenImagesCheck(String[] countryURL)  {
        BrokenImageTest.brokenImageValidationCheck(countryURL); // Checking for broken images on Standard Chartered home page
    }

    @Test (priority = 4, dataProvider = "testData")
    public void responseDimensionCheckBrokenLinks(String[] countryURL){
        ResponseDimensionCheckBrokenLinks.responsiveDimensionBrokenLinksValidation(countryURL); // Checking for responsive dimension
    }

    @Test (priority = 5, dataProvider = "testData")
    public void responseDimensionCheckBrokenImages(String[] countryURL){
        ResponseDimensionCheckBrokenImages.responsiveDimensionBrokenImagesValidation(countryURL); // Checking for responsive dimension
    }

    @Test (priority = 6, dataProvider = "testData")
    public void consoleLogsCheck(String[] countryURL) {
        ConsoleLogsValidationCheck.consoleLogsValidation(countryURL); // Checking the console log errors
    }

    @Test (priority = 7, dataProvider = "testData")
    public void responsiveDimensionConsoleLogs(String[] countryURL) {
        ResponsiveDimensionConsoleLogsCheck.responsiveDimensionConsoleLogsCheck(countryURL); // Checking the console log errors
    }


    @BeforeTest
    public void setup() {

        if (driver == null) {

            if (os.equalsIgnoreCase("Windows")) {
                if (driverType.equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + File.separator + "drivers" + File.separator + "chromedriver.exe");
            } else if (os.equalsIgnoreCase("Mac")) {
                if (driverType.equalsIgnoreCase("chrome"))
                    System.setProperty("webdriver.chrome.driver", getPath() + "" + File.separator + "drivers" + File.separator + "chromedriver_mac");
            }

            //Check if desired browser is Chrome
            if (WebBrowser.Chrome.toString().equalsIgnoreCase(driverType)) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--incognito");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1600x900");
                driver = new ChromeDriver(options);
//                country = "sg".toLowerCase();
                country = System.getProperty("country").toLowerCase();
                //filePath = System.getProperty("filePath");
                System.out.println("Country: " + country);
                System.out.println("FilePath: " + filePath);
            }

//            //If browser type is not matched, exit from the system
//            else {
//                String path = getPath();
//                System.setProperty("webdriver.chrome.driver", path + File.separator + "drivers" + File.separator + "chromedriver");
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--no-sandbox");
//                options.addArguments("--headless");
//                options.addArguments("--disable-gpu");
//                options.addArguments("--disable-dev-shm-usage");
//                options.addArguments("--window-size=1325x744");
//                driver = new ChromeDriver(options);
//            }
        }
        ExecutionLog.log("Browser has been initiated successfully");
        driver.manage().window().maximize();
        ExecutionLog.log("Window has been maximized to full screen");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 30);
        waitForAlert = new WebDriverWait(driver, 3);

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

            int pageCount = 0;
            for (int i = 0; i < numberOfSubFolders; i++) {
                CSVReader reader = new CSVReader(new FileReader(readApplicationFile("inputPath", path) + File.separator + countryArray[i] + File.separator + "url.csv"));
                String[] nextLine;
                //reads one line at a time
                while ((nextLine = reader.readNext()) != null) {
                    for (String token : nextLine) {
                        String[] countryURLToNavigate = token.split(", ");
                        if (countryURLToNavigate.length > pageCount) {
                            pageCount = countryURLToNavigate.length;
                        }
                        map.put(i, countryArray[i] + "# " + token);
                        System.out.print(token);
                    }
                    System.out.print("\n");

                }
            }

            data = new Object[countryArray.length][pageCount + responsiveDeviceData.size()];
            for (int m = 0; m < map.size(); m++) {
                data[m][0] = map.get(m) + responsiveDataCollection;
            }


//  Original Code
//            data = new Object[countryArray.length][pageCount];
//            for (int m = 0; m < map.size(); m++) {
//                data[m][0] = map.get(m);
//            }


//        Scanner sc = new Scanner(new File(filePath));
//            sc.useDelimiter(", ");   //sets the delimiter pattern
//            int count = 0;
//            data = new Object[2][3];
//            while (sc.hasNext())  //returns a boolean value
//            {
//                String x = sc.next();
//                System.out.print(x);  //find and returns the next complete token from this scanner
//                data[0][count] = x;
//                count++;
//            }
//            sc.close();  //closes the scanner

//                filename = new FileInputStream(filePath);
//                workbook = new XSSFWorkbook(filename);
//                sheet = workbook.getSheetAt(0);
//                Row_count = sheet.getLastRowNum();
//                System.out.println("Row Count: " +Row_count);
//                Col_count = sheet.getRow(0).getLastCellNum();

            //data = new Object[count][3];

//                for(int i = 0; i < count; i++){
//                    String country = sheet.getRow(i).getCell(0).getStringCellValue().trim(); // Country
//                    System.out.println("Country "+country);
//                    System.out.println("");
//                    String [] urlArray = sheet.getRow(i).getCell(1).getStringCellValue().trim().split(", "); // URL
//                    for(int j = 0; j < urlArray.length; j++){
//                        data[i][j] = country + "# "+urlArray[j];
//                        System.out.println("++ "+data[i][j]);
//                        System.out.println("");
//                    }
//                    //data[i][1] = sheet.getRow(i).getCell(1).getStringCellValue().trim(); // URL
//
//                }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //workbook.close();
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

