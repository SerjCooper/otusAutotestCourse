package Sites.Yandex.Market.Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class CatalogPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static int timeOutExpWait = 10;
    protected FilterModule filterModule;

    private static final Logger logger = LogManager.getLogger(CatalogPage.class);

    private static String catalogCell = "//div/a[contains(@title, '%n')]";                //%n - параметр имени бренда для поиска по каталогу, например Honor
    private static String catalogCellCompareButton = "//div[contains(@data-bem, '%n')]";                //%n - параметр имени бренда для добавления к сравнению, например Honor
    private static By popupCompareCloseButtonXpath = By.xpath("//div[contains(@class, 'image_name_close')]");
    private static By popupCompareTitleXpath = By.xpath("//div[@class='popup-informer__title']");

    public CatalogPage(WebDriver driver){
        this.driver = driver;
        this.filterModule = new FilterModule(driver);
        this.wait = new WebDriverWait(driver, timeOutExpWait);
    }

    public FilterModule getFilterModule() {
        return filterModule;
    }

    public void addFirstToCompareList(List<String> modelName){
        for (String m : modelName) {
            addFirstToCompareList(m);
        }
    }

    public void addFirstToCompareList(String modelName){
        logger.info("Добавление к сравнению " + modelName);

        //Хитрость: скролим страницу в начало, чтобы не перекрывать кнопку сравнения
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,0)");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        Actions actions = new Actions(driver);
        By modelLocator;
        String comparePopupText;

        //Кнопка "Добавить к сравнению" появится только если на неё навестись
        actions
                .moveToElement(driver
                        .findElement(By
                                .xpath(catalogCell
                                        .replace("%n", modelName))));
        actions.perform();

        modelLocator = By.xpath(catalogCellCompareButton.replace("%n", modelName));

        //Ждём пока кнопка добавления к сравнению станет видима, а затем кликаем по ней
        wait.until(ExpectedConditions.visibilityOfElementLocated(modelLocator));
        driver.findElement(modelLocator).click();

        //Ждём всплывашки о том, что товар добавлен к сравнению
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupCompareTitleXpath));
        comparePopupText = driver.findElement(popupCompareTitleXpath).getText();
        logger.info("Проверка появления всплывашки добавления товара к сравнению");
        Assert.assertTrue(
                comparePopupText.contains("добавлен к сравнению") && comparePopupText.contains(modelName));
    }
}
