import Pages.MainPage;
import config.ServerConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.*;

public class openSiteTest {

    protected static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(openSiteTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @BeforeTest
    public void setUp(){
        logger.info("Инициализация теста");

        switch (cfg.browser().toLowerCase()){
            case ("firefox"):                                           //заготовка для расширения пула браузеров
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            default:                                                    //если иного не задано, то запускаем хром
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        logger.info("Запущен драйвер для " + cfg.browser());
    }
    @Test
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
