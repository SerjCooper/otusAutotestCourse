package Sites.Otus.Pages;

import Sites.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage extends BasePage {

    private static By mainLogoXpath = By.xpath("//a[@class='header2__logo-img']");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getMainLogo(){
        return driver.findElement(mainLogoXpath);
    }
}
