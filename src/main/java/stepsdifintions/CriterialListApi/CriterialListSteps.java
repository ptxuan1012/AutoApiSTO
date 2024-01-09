package stepsdifintions.CriterialListApi;


import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import common.ApiUtils;
import common.JsonUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CriterialListSteps {
	String url, requestBodyFilePath,requestBody;
	HttpResponse<String> response;
	JsonUtils jsonUtils = new JsonUtils();
	ApiUtils apiUtils = new ApiUtils();
	
	
	@Given("I have url and Method and request body of criterial list api")
	public void i_have_url_and_method_and_request_body_of_criterial_list_api(DataTable givenTable) {
		List<Map<String, String>> list =givenTable.asMaps(String.class,String.class);
		String requestBodyName="";
		for (Map<String, String> m: list) {
			url=m.get("URL");
			requestBodyName=m.get("RequestBodyName");
		}
		requestBodyFilePath =System.getProperty("user.dir") + "\\src\\main\\resources\\" + requestBodyName;
	}
	
	@When("I sent criterial list request")
	public void i_sent_criterial_list_request() {
		requestBody =jsonUtils.readJsonFile(requestBodyFilePath);
			response=apiUtils.sendPostRequest(url, requestBody);
	}
	
	@Then("I check {int} of criterial list api correctly")
	public void i_check_of_criterial_list_api_correctly(int expectedStatusCode) {
		assertEquals(response.statusCode(), expectedStatusCode);
	}
	
	//Validate
	@When("I sent criterial request with validation data with {string} and {string}")
	public void i_sent_cat_request_with_validation_data(String fieldName, String value) {
		requestBody= createRequestBody(requestBodyFilePath,fieldName, value);
		response = apiUtils.sendPostRequest(url, requestBody);

	}
	
	@Then("I check {int} and {string} and {string} of criterial api correctly")
	public void i_check_and_must_be_a_string_of_cat_api_correctly(int expectedStatusCode, String fieldExMessage, String expectedErrorMessage) {
		assertEquals(response.statusCode(), expectedStatusCode);
		String res =response.body().toString();
		System.out.println("====================================================================================================");
		System.out.println("Response la: "+res);
		System.out.println("====================================================================================================");
		assertEquals(jsonUtils.getValueByKeyMitl(res, fieldExMessage),expectedErrorMessage);
	}
	
	
	public String createRequestBody(String jsonBodySourceFilePath, String fieldName, String value) {
		String requestBody = "";
		
		File sourceFile = new File(jsonBodySourceFilePath);
		String requestBodyName = sourceFile.getName();

		String jsonBodyDictinationFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\"
				+ requestBodyName.replace(".json", "Copy.json");
		File dictinationFile = new File(jsonBodyDictinationFilePath);
		jsonUtils.copyJsonFile(sourceFile, dictinationFile);

		// đổi giá trị //đọc content file
		requestBody = jsonUtils.replateJSONObjectValue(dictinationFile, fieldName, value);
		return requestBody;
	}
	
	}


