import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import utility.ExecutionLog;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class W3cValidationErrorCheck extends ParentClass {
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    private static PrintWriter writer;
    static SoftAssert softAssert = new SoftAssert();
    static String path = "config.properties";
    static int errorCounter = 0;
    public static void w3cValidation(String[] countryURL) {
        try {
            String[] splitResponsiveData = countryURL[0].split("@@");
            countryURL[0] = splitResponsiveData[0].trim();
            String[] country = countryURL[0].split("# ");
            System.out.println("Country: " + country[0]);
            directoryPath = ParentClass.readApplicationFile("outputPath", path) + File.separator + "" + country[0] + "" + File.separator + "" + date;
            File dir = new File(directoryPath + "" + File.separator + "W3cValidationSummary");
            dir.mkdir();
            String[] countryURLToNavigate = countryURL[0].replaceAll(country[0] + "# ", "").split(",");
            writer = new PrintWriter(directoryPath + "" + File.separator + "W3cValidationSummary" + File.separator + "W3cValidationReport.txt", "UTF-8");
            for (int x = 0; x < countryURLToNavigate.length; x++) {
                try {
                    writer.println("________________________________________________________________________________________");
                    System.out.println("URL to be hit: " + countryURLToNavigate[x].replace("\"", ""));
                    String newUrl = countryURLToNavigate[x].replace("\"", "");

                    driver.navigate().to("https://validator.w3.org/");
                    new WebDriverWait(driver, 60).until(webDriver ->
                            js.executeScript("return document.readyState").equals("complete"));
                    ExecutionLog.log("Validating the page for W3C error check : " + newUrl);
                    writer.println("Validating the URL page W3C error check  : " + newUrl);
                    driver.findElement(By.xpath("//label[@title='Address of page to Validate']/following-sibling::input[@type='text']")).sendKeys(newUrl);
                    ExecutionLog.log("Entered the standard chartered url for validation into the address bar for validation");
                    driver.findElement(By.xpath("(//a[@class='submit' and text()='Check'])[1]")).click();
                    ExecutionLog.log("Clicked on the check button to validate the standard chartered url address");
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//a[@class='submit' and text()='Check'])[1]")));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='results']")));
                    List<WebElement> elementList = driver.findElements(By.xpath("//div[@id='results']//li"));
                    ExecutionLog.log("There are " + elementList.size() + " results shown after url validation");
                    writer.println("There are " + elementList.size() + " results shown after url validation");

                    for (int i = 1; i <= elementList.size(); i++) {
                        if (driver.findElements(By.xpath("(//div[@id='results']//li[@class='info warning'])[" + i + "]")).size() > 0) {
                            ExecutionLog.log("Warning info displayed");
                            writer.println("Warning info displayed");
                            List<WebElement> elementListWarning = driver.findElements(By.xpath("//div[@id='results']//li[@class='info warning']"));
                            softAssert.assertEquals(elementList, elementListWarning);
                            if (elementList == elementListWarning) {
                                ExecutionLog.log("All the validation results are shown as warning info");
                                writer.println("All the validation results are shown as warning info");
                            }
                            break;
                        }
                    }
                    List<WebElement> errorList = driver.findElements(By.xpath("//div[@id='results']//li[contains(@class,'error')]"));
                    errorCounter = errorCounter + errorList.size();
                    if (errorList.size() > 0) {
                        for (int j = 1; j <= errorList.size(); j++) {
                            String errorMessage = driver.findElement(By.xpath("((//div[@id='results']//li[contains(@class,'error')])[" + j + "]//span)[1]")).getText().trim();
                            ExecutionLog.log("Error message is displayed as \"" + errorMessage + "\"");
                            writer.println("Error message is displayed as \"" + errorMessage + "\"");
                        }
                    }
                } catch (Exception e) {
                    writer.println("Error occur during execution");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            writer.println("Exception occurred in the main try block");
            e.printStackTrace();
        }
        finally {
            writer.println("*************************************************");
            writer.println();
            writer.println("Total error in W3C validation : "+ errorCounter);
            writer.close();
        }
    }
}
