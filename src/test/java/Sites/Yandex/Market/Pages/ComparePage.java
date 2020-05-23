package Sites.Yandex.Market.Pages;

import Sites.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ComparePage extends BasePage {
    protected WebDriverWait wait;
    protected static int timeOutExpWait = 10;

    private static final Logger logger = LogManager.getLogger(ComparePage.class);

    private final By compareContentXpath = By.xpath("//a[contains(@class, 'n-compare-head__name')]");
    private final By showAllFeaturesButtonXpath = By.xpath("//span[contains(@class, 'n-compare-show-controls__all')]");
    private final By showDiffFeaturesButtonXpath = By.xpath("//span[contains(@class, 'n-compare-show-controls__diff')]");
    private final By featureRowXpath = By.xpath("//div[contains(@class, 'n-compare-row-name')]");
    private final By GeneralFeaturesGroupTitleXpath = By.xpath("//div[text()='Общие характеристики']");

    public ComparePage(WebDriver driver){
        super(driver);
        this.wait = new WebDriverWait(driver, timeOutExpWait);
    }

    public int getProductCount(){
        logger.info("Получение количества товаров представленных к сравнению");
        return driver.findElements(compareContentXpath).size();
    }

    public void showAllFeatures(){
        logger.info("Включение отображения всех характеристик");
        driver.findElement(showAllFeaturesButtonXpath).click();

        //Ждём пока обновится список характеристик
        wait.until(ExpectedConditions.visibilityOfElementLocated(GeneralFeaturesGroupTitleXpath));
    }

    public void showDiffFeatures(){
        logger.info("Включение отображения отличающихся характеристик");
        driver.findElement(showDiffFeaturesButtonXpath).click();

        //Ждём пока обновится список характеристик
        wait.until(ExpectedConditions.visibilityOfElementLocated(GeneralFeaturesGroupTitleXpath));
    }

    public List<String> getFeatureTitles(){
        logger.info("Получение заголовков отображаемых характеристик");

        //получаем вебэлементы всех характеристик
        List<WebElement> featureRows = driver.findElements(featureRowXpath);
        List<String> featureRowsTitle = new ArrayList<>();

        //забираем название всех характеристик и кладём в list
        for(WebElement e : featureRows){
            featureRowsTitle.add(e.getText().toLowerCase());
        }
        logger.info("Всего характеристик отображается: " + featureRowsTitle.size());

        return featureRowsTitle;
    }
}
