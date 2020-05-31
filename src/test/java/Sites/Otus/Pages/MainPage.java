package Sites.Otus.Pages;

import Sites.BasePage;
import Utils.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends BasePage {

    private Browser browser;
    private WebDriverWait wait;

    private static final Logger logger = LogManager.getLogger(MainPage.class);

    private final By mainLogoXpath = By.xpath("//a[@class='header2__logo-img']");
    private final By loginButtonXpath = By.xpath("//button[contains(@class, 'header2__auth')]");
    private final By emailLoginXpath = By.xpath("//form[contains(@class, 'new-log-reg__form')]//input[@placeholder='Электронная почта']");
    private final By passLoginXpath = By.xpath("//input[@placeholder='Введите пароль']");
    private final By submitLoginPassButton = By.xpath("//button[@class='new-button new-button_full new-button_blue new-button_md']");
    private final By userNameXpath = By.xpath("//div[contains(@class,'header2-menu__item-wrapper__username')]");
    private final By personCabinetXpath = By.xpath("//div[contains(@class,'header2-menu__item_dropdown')]//a[@title='Личный кабинет']");

    public MainPage(Browser browser) {
        super(browser.getDriver());
        this.browser = browser;
    }

    public MainPage auth(String login, String pass) {
        logger.info("Прохождение авторизации");
        driver.findElement(loginButtonXpath).click();
        driver.findElement(emailLoginXpath).sendKeys(login);
        driver.findElement(passLoginXpath).sendKeys(pass);
        driver.findElement(submitLoginPassButton).click();
        return this;
    }

    public PersonCabinetPage openPersonCabinet(){
        logger.info("Переход в личный кабинет");
        wait = new WebDriverWait(driver, 10);

        Actions actions = new Actions(driver);
        actions
                .moveToElement(driver.findElement(userNameXpath))
                .perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(personCabinetXpath));
        driver.findElement(personCabinetXpath).click();
        return new PersonCabinetPage(browser);
    }

    public WebElement getMainLogo(){
        return driver.findElement(mainLogoXpath);
    }
}
