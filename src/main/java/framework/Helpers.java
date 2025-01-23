package framework;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class  Helpers {
    @Step("Validate response with JSON Schema with ")
    public static void validateResponse(ValidatableResponse response, String filePath) {
        response
                .assertThat()
                .statusCode(200)
                .log()
                .body()
                .body(matchesJsonSchemaInClasspath(filePath));
    }
}
