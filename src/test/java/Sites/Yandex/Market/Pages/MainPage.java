package Sites.Yandex.Market.Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static int timeOutExpWait = 10;

    private static final Logger logger = LogManager.getLogger(MainPage.class);

    private static By allCategoriesButtonXpath = By.xpath("//span[text()='Все категории']/ancestor::button");
    private static String categoryTempTabXpath = "//div[@role='tabpanel']//span[text()='%c']";                          //%c - текст искомой категории, например Электроника
    private static String SectionTempXpath = "//div[@role='tabpanel']//a[text()='%s']";                                 //%s - текст искомого раздела, например Мобильные телефоны
    private static By verticalTabListXpath = By.xpath("//div[@role='tablist' and @aria-orientation='vertical']");       //вертикальное меню категорий
    private static By popupMessageForSearchXpath = By.xpath("//div[@class='popup2__content']/div");
    private static By compareButtonXpath = By.xpath("//div[contains(@data-apiary-widget-id, 'compareButton')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeOutExpWait);
    }

    public CatalogPage goToSection(String category, String section){
        //ждём пока появится всплывашка о ВОЗМОЖНОСТЯХ поиска
        wait
                .until(ExpectedConditions
                        .visibilityOfElementLocated(popupMessageForSearchXpath));

        //ждём пока пропадёт всплывашка о ВОЗМОЖНОСТЯХ поиска
        wait
                .until(ExpectedConditions
                        .invisibilityOfElementLocated(popupMessageForSearchXpath));

        logger.info("Открытие меню Все категории");
        WebElement allCategoriesButton = driver.findElement(allCategoriesButtonXpath);
        wait
                .until(ExpectedConditions
                        .visibilityOf(allCategoriesButton));
        allCategoriesButton.click();

        logger.info("Раскрытие категории \"" + category + "\"");
        wait
                .until(ExpectedConditions
                        .visibilityOf(driver
                                .findElement(verticalTabListXpath)));
        Actions action = new Actions(driver);

        //наводим курсор на выбранную категорию
        action
                .moveToElement(driver
                        .findElement(By
                                .xpath(categoryTempTabXpath
                                        .replace("%c", category))));
        action.perform();
        logger.info("Переход в раздел \"" + section + "\"");

        WebElement elementSection =
                driver
                .findElement(By
                        .xpath(SectionTempXpath
                                .replace("%s", section)));
        wait
                .until(ExpectedConditions
                        .elementToBeClickable(elementSection));

        //переходим в выбранный раздел
        elementSection.click();

        return new CatalogPage(driver);
    }

    public ComparePage openComparePage(WebDriver driver){
        driver.findElement(compareButtonXpath).click();
        return new ComparePage(driver);
    }
}