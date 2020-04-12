package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage {

    protected WebDriver driver;

    private static By mainLogoXpath = By.xpath("//a[@class='header2__logo-img']");

    public MainPage(WebDriver driver){
        this.driver = driver;
    }

    public WebElement getMainLogo(){
        return driver.findElement(mainLogoXpath);
    }
}
