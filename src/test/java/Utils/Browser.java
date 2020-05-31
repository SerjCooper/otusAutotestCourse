package Utils;

import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Browser {

    protected WebDriver driver;
    protected String browserName;

    private static final Logger logger = LogManager.getLogger(Browser.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    public Browser(String browserName){
        this.browserName = browserName;
        init();
    }

    public Browser(){
        init();
    }

    //инициализация по умолчанию из параметров
    private void init(){

        logger.info("Инициализация браузера");
        if (browserName == null)
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

    public void setUp() throws NullPointerException {
        logger.info("Запуск браузера");

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
        driver.manage().timeouts().implicitlyWait(cfg.timeout_implicitly(), SECONDS);
    }

    public void getUrl(String url){
        driver.get(url);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quit(){
        if(driver != null) {
            driver.quit();
            driver = null;
        }
        logger.info("Браузер завершил работу");
    }

    public void close(){
        if(driver != null)
            driver.close();
        logger.info("Закрыто окно браузера");
    }
}
