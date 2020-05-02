import Sites.Otus.Pages.MainPage;
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


//@Listeners(ExecutionListener.class)
public class OpenSiteTest {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OpenSiteTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String browserName;

    //homework #2
    @BeforeSuite
    public void browserInit(){
        logger.info("Инициализация Suite");
        browserName = System.getProperty("browser").replace("'", "");   //забираем параметр введенный через maven
        browserName = browserName.toUpperCase();
        logger.info(browserName);

        if(browserName.isEmpty())                                                   //браузер устанавливается в порядке приоритета:
            if(!cfg.browser().isEmpty())                                            //1. параметра в консоли
                browserName = cfg.browser();                                        //2. параметра в Pom.xml
            else{                                                                   //3. config.properties
                logger.error("Имя браузера не задано");                          //и если нигде мы не нашли название браузера, кидаем исключение
                throw new NullPointerException("Имя браузера не задано");
            }
    }

    //homework #2
    @BeforeTest
    public void setUp() throws NullPointerException {
        logger.info("Инициализация теста");

        switch (browserName){
            case ("FIREFOX"):
             //   FirefoxOptions firefoxOptions = new FirefoxOptions();
             //   firefoxOptions.addArguments("--incognito");                              //здесь будут какие либо опции
                driver = WebDriverFactory.create(browserName);
                break;
            default:
                ChromeOptions chromeOptions = new ChromeOptions();                    //для включения ChromeOptions раскоменть это
                chromeOptions.addArguments("disable-extensions");
                driver = WebDriverFactory.create(browserName, chromeOptions);
                break;
        }
        driver.manage().window().maximize();
    }

    //homework #1
    @Test(description = "Checking logo on main page")
    public void testOTUS(){
        driver.get(cfg.url());
        MainPage mainPage = new MainPage(driver);                        //создаём экземпляр главной страницы
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
