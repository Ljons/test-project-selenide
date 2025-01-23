package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import utils.Logger;

import java.net.URI;
import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static enums.EnvProperties.CONTAINER_ENGINE;
import static enums.WebEnvProperties.BROWSER;
import static enums.WebEnvProperties.BROWSER_VERSION;
import static java.lang.String.format;


public class WebDriverManager {
    private static WebDriver driver;
    public static boolean HOLD_BROWSER_OPEN = false;
    public static boolean CLEAR_COOKIES = true;
    public static final boolean CLEAR_REPORTS_DIR = true;


    public static void setupDriver() {

        if (Boolean.parseBoolean(CONTAINER_ENGINE.getValue())) {
            Configuration.browser = BROWSER.getValue();
            DesiredCapabilities browser = new DesiredCapabilities();
            browser.setBrowserName(BROWSER.getValue());
            browser.setVersion(BROWSER_VERSION.getValue());
            browser.setCapability("enableVNC", true);
            try {
                driver = new RemoteWebDriver(URI.create("http://0.0.0.0:4444/wd/hub").toURL(), browser);
            } catch (Exception e) {
                Logger.log(e.getMessage());
                throw new Error("Failed to init webDriver");
            }
            setWebDriver(driver);
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            Configuration.browser = BROWSER.getValue();
            Configuration.browserSize = "1920x1080";
            Configuration.holdBrowserOpen = HOLD_BROWSER_OPEN;
            Configuration.reportsFolder = "builds/reports/tests";
            Configuration.timeout = 5000;
        }
    }

    public static void addWebDriverEventListener() {
        Logger logger = Logger.get("Selenide Listener");
        WebDriverRunner.addListener(new AbstractWebDriverEventListener() {

            public void beforeClickOn(WebElement element, WebDriver driver) {
                logger.info(format("Clicking on {%s}", $(element).toString()));
            }

            public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
                if (keysToSend == null) logger.info(format("Clear input in {%s}.", $(element).toString()));
                else logger.info(format("Input text {%s} in {%s}.", Arrays.toString(keysToSend), $(element).toString()));
            }
        });
    }

    public static void addWebDriverEventListener(Boolean isEnabled) {
        if (isEnabled) {
            addWebDriverEventListener();
        }

    }
}
