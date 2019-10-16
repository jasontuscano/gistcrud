package tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

	protected final String baseUrl = Settings.getProperty("URL");
	protected final String TOKEN = Settings.getProperty("APIToken");
	protected RequestSpecification request;
	protected static String gistId;
	
	@BeforeSuite
	public void beforeSuite() {
		List<String> gistIds = getAvailableGistIds();
		deleteGist(gistIds);
	}
	
	@BeforeMethod
	public void initialize() {
		RestAssured.baseURI = baseUrl;
		request = RestAssured.given();
	}
	
	@AfterMethod
	public void finalize(ITestResult result) {
		String name = result.getMethod().getDescription();
		if(result.getStatus() == ITestResult.SUCCESS) {
			System.out.println("PASSED : " + name);
			System.out.println("====================================");
		}
		else if(result.getStatus() == ITestResult.FAILURE) {
			System.out.println("FAILED : " + name);
			System.out.println("====================================");
		}
	}
	
	public void deleteGist(List<String> gistIds) {
		if(gistIds.isEmpty()) {
			return;
		}
		for(String id : gistIds) {
			String api = "/gists/" + id;
			request = RestAssured.given();
			Response response = request.header("Authorization", "token " + TOKEN)
					.header("X-Auth-Scopes", "repo, user")
					.header("X-Accepted-OAuth-Scopes", "user")
					.delete(api);
			if(response.getStatusCode() == 204) {
				System.out.println("Deleted GIST with id " + id);
			}
		}
	}
	
	public List<String> getAvailableGistIds() {
		List<String> gistIds = new ArrayList<String>();
		try {
			String api = "/gists";
			RestAssured.baseURI = baseUrl;
			request = RestAssured.given();
			Response response = request.header("Authorization", "token " + TOKEN)
					.header("X-Auth-Scopes", "repo, user")
					.header("X-Accepted-OAuth-Scopes", "user")
					.get(api);
			int status = response.getStatusCode();
			if(status != 200) {
				return gistIds;
			}
			String body = response.body().asString();
			JSONArray json = new JSONArray(body);
			if(json.isEmpty()) {
				return gistIds;
			}
			for(int i=0;i<json.length();i++) {
				JSONObject gist = json.getJSONObject(i);
				if(!gist.has("id")) {
					continue;
				}
				String id = gist.getString("id");
				gistIds.add(id); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gistIds;
	}
}
