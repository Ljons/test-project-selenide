package ui.uploadFileProposal;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.base.BaseTest;
import ui.common.UserActions;
import ui.models.UserRole;


@Feature("Upload XLS file")
public class UploadFileProposalTest extends BaseTest {

    String UPLOAD_XLS_FILE_PATH = "src/test/resources/test_data/Coca_Cola_1_5L.xls";
    String UPLOAD_XLSX_FILE_PATH = "src/test/resources/test_data/Coca_Cherry.xlsx";
    String UPLOAD_CSV_FILE_PATH = "src/test/resources/test_data/06.Proforma_template 29127870.csv";



    @DataProvider(name = "FilesToUploadDataProvider")
    public Object[][] FilesToUpload() {
        return new Object[][]{
                {UPLOAD_XLS_FILE_PATH},
                {UPLOAD_XLSX_FILE_PATH}
        };
    }

    @Test(description = "Check success file upload", groups = "Smoke", dataProvider = "FilesToUploadDataProvider")
    @Severity(SeverityLevel.CRITICAL)
    public void uploadProposalFile(String filePath) {
        UserActions.openMainPage()
                .selectUserRole(UserRole.BUYER);

    }

    @Test(description = "Check validation for .csv format", groups = "Smoke")
    @Severity(SeverityLevel.CRITICAL)
    public void uploadCsvFile() {
        UserActions.openMainPage()
                .selectUserRole(UserRole.BUYER);

    }

}
