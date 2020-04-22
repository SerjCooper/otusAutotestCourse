import Pages.MainPage;
import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;


@Listeners(ExecutionListener.class)
public class OpenSiteTest {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(OpenSiteTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String browserName;

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

    @Test(description = "Checking logo on main page")
    public void testOTUS(){
        driver.get(cfg.url());
        MainPage mainPage = new MainPage(driver);                        //создаём экземпляр главной страницы
        logger.info("Произошёл переход на " + cfg.url());
        Assert.assertTrue(mainPage.getMainLogo().isDisplayed());         //проверяем, что логотип виден
        logger.info("Найден логотип на главной странице");
    }

    @AfterTest
    public void quit(){
        if(driver != null)
            driver.quit();
        logger.info("Драйвер завершил работу");
    }
}
