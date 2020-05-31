package Cases;

import Sites.Yandex.Market.Pages.*;
import Utils.Browser;
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

    private static final Logger logger = LogManager.getLogger(YaMarketTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String browserName;                                                             //параметр для прямого указания браузера из теста
    private Browser browser;

    @BeforeTest
    public void setUp() {
        logger.info("Инициализация теста");
        this.browser = new Browser();
        browser.setUp();
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

        browser.getUrl(cfg.urlYaMarket());
        MainPage mainPage = new MainPage(browser);

        //Открываем весь каталог, переходим в требуемую категорию и раздел
        CatalogPage catalog = mainPage.goToSection(category, section);

        FilterModule filterModule = catalog.getFilterModule();
        //отмечаем чекбоксы в фильтре "производитель", можно передавать одно значение или List
        filterModule.selectManufacturer(brandNames);

        //задаём критерий сортировки
        filterModule.sortBy("по цене");

        //Добавляем первые найденные товары. Вместо brandNames можно передать список конкретных моделей
        catalog.addFirstToCompareList(brandNames);
        ComparePage comparePage = mainPage.openComparePage(browser);

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
        browser.quit();
    }
}
