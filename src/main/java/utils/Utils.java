package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestResult;
import org.testng.SkipException;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

import static enums.EnvProperties.*;
import static java.util.Objects.nonNull;

public class Utils {
    private static Logger logger = Logger.get(Utils.class);
    public static final String DEFAULT_ENV = "qa";

    public static <T> T parseJson(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, clazz);
    }

    public static <T> T parseJson(String json, TypeToken<T> clazz) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, clazz.getType());
    }

    public static String parseJsonFile(String filePath, String key) {
        JSONParser parser = new JSONParser();
        String value = "";
        try {
            Object obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;
            value = (String) jsonObject.get(key);

        } catch (Exception e) {
            e.getMessage();
        }
        return value;
    }

    public static String getTestRailSuiteName(Class<?> clazz) {
        Epic annotation = clazz.getAnnotation(Epic.class);
        if (nonNull(annotation)) {
            return annotation.value();
        } else {
            return null;
        }
    }

    public static void waitForThreadsToFinish(Thread... threads) {
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static String getPropertyEnv() {
        String env = nonNull(TEST_ENV.getValue()) ? TEST_ENV.getValue() : TEST_ENV.getName();
        if (env.contains("qa"))
            return "qa";
        else if (env.contains("dev"))
            return "dev";
        else
            return env.equals("") ? DEFAULT_ENV : env;
    }

    public static String getEnv() {
        String env = nonNull(TEST_ENV.getValue()) ? TEST_ENV.getValue() : TEST_ENV.getName();
        if (env.contains("qa"))
            return "qa";
        else if (env.contains("dev"))
            return "dev";
        else if (env.contains("stage"))
            return "stage";
        else if (env.contains("local"))
            return "local";
        else
            return env.equals("") ? DEFAULT_ENV : env;
    }

    public static String getServicesEnv() {
        return nonNull(SERVICES_ENV.getValue()) ? SERVICES_ENV.getValue() : SERVICES_ENV.getName();
    }

    public static String getWebEnv() {
        return nonNull(WEB_ENV.getValue()) ? WEB_ENV.getValue() : WEB_ENV.getName();
    }

    public static String getJiraItem(ITestResult iTestResult) {
        Issue jiraItem = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Issue.class);

        if (Objects.isNull(jiraItem)) {
            return null;
        } else {
            return jiraItem.value();
        }
    }

    public static int[] getTestRailIds(ITestResult iTestResult) {
        TmsLink tmsLinkAnnotation = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TmsLink.class);
        TmsLinks tmsLinksAnnotation = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TmsLinks.class);
        if (Objects.nonNull(tmsLinkAnnotation)) {
            return new int[]{Integer.parseInt(tmsLinkAnnotation.value())};
        }
        if (Objects.nonNull(tmsLinksAnnotation)) {
            return Arrays.stream(tmsLinksAnnotation.value()).mapToInt(v -> Integer.parseInt(v.value())).toArray();
        }
        return null;
    }

    public static void skipIfJiraItemExist(ITestResult iTestResult) {
        String jiraItem = getJiraItem(iTestResult);
        if (nonNull(jiraItem)) {
            String skippedMsg = "Test skipped due to Jira Item " + jiraItem;
            Logger.log(skippedMsg);
            iTestResult.setStatus(ITestResult.SKIP);
            throw new SkipException(jiraItem);
        }
    }

    public static Integer getRandomInteger(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.random(length, true, false);
    }

    public static String getRandomString() {
        return getRandomString(9);
    }

    public static int getAbsRandomInt() {
        return Math.abs(new Random().nextInt());
    }

    public static Double getRandomDouble() {
        return new Random().nextDouble();
    }

    public static Float getRandomFloat() {
        return new Random().nextFloat();
    }

    public static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    @Nullable
    public static String getFilePathFromResources(Class<?> clazz, String relativePath) {
        URL sourceUrl = clazz.getResource(relativePath);
        String sourceFilePath = null;
        try {
            sourceFilePath = Paths.get(sourceUrl.toURI()).toFile().getAbsolutePath();
        } catch (URISyntaxException e) {
            logger.info("Wrong URI syntax: " + e.getMessage());
        }
        return sourceFilePath;
    }

    public static void grabScreenshot(String name)
    {
        try {
            Robot robot = new Robot();
            String format = "png";

            String fileName = System.getProperty("user.dir") + "/build/reports/tests/" + name + System.currentTimeMillis() + ".png";
            Rectangle captureRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
            ImageIO.write(screenFullImage, format, new File(fileName));
            Logger.log("Screenshot saved at: " + fileName);
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }

    public static void createDirectory(String folderName)
    {
        File theDir = new File(folderName);
        if (!theDir.exists()) {
            Logger.log("creating directory: " + theDir.getName());

            try {
                if (theDir.mkdirs())
                    Logger.log(folderName + " folder created");
            } catch (Exception e) {
                Logger.log("Failed creating directory: " + theDir.getName() + " with Error: " + e.getMessage());
            }
        }
    }
}
