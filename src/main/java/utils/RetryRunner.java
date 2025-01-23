package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryRunner implements IRetryAnalyzer
{
    private int count = 0;
    private int maxTry = 2;

    @Override
    public boolean retry(ITestResult iTestResult)
    {
        if (count < maxTry) {
            count++;
            Logger.log("Retrying test " + iTestResult.getName() + " with status " + getResultStatusName(iTestResult.getStatus()) + " for the " + (count) + " time(s).");
            return true;
        }
        return false;
    }

    public boolean isRetryAvailable()
    {
        return count < maxTry;
    }

    private String getResultStatusName(int status)
    {
        String resultName = null;
        if (status == 1) resultName = "SUCCESS";
        if (status == 2) resultName = "FAILURE";
        if (status == 3) resultName = "SKIP";
        return resultName;
    }

    public static int getMaxRetry()
    {
        return new RetryRunner().maxTry;
    }

}
