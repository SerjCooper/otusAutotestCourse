package Cases;

import Sites.Otus.Pages.MainPage;
import Utils.Browser;
import Utils.WebDriverFactory;
import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


//@Listeners(Utils.ExecutionListener.class)
public class OpenSiteTest {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OpenSiteTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String browserName;                                                             //параметр для прямого указания браузера из теста
    private Browser browser;

    @BeforeTest
    public void setUp() {
        logger.info("Инициализация теста");
        this.browser = new Browser();
        browser.setUp();
    }

    //homework #1
    @Test(description = "Checking logo on main page")
    public void testOTUS(){
        driver.get(cfg.url());
        MainPage mainPage = new MainPage(browser);                        //создаём экземпляр главной страницы
        logger.info("Произошёл переход на " + cfg.url());
        Assert.assertTrue(mainPage.getMainLogo().isDisplayed());         //проверяем, что логотип виден
        logger.info("Найден логотип на главной странице");
    }

    @Test
    public void testGoogle(){
        driver.get("http://www.google.ru");
        WebElement element = driver.findElement(By.xpath("//input[@name='q']"));
        String attr = element.getAttribute("class");
        element.sendKeys("Hello world");
        Dimension elSize = element.getSize();
        Assert.assertTrue(element.isDisplayed());
    }

    @Test
    public void testVote(){
        driver.get(cfg.url());
        WebElement element = driver.findElement(By.xpath("//a[@class='inline-block button button_red-stripe hide-md js-event-click']"));
        System.out.println(element.isDisplayed());
        System.out.println(element.isEnabled());
    }

    @Test
    public void testBootstrap(){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.get("https://ng-bootstrap.github.io/#/components/alert/examples");
        WebElement element = driver.findElement(By.xpath("//button[contains(text(),'Change message')]"));
        element.click();
        WebElement alertBox = driver.findElement(By.xpath("//div[@class='card-body']//ngb-alert[contains(text(), 'Message successfully changed')]"));
        wait.until(ExpectedConditions.visibilityOf(alertBox)); //ждём появления бокса
        String alertText = alertBox.getText();

        wait.until(ExpectedConditions.not(visibilityOf(alertBox))); //ждём его исчезновения

        element.click();
        WebElement alertBox2 = driver.findElement(By.xpath("//div[@class='card-body']//ngb-alert[contains(text(), 'Message successfully changed')]"));
        wait.until(ExpectedConditions.visibilityOf(alertBox2)); //ждём появления бокса
        String alertText2 = alertBox2.getText();
        Assert.assertNotEquals(alertText, alertText2);
    }

    @AfterTest
    public void quit(){
        if(driver != null)
            driver.quit();
        logger.info("Драйвер завершил работу");
    }
}
