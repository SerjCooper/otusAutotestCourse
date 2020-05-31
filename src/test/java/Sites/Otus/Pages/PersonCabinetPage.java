package Sites.Otus.Pages;

import Sites.BasePage;
import Utils.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class PersonCabinetPage extends BasePage {

    private Browser browser;

    private static final Logger logger = LogManager.getLogger(PersonCabinetPage.class);

    private final By aboutMeButtonXpath = By.xpath("//a[contains(text(), 'О себе')]");

    public PersonCabinetPage(Browser browser) {
        super(browser.getDriver());
        this.browser = browser;
    }

    public aboutMePage openAboutMePage() {
        logger.info("Переход на страницу \"О себе\"");
        driver.findElement(aboutMeButtonXpath).click();
        return new aboutMePage(browser);
    }

}
