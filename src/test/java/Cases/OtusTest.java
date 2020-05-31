package Cases;

import Sites.Otus.Pages.MainPage;
import Utils.Browser;
import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.List;

public class OtusTest {
    private static final Logger logger = LogManager.getLogger(YaMarketTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String browserName = null;                                                             //параметр для прямого указания браузера из теста
    private Browser browser;

    @BeforeTest
    public void setUp() {
        logger.info("Инициализация теста");
        this.browser = new Browser(browserName);
        browser.setUp();
    }

    @Test
    public void FillPersonDataTest(){
        logger.info("Запуск теста сохранения персональных данных в личном кабинете Otus");
        String firstName = "Автотест";
        String secondName = "Автотестов";
        String firstNameLatin = "Autotest";
        String secondNameLatin = "Autotestov";
        String NickName = "Auto_Nick";
        String birthDate =  "01.01.2000";
        String contact_0 =  "https://fb.com/autotest";
        String contact_1 =  "https://vk.com/autotest";

        //Получаем логин и пароль из консоли при запуске
        String login = System.getProperty("otusLogin").replace("'", "");
        String pass = System.getProperty("otusPass").replace("'", "");

        browser.getUrl(cfg.url());
        MainPage mainPage = new MainPage(browser);
        mainPage
                .auth(login, pass)
                .openPersonCabinet()
                .openAboutMePage()
                .fillPersonFields(
                        firstName,
                        secondName,
                        firstNameLatin,
                        secondNameLatin,
                        NickName,
                        birthDate,
                        contact_0,
                        contact_1)
                .saveAndFillLater();

        logger.info("Перезапуск браузера");
        this.quit();
        this.setUp();

        browser.getUrl(cfg.url());
        mainPage = new MainPage(browser);
        List<String> personFields;
        personFields =
                mainPage
                .auth(login, pass)
                .openPersonCabinet()
                .openAboutMePage()
                .getPersonFields();

        logger.info("Проверка заполненности полей");
        Assert.assertTrue(personFields.contains(firstName), "Поле Имя не заполнено");
        Assert.assertTrue(personFields.contains(secondName), "Поле Фамилия не заполнено");
        Assert.assertTrue(personFields.contains(firstNameLatin), "Поле Имя (латиницей) не заполнено");
        Assert.assertTrue(personFields.contains(secondNameLatin), "Поле Фамилия (латиницей) не заполнено");
        Assert.assertTrue(personFields.contains(NickName), "Поле Имя (в блоге) не заполнено");
        Assert.assertTrue(personFields.contains(birthDate), "Поле Дата рождения не заполнено");
    }

    @AfterTest
    public void quit(){
        browser.quit();
        this.browser = null;
    }
}
