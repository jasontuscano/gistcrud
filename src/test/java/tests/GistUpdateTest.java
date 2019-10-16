package tests;

import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import tools.BaseTest;

public class GistUpdateTest extends BaseTest {
	
	String editGistName = "Test Gist Edit";
	JSONObject payload = new JSONObject();
	
	public JSONObject createEditGistPayload() {
		payload.put("description", editGistName);
		payload.put("public", true);
		JSONObject files = new JSONObject();
		files.put("Readme.md", new JSONObject().put("content", "Test"));
		payload.put("files", files);
		return payload;
	}
	
	/**
	 * Test to verify status 401 while editing Gist without authorization
	 */
	@Test(description="Edit Gist without Authorization Test", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=6)
	public void testUpdateGistUnAuthorized() {
		String api = "/gists/" + gistId;
		if(payload.isEmpty()) {
			payload = createEditGistPayload();
		}
		request.body(payload.toString());
		Response response = request.post(api);
		assertEquals(response.getStatusCode(), 404);
	}
	
	/**
	 * Test to verify status 200 while editing Gist with authorization
	 */
	@Test(description="Edit Gist with Authorization Test", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=7)
	public void testUpdateGistAuthorized() {
		String api = "/gists/" + gistId;
		if(payload.isEmpty()) {
			payload = createEditGistPayload();
		}
		System.out.println("New Gist name = " + editGistName);
		request.body(payload.toString());
		Response response = request.header("Authorization", "token " + TOKEN)
				.post(api);
		assertEquals(response.getStatusCode(), 200);
		
		JsonPath json = response.jsonPath();
		assertEquals(editGistName, json.getString("description"));
		System.out.println("Gist is updated with the name" + editGistName + "'");
	}
	
	/**
	 * Test to verify status 204 on starring a gist with Authorization
	 */
	@Test(description="Star Gist Test", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=8)
	public void testStarGistAuthorized() {
		String api = "/gists/" + gistId + "/star";		
		Response response = request.header("Authorization", "token " + TOKEN)
				.put(api);
		assertEquals(response.getStatusCode(), 204);
		System.out.println("Starred Gist with id " + gistId);
	}
	
	@Test(description="Unstar Gist Test", dependsOnMethods= {"tests.GistUpdateTest.testStarGistAuthorized"}, 
			priority=11)
	public void testUnStarGistAuthorized() {
		String api = "/gists/" + gistId + "/star";		
		Response response = request.header("Authorization", "token " + TOKEN)
				.delete(api);
		assertEquals(response.getStatusCode(), 204);
		System.out.println("Unstarred Gist with id " + gistId);
	}
}
