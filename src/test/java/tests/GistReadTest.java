package tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.json.JSONArray;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import tools.BaseTest;

public class GistReadTest extends BaseTest {
	
	/**
	 * Test to verify status 200 while retrieving public gists
	 */
	@Test(description="Read Public Gists Test", priority=0)
	public void testGetPublicGist() {
		String api = "/gists/public";
		Response response = request.get(api);
		assertEquals(200, response.getStatusCode());
		JsonPath json = response.jsonPath();
		assertTrue(json.getString("description").length() > 0);
	}
	
	/**
	 * Test to verify status 401 while retrieving starred gists wihtout api token
	 */
	@Test(description="Read Starred Gists without Authentication Test", priority=1)
	public void testGetStarredGistUnAuthorized() {
		String api = "/gists/starred";
		Response response = request.get(api);
		assertEquals(401, response.getStatusCode());
		JsonPath json = response.jsonPath();
		assertEquals("Requires authentication", json.getString("message"));
	}
	
	/**
	 * Test to verify status 200 while retrieving starred gists with api token authentication
	 */
	@Test(description="Read Starred Gists with Authentication Test", dependsOnMethods= {"tests.GistUpdateTest.testStarGistAuthorized"},  
			priority=10)
	public void testGetStarredGistAuthorized() {
		String api = "/gists/starred";
		RestAssured.baseURI = baseUrl;
		Response response = request.header("Authorization", "token " + TOKEN)
				.get(api);
		assertEquals(200, response.getStatusCode());
		
		JsonPath json = response.jsonPath();
		assertEquals(json.getString("id").replace("[", "").replace("]", ""), gistId);
		System.out.println("Starred Gist id = " + gistId);
	}
	
	/**
	 * Test to verify status 200 while retrieving user specific gists with authentication
	 */
	@Test(description="Get User Specific Gists Test", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=5)
	public void testGetUserSpecificGist() {
		String api = "/gists";
		Response response = request.header("Authorization", "token " + TOKEN)
				.header("X-Auth-Scopes", "repo, user")
				.header("X-Accepted-OAuth-Scopes", "user")
				.get(api);
		assertEquals(response.getStatusCode(), 200);
		JSONArray json = new JSONArray(response.getBody().asString());
		if(json.length() == 0) {
			return;
		}
		gistId = json.getJSONObject(0).getString("id");
		assertTrue(gistId.length() > 0);
		System.out.println("User specific Gist id = " + gistId);
	}
}
