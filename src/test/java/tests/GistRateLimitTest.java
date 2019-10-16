package tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import io.restassured.response.Response;
import tools.BaseTest;

public class GistRateLimitTest extends BaseTest {

	/**
	 * Test to verify Rate Limit is 10 for unauthenticated search request
	 */
	@Test(description="Search Rate Limit Test without authentication", priority=13)
	public void testSearchRateLimitWithoutAuthentication() {
		String api = "search/repositories?q=topic:ruby+topic:rails";		
		Response response = request.get(api);
		assertEquals(response.getStatusCode(), 200);
		
		String rateLimitHeader = response.getHeader("X-RateLimit-Limit");
		assertEquals(rateLimitHeader, "10");
		System.out.println("Rate Limit = " + rateLimitHeader);
	}
	
	/**
	 * Test to verify Rate Limit is 30 for authenticated search request
	 */
	@Test(description="Search Rate Limit Test with authentication", priority=14)
	public void testSearchRateLimitWitAuthentication() {
		String api = "search/repositories?q=topic:ruby+topic:rails";		
		Response response = request.header("Authorization", "token " + TOKEN)
				.header("X-Auth-Scopes", "repo, user")
				.header("X-Accepted-OAuth-Scopes", "user")
				.get(api);
		assertEquals(response.getStatusCode(), 200);
		String rateLimitHeader = response.getHeader("X-RateLimit-Limit");
		assertEquals(rateLimitHeader, "30");
		System.out.println("Rate Limit = " + rateLimitHeader);
	}
}
