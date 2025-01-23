package ui.base;

import com.codeborne.selenide.Selenide;
import core.WebDriverManager;
import helpers.WebPropertiesHelper;
import org.testng.annotations.*;
import ui.listeners.TestListener;
import utils.Logger;

import java.io.File;
import java.util.Objects;

import static core.WebDriverManager.*;
import static enums.WebEnvProperties.STEP_LOGGER_ENABLE;


@Listeners({TestListener.class})
public class BaseTest extends AbstractBaseTest {

    static Logger logger = Logger.get(BaseTest.class);

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        deleteReportFolder();
        WebPropertiesHelper.init();
        WebDriverManager.setupDriver();
        addWebDriverEventListener(Boolean.parseBoolean(STEP_LOGGER_ENABLE.getValue()));
    }

    @AfterTest
    public static void clearBrowserCookiesAndStorage() {
        if (CLEAR_COOKIES) {
            try {
                Selenide.clearBrowserCookies();
                Selenide.clearBrowserLocalStorage();
                Selenide.executeJavaScript("window.sessionStorage.clear()");
            } catch (Exception e) {
                logger.info("SESSION_STORAGE/COOKIES has not been cleared!!!!!");
            }
        }
    }

    @AfterSuite
    public static void closeBrowser() {
        if (!HOLD_BROWSER_OPEN) {
            Selenide.closeWebDriver();
        }
    }

    /**
     * A static initialization block in order to clean the folders with reports and screenshots before build starts
     */
    public void deleteReportFolder() {
        File allureResults = new File("target/allure-results");
        if(allureResults.isDirectory()) {
            for (File item : Objects.requireNonNull(allureResults.listFiles())) {
                item.delete();
            }
        }
        if(CLEAR_REPORTS_DIR) {
            File reports = new File("build/reports/tests/");
            if (reports.isDirectory()) {
                for (File item : Objects.requireNonNull(reports.listFiles())) {
                    item.delete();
                }
            }
        }
        File downloads = new File("builds/downloads/");
        if (downloads.isDirectory()) {
            for(File item : Objects.requireNonNull(downloads.listFiles())) {
                item.delete();
            }
        }
    }
}
