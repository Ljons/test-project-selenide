package ui.common;

import com.codeborne.selenide.Selenide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;

import static core.WebDriverManager.CLEAR_COOKIES;
import static core.WebDriverManager.HOLD_BROWSER_OPEN;


public class CommonActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonActions.class);

    @AfterTest
    public static void clearBrowserCookiesAndStorage() {
        if (CLEAR_COOKIES) {
            try {
                Selenide.clearBrowserCookies();
                Selenide.clearBrowserLocalStorage();
                Selenide.executeJavaScript("window.sessionStorage.clear()");
            } catch (Exception e) {
                LOGGER.info("SESSION_STORAGE/COOKIES has not been cleared!!!!!");
            }
        }
    }

    @AfterSuite
    public static void closeBrowser() {
        if (!HOLD_BROWSER_OPEN) {
            Selenide.closeWebDriver();
        }
    }

}