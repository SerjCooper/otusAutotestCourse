package Sites.Otus.Pages;

import Sites.BasePage;
import Utils.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;


public class aboutMePage extends BasePage {

    private Browser browser;
    private static final Logger logger = LogManager.getLogger(aboutMePage.class);

    private final By firstNameXpath = By.xpath("//input[@id='id_fname']");
    private final By firstNameLatinXpath = By.xpath("//input[@id='id_fname_latin']");
    private final By secondNameXpath = By.xpath("//input[@id='id_lname']");
    private final By secondNameLatinXpath = By.xpath("//input[@id='id_lname_latin']");
    private final By blogNameXpath = By.xpath("//input[@id='id_blog_name']");
    private final By birthDateXpath = By.xpath("//input[@name='date_of_birth']");
    private final By saveAndFillLaterButtonXpath = By.xpath("//button[@title='Сохранить и заполнить позже']");
    private final By contactXpath_0 = By.xpath("//input[@id='id_contact-0-value']");
    private final By contactXpath_1 = By.xpath("//input[@id='id_contact-1-value']");

    public aboutMePage(Browser browser) {
        super(browser.getDriver());
        this.browser = browser;
    }

    public aboutMePage fillPersonFields(String firstName, String secondName, String firstNameLatin, String secondNameLatin, String NickName, String birthDate, String contact_0, String contact_1){
        clearAndFillField(driver.findElement(firstNameXpath), firstName);
        clearAndFillField(driver.findElement(firstNameLatinXpath), firstNameLatin);
        clearAndFillField(driver.findElement(secondNameXpath), secondName);
        clearAndFillField(driver.findElement(secondNameLatinXpath), secondNameLatin);
        clearAndFillField(driver.findElement(blogNameXpath), NickName);
        clearAndFillField(driver.findElement(birthDateXpath), birthDate);
        clearAndFillField(driver.findElement(contactXpath_0), contact_0);
        clearAndFillField(driver.findElement(contactXpath_1), contact_1);
        return this;
    }

    public List<String> getPersonFields(){
        List<String> personFields = new ArrayList<>();
        logger.info("Получение заполненных персональных данных");
        personFields.add(driver.findElement(firstNameXpath).getAttribute("value"));
        personFields.add(driver.findElement(firstNameLatinXpath).getAttribute("value"));
        personFields.add(driver.findElement(secondNameXpath).getAttribute("value"));
        personFields.add(driver.findElement(secondNameLatinXpath).getAttribute("value"));
        personFields.add(driver.findElement(blogNameXpath).getAttribute("value"));
        personFields.add(driver.findElement(birthDateXpath).getAttribute("value"));
        return personFields;
    }

    public aboutMePage saveAndFillLater(){
        logger.info("Нажатие на кнопку \"Сохранить и заполнить позже\"");
        driver.findElement(saveAndFillLaterButtonXpath).click();
        return this;
    }

    private void clearAndFillField(WebElement element, String str){
        Actions actions = new Actions(driver);
        logger.info("Очистка и заполнение поля " + element.getAttribute("name"));
        actions
                .click(element)
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(str)
                .perform();
    }
}
