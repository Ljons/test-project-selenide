package ui.base;

import org.testng.IConfigurable;
import org.testng.IConfigureCallBack;
import org.testng.ITestResult;
import utils.Logger;
import utils.RetryRunner;
import utils.Utils;

import static java.util.Objects.isNull;
import static utils.Utils.createDirectory;

public abstract class AbstractBaseTest implements IConfigurable {
    @Override
    public void run(IConfigureCallBack callBack, ITestResult testResult) {
        int attempts = RetryRunner.getMaxRetry();
        for (int attempt = 0; attempt <= attempts; attempt++) {
            callBack.runConfigurationMethod(testResult);
            if (attempt != 0) {
                Logger.log("Rerunning configuration method " + testResult.getMethod().getMethodName());
                createDirectory(System.getProperty("user.dir") + "/build/reports/tests");
                Utils.grabScreenshot("SetUpFailed_" + testResult.getMethod().getMethodName());
            }
            if (isNull(testResult.getThrowable())) {
                break;
            } else {
                testResult.getThrowable().printStackTrace();
            }
        }
    }
}