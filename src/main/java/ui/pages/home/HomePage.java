package ui.pages.home;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import ui.models.UserRole;
import ui.pages.base.BasePage;
import java.io.File;
import static com.codeborne.selenide.Selenide.$x;

public class HomePage extends BasePage {

    private final static String PRODUCTS_TAB = "//a[text()='Products']";
    private final static String PROPOSALS_TAB = "//a[text()='Proposals']";
    private final static String IMPORT_XLS_LINK_ID = "importXLSText";
    private final static String IMPORT_XLS_BUTTON_ID = "importXLSButton";
    private final static String IMPORT_XLS_POPUP = "//input[@class='dzu-input']";
    private final static String SUCCESS_UPOAD_POPUP = "//div[@class='NotificationContent']/div[@class='TitleAndButton']";
    private final static String WELCOME_MESSAGE = "//h2[@class='MuiTypography-root UserGreetingText MuiTypography-h1']";

    @Step("Check welcome message for role: {userRole}")
    public void checkWelcomeMessage(UserRole userRole) {
        $x(WELCOME_MESSAGE).shouldHave(Condition.text("Hey, " + userRole.getRole()));
    }

    @Step("Open Products Tab")
    public ProductsPage goToProductsPage() {
        $x(PRODUCTS_TAB).click();
        return new ProductsPage();
    }

    @Step("Open Proposals Tab")
    public ProposalsPage goToProposalsPage() {
        $x(PRODUCTS_TAB).click();
        return new ProposalsPage();
    }

    @Step("Upload file proposal")
    public HomePage uploadXls(String filePath) {
        $id(IMPORT_XLS_BUTTON_ID).click();
        File file = new File(filePath);
        $x(IMPORT_XLS_POPUP).uploadFile(file);
        return this;
    }

    @Step("Verify popup when file proposal is uploaded")
    public void checkFileUploadPopup(String expectedCondition) {
        switch (expectedCondition) {
            case "Success":
                $x(SUCCESS_UPOAD_POPUP).shouldHave(Condition.text("Success"));
                break;
            case "Failed":
                $x(SUCCESS_UPOAD_POPUP).shouldBe(Condition.visible).
                        shouldHave(Condition.text("Oops! Something went wrong"));
                break;
        }
    }
}
