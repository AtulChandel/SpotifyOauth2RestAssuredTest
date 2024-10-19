package spotify.oauth2.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.junit.BeforeClass;
import org.junit.Test;


import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.equalTo;

public class PlaylistTests {
    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;
    static String access_token = "*********";

    @BeforeClass
    public static void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().setBaseUri("https://api.spotify.com").setBasePath("/v1").addHeader("Authorization", "Bearer " + access_token).setContentType(ContentType.JSON).log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void ShouldBeAbleToCreatePlaylist() {
        String payload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).body(payload).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(201).body("name", equalTo("New Playlist"), "description", equalTo("New playlist description"), "public", equalTo(false));
    }

    @Test
    public void ShouldBeAbleToGetPlaylist() {

        given(requestSpecification).when().get("/playlists/4GS0eXJNfL9wAS4HUpGuzn").then().spec(responseSpecification).assertThat().statusCode(200).body("name", equalTo("Updated Playlist Name"), "description", equalTo("Updated playlist description"), "public", equalTo(true));
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist() {
        String payload = "{\n" +
                "    \"name\": \"Updated Playlist\",\n" +
                "    \"description\": \"Updated playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).body(payload).when().put("/playlists/4GS0eXJNfL9wAS4HUpGuzn").then().spec(responseSpecification).assertThat().statusCode(200);
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName() {
        String payload = "{\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given(requestSpecification).body(payload).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(400).body("error.status", equalTo(400), "error.message", equalTo("Missing required field: name"));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken() {
        String payload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";

        given().baseUri("https://api.spotify.com").basePath("v1").header("Authorization", "Bearer 12345").contentType(ContentType.JSON).log().all().body(payload).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(401).body("error.status", equalTo(401), "error.message", equalTo("Invalid access token"));
    }
}
