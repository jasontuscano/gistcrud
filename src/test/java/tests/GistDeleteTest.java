package tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import io.restassured.response.Response;
import tools.BaseTest;

public class GistDeleteTest extends BaseTest {

	/**
	 * Test to verify status code 404 while deleting a Gist without authorization
	 */
	@Test(description="Delete Gist Test without Authorization", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=9)
	public void testDeleteGistWithoutAuthorization() {
		String api = "/gists/" + gistId;		
		Response response = request.delete(api);
		assertEquals(response.getStatusCode(), 404);
	}
	
	/**
	 * Test to verify status code 204 while deleting a Gist with authorization
	 */
	@Test(description="Delete Gist Test with Authorization", dependsOnMethods= {"tests.GistCreateTest.testCreateGistAuthorized"}, 
			priority=12)
	public void testDeleteGistWithAuthorization() {
		String api = "/gists/" + gistId;		
		Response response = request.header("Authorization", "token " + TOKEN)
				.header("X-Auth-Scopes", "repo, user")
				.header("X-Accepted-OAuth-Scopes", "user")
				.delete(api);
		assertEquals(response.getStatusCode(), 204);
		System.out.println("Deleted Gist with id '" + gistId + "'");
	}
}
