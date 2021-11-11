import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utility.ExecutionLog;

import java.util.HashMap;

public class ResponsiveDimensionCheck extends ParentClass{

    public static void responsiveDimensionValidation(){
        driver.navigate().to("https://www.sc.com/sg/");
        Dimension dimension = new Dimension(400,600);
        ExecutionLog.log("Current window has been resized to the dimension : 400*600");
        driver.manage().window().setSize(dimension);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'hamburger only-mobile')]")));
        driver.findElement(By.xpath("//button[contains(@class,'hamburger only-mobile')]")).click();
        ExecutionLog.log("Clicked on the hamburger menu of mobile view");
        driver.findElement(By.xpath("//ul[contains(@class,'menu-visible')]")).isDisplayed();
        ExecutionLog.log("Menu list is visible");
        driver.findElement(By.xpath("//button[contains(@class,'close-button sc-menu-visible')]")).isDisplayed();
        driver.findElement(By.xpath("//button[contains(@class,'close-button sc-menu-visible')]")).click();
        ExecutionLog.log("Menu list close button is clicked");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[contains(@class,'menu-visible')]")));
        ExecutionLog.log("Menu list is closed successfully");
    }
}
