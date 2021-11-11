import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.ExecutionLog;

public class JsValidationCheck extends ParentClass{
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    public static void jsValidation() {
        driver.navigate().to("https://seositecheckup.com/analysis");
        new WebDriverWait(driver, 60).until(webDriver ->
                js.executeScript("return document.readyState").equals("complete"));
        ExecutionLog.log("Navigated to the URL successfully : https://seositecheckup.com/analysis");
        driver.findElement(By.xpath("(//input[@placeholder='Website URL'])[1]")).sendKeys("https://www.sc.com/sg/");
        ExecutionLog.log("Entered the standard chartered url for js validation into the address bar for validation");
        driver.findElement(By.xpath("(//button[@type='submit' and text()='Checkup'])[1]")).click();
        ExecutionLog.log("Clicked on the check button to validate the standard chartered url address");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h2[text()='Loading results']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//div[@class='card-header']//div[@class='ant-progress-inner'])[1]")));
        ExecutionLog.log("Progress bar under the card header is complete");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='report-summary']")));
        ExecutionLog.log("Report summary has been created successfully");
        String jsFailedCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item failed']//span")).getText();
        ExecutionLog.log("JS failed count : "+jsFailedCount);
        String jsWarningCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item warning']//span")).getText();
        ExecutionLog.log("JS warning count : "+jsWarningCount);
        String jsPassedCount = driver.findElement(By.xpath("(//div[@class='summary-graph-split'])[1]//div[@class='progress-item passed']//span")).getText();
        ExecutionLog.log("JS passed count : "+jsPassedCount);
    }
}
