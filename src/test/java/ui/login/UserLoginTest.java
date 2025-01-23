package ui.login;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import ui.base.BaseTest;
import ui.common.UserActions;

@Feature("User login")
public class UserLoginTest extends BaseTest {

    @Test(description = "Login test for Buyer role", groups = "Smoke")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyWelcomeMessageForBuyer() {
        UserActions.openMainPage()
                .clickOnTermsAndConditions()
                .clickOnLogin()
                .beginAdventureLoop();
    }
}
