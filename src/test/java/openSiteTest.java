import config.ServerConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class openSiteTest {

    protected  static WebDriver driver;
    private Logger logger = LogManager.getLogger(openSiteTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @BeforeTest
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
    }
    @Test
    public void testOTUS(){
        driver.get("http://otus.ru");
        System.out.print(driver.getTitle() + "============");
    }

    @AfterTest
    public void quit(){
        if(driver != null)
            driver.quit();
    }
}
