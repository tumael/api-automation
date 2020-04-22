package org.automation.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.automation.JsonHelper;
import org.automation.RequestSpecFactory;
import org.automation.ScenarioContext;
import org.automation.api.DynamicIdHelper;
import org.automation.api.RequestManager;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class RequestSteps {
    private Response response;
    private ScenarioContext context;

    public RequestSteps(final ScenarioContext context) {
        this.context = context;
    }

    @Given("I use the {string} service")
    public void iUseTheService(final String serviceName) {
        RequestSpecification requestSpecification = RequestSpecFactory.getRequestSpec(serviceName);
        context.set("REQUEST_SPEC", requestSpecification);
    }

    @Given("I send a {string} request to {string} with json body")
    public void iSendARequestToWithJsonBody(final String httpMethod, final String endpoint,
                                            final String jsonBody) {
        RequestSpecification requestSpecification = (RequestSpecification) context.get("REQUEST_SPEC");
        String builtEndpoint = DynamicIdHelper.buildEndpoint(context, endpoint);
        response = RequestManager.doRequest(httpMethod, requestSpecification, builtEndpoint,
                DynamicIdHelper.replaceIds(context, jsonBody));
        context.set("LAST_ENDPOINT", builtEndpoint);
        context.set("LAST_RESPONSE", response);
    }

    @When("I send a {string} request to {string}")
    public void iSendARequestTo(final String httpMethod, final String endpoint) {
        RequestSpecification requestSpecification = (RequestSpecification) context.get("REQUEST_SPEC");
        String builtEndpoint = DynamicIdHelper.buildEndpoint(context, endpoint);
        response = RequestManager.doRequest(httpMethod, requestSpecification, builtEndpoint);
        context.set("LAST_ENDPOINT", builtEndpoint);
        context.set("LAST_RESPONSE", response);
    }

    @Then("I validate the response has status code {int}")
    public void iValidateTheResponseHasStatusCode(int expectedStatusCode) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, expectedStatusCode);
    }

    @And("I save the response as {string}")
    public void iSaveTheResponseAs(final String key) {
        context.set(key, response);
    }

    @And("I validate the response contains:")
    public void iValidateTheResponseContains(final Map<String, String> validationMap) {
        Map<String, Object> responseMap = response.jsonPath().getMap(".");
        for (Map.Entry<String, String> data: validationMap.entrySet()) {
            if (responseMap.containsKey(data.getKey())) {
                Assert.assertEquals(String.valueOf(responseMap.get(data.getKey())), data.getValue());
            }
        }
    }

    @And("I obtained the Student by email {string} saved as {string}")
    public void iObtainedTheStudentByEmail(String searchTo, String key) {
        List<Map<String, Object>> studentList = ((Response) context.get("students")).jsonPath().get();

        for (Map<String, Object> student: studentList) {
            if (student.get("email").equals(searchTo)) {
                context.set(key, student);
            }
        }
    }
}
