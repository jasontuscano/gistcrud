package tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import tools.BaseTest;

public class GistCreateTest extends BaseTest {
	
	/**
	 * Test to create Gist without Authorization
	 */
	@Test(description="Create Gist without Authorization", priority=3)
	public void testCreateGistUnAuthorized() {
		String api = "/gists";
		JSONObject json = new JSONObject();
		json.put("description", "Test Gist");
		json.put("public", true);
		JSONObject files = new JSONObject();
		files.put("Readme.md", new JSONObject().put("content", "Test"));
		json.put("files", files);
		
		request.body(json.toString());
		Response response = request.post(api);
		assertEquals(response.getStatusCode(), 401);
		
		JsonPath body = response.jsonPath();
		assertEquals(body.getString("message"), "Requires authentication");
	}
	
	@Test(description="Create Gist with Authorization", dependsOnMethods= {"testCreateGistUnAuthorized"}, 
			priority=4)
	public void testCreateGistAuthorized() {
		String api = "/gists";
		JSONObject json = new JSONObject();
		json.put("description", "Test Gist");
		json.put("public", true);
		JSONObject files = new JSONObject();
		files.put("Readme.md", new JSONObject().put("content", "Test"));
		json.put("files", files);
		
		request.body(json.toString());
		Response response = request.header("Authorization", "token " + TOKEN)
				.post(api);
		assertEquals(response.getStatusCode(), 201);
		JsonPath body = response.jsonPath();
		String id = body.getString("id");
		assertTrue(id.length() > 0);
		System.out.println("Created Gist with id '" + id + "'");
	}
}
