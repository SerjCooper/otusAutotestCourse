package Sites.Yandex.Market.Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class FilterModule {

    protected static WebDriver driver;
    protected WebDriverWait wait;
    protected static int timeOutExpWait = 10;

    private static final Logger logger = LogManager.getLogger(FilterModule.class);

    private String manufactCheckBoxXpath = "//legend[text()='Производитель']/following-sibling::ul//span[text()='%s']/..";   //%s - параметр для указания компании производителя
    private static String sortByTempXpath = "//a[text()='%t']";                                                              //$t - параметр для указания способа сортировки, например "по цене"
    private static By popupMessageForSortXpath = By.xpath("//div[@class='popup2__content']/div");

    public FilterModule(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeOutExpWait);
    }

    public void selectManufacturer(String brandName){
        //отметка одного чекбокса в фильтре "Производитель"
        logger.info("Поиск имени производителя для отметки: " + brandName);
        driver
                .findElement(By
                        .xpath(manufactCheckBoxXpath
                                .replace("%s", brandName)))
                .click();
    }

    public void selectManufacturer(List<String> brandNames){
        //отметка нескольких чекбоксов в фильтре "Производитель"
        for(String b : brandNames){
            selectManufacturer(b);
        }
    }

    public void sortBy(String sorter) {
        logger.info("Сортировка " + sorter);
        driver.findElement(By.xpath(sortByTempXpath.replace("%t", sorter))).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(popupMessageForSortXpath));
    }


}
