import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utility.ExecutionLog;

import java.util.List;

public class W3cValidationErrorCheck extends ParentClass{
    public static JavascriptExecutor js = (JavascriptExecutor) driver;
    public static void w3cValidation(){
        driver.navigate().to("https://validator.w3.org/");
        new WebDriverWait(driver, 60).until(webDriver ->
                js.executeScript("return document.readyState").equals("complete"));
        ExecutionLog.log("Navigated to the URL successfully : https://validator.w3.org/");
        driver.findElement(By.xpath("//label[@title='Address of page to Validate']/following-sibling::input[@type='text']"))
                .sendKeys("https://www.sc.com/sg/");
        ExecutionLog.log("Entered the standard chartered url for validation into the address bar for validation");
        driver.findElement(By.xpath("(//a[@class='submit' and text()='Check'])[1]")).click();
        ExecutionLog.log("Clicked on the check button to validate the standard chartered url address");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//a[@class='submit' and text()='Check'])[1]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='results']")));
        List<WebElement> elementList = driver.findElements(By.xpath("//div[@id='results']//li"));
        ExecutionLog.log("There are "+elementList.size()+" results shown after url validation");
        for(int i = 1; i <= elementList.size(); i++){
            Assert.assertTrue(driver.findElement(By.xpath("(//div[@id='results']//li[@class='info warning'])["+i+"]")).isDisplayed());
            ExecutionLog.log("Warning info is displayed");
        }
        List<WebElement> elementListWarning = driver.findElements(By.xpath("//div[@id='results']//li[@class='info warning']"));
        Assert.assertEquals(elementList, elementListWarning);
        ExecutionLog.log("All the validation results are shown as warning info");
    }
}
