package Cases;

import Sites.Yandex.Market.Pages.*;
import Utils.WebDriverFactory;
import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.TimeUnit.SECONDS;

//homework #3
public class YaMarketTest {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(YaMarketTest.class);
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
        driver.manage().timeouts().implicitlyWait(cfg.timeout_implicitly(), SECONDS);
    }

    @Test
    public void testYaMarketMobileConfigOS(){
        //-----Задаём входные параметры для теста
        String expectedFeature = "операционная система";
        String category = "Электроника";
        String section = "Мобильные телефоны";

        List<String> brandNames = new ArrayList<>();
        brandNames.add("Xiaomi");
        brandNames.add("Honor");
        //-----Конец входных параметров

        driver.get(cfg.urlYaMarket());
        MainPage mainPage = new MainPage(driver);

        //Открываем весь каталог, переходим в требуемую категорию и раздел
        CatalogPage catalog = mainPage.goToSection(category, section);

        FilterModule filterModule = catalog.getFilterModule();
        //отмечаем чекбоксы в фильтре "производитель", можно передавать одно значение или List
        filterModule.selectManufacturer(brandNames);

        //задаём критерий сортировки
        filterModule.sortBy("по цене");

        //Добавляем первые найденные товары. Вместо brandNames можно передать список конкретных моделей
        catalog.addFirstToCompareList(brandNames);
        ComparePage comparePage = mainPage.openComparePage(driver);

        //Проверяем, что на странице сравнения товаров столько сколько было отмечено в каталоге
        Assert.assertEquals(comparePage.getProductCount(), brandNames.size());

        //Нажимаем на кнопку "Показывать: все характеристики"
        comparePage.showAllFeatures();
        logger.info("Проверка отображения характеристики: " + expectedFeature);
        Assert.assertTrue(comparePage.getFeatureTitles().contains(expectedFeature));

        //Нажимаем на кнопку "Показывать: различающиеся характеристики"
        comparePage.showDiffFeatures();
        logger.info("Проверка отсутствия характеристики: " + expectedFeature);
        Assert.assertFalse(comparePage.getFeatureTitles().contains(expectedFeature));
    }

    @AfterTest
    public void quit(){
        if(driver != null)
            driver.quit();
        logger.info("Драйвер завершил работу");
    }
}
