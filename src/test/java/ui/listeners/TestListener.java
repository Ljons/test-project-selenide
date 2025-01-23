package ui.listeners;

import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.*;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

@Log4j
public class TestListener implements ITestListener, IInvokedMethodListener {

    @SneakyThrows
    public void onTestFailure(ITestResult result) {
        var msg = String.format("TEST:'%s' - FAILED", desc(result));
        log.error(ExceptionUtils.getStackTrace(result.getThrowable()));
        logMessage(msg);
        attachLog(result);
        var name = String.format("%s - %s", result.getMethod().getMethodName(), result.getMethod().getId());
        if (!result.getTestClass().getName().matches(".*api\\..*")) {
            var alert = getAlert();
            if (alert.isPresent())
                log.error(String.format("Cannot take screenshot. Alert dialog detected: '%s'", alert.get().getText()));
            else
                saveScreenshot(name);
        }
    }

    private Optional<Alert> getAlert() {
        Alert alert = null;
        try {
            Selenide.title();
        } catch (UnhandledAlertException e) {
            alert = Selenide.switchTo().alert();
        }
        return Optional.ofNullable(alert);
    }

    @Override
    public void onTestStart(ITestResult result) {
        var msg = String.format("START: '%s'", desc(result));
        logMessage(msg);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        var msg = String.format("TEST: '%s' - PASSED", desc(result));
        logMessage(msg);
        attachLog(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        var msg = String.format("TEST: '%s' - SKIPPED", desc(result));
        logMessage(msg);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub

    }

    @Attachment(value = "{screenshotName}", type = "image/png")
    private byte[] saveScreenshot(String screenshotName) throws IOException {
        var screenshot = Screenshots.takeScreenShotAsFile();
        return FileUtils.readFileToByteArray(screenshot);
    }

    @Attachment(value = "DETAILS")
    private String attachLog(ITestResult testResult) {
        var out = Reporter.getOutput(testResult);
        var res = "";
        for (var line : out) {
            res += format("%s", line);
        }
        return res;
    }

    private void logMessage(String msg) {
        log.info(StringUtils.repeat('#', msg.length() + 20));
        log.info(String.format("######### %s #########", msg));
        log.info(StringUtils.repeat('#', msg.length() + 20));
    }

    private String desc(ITestResult result) {
        return result.getMethod().getDescription();
    }
}
