package ui.common;

import io.qameta.allure.Step;
import ui.pages.login.LoginPage;

import static com.codeborne.selenide.AuthenticationType.BASIC;
import static com.codeborne.selenide.Selenide.open;
import static enums.WebEnvProperties.*;

public class UserActions {

    @Step("Open main website page")
    public static LoginPage openMainPage() {
        open(ENV_URL.getValue(), BASIC, MAIN_USER.getValue(),  MAIN_USER_PASSWORD.getValue());
        return new LoginPage();
    }

}
