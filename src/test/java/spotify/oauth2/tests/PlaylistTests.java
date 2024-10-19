package spotify.oauth2.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.junit.BeforeClass;
import org.junit.Test;
import spotify.oauth2.pojo.PlayList;
import spotify.oauth2.pojo.Error;


import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlaylistTests {
    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;
    static String access_token = "BQC5F6zzu8LobluYpk_Px0NXzrS13T4OavYMrmeuTkriUd2QiUs_5Mivgic9Evl1t_d3RurBH1c_WnFt09vbya4nXo3m6ZXrfaQvxX4KkKgoGAT9FFByGfGNI_N8vWaOVAqPdqAouyC9Nvdr8kiPQ38bqkYdeaAMXsYyiaDKC42w_uHpRw9mWJB0po_8olTfqs2lKhqpnGckLP-YjWel-6pEy3Z22qvn8YCXMwawgnibtKxZQqcjttFAKcWnlZBSlUnnJ7FSeOiz6Y_U7mV5k-Ue";

    @BeforeClass
    public static void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().setBaseUri("https://api.spotify.com").setBasePath("/v1").addHeader("Authorization", "Bearer " + access_token).setContentType(ContentType.JSON).log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void ShouldBeAbleToCreatePlaylist() {
        PlayList requestPlaylist = new PlayList();
        requestPlaylist.setName("New playlist");
        requestPlaylist.setDescription("New playlist description");
        requestPlaylist.setPublic(false);

        PlayList responsePlaylist = given(requestSpecification).body(requestPlaylist).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(201).extract().response().as(PlayList.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));

    }

    @Test
    public void ShouldBeAbleToGetPlaylist() {
        PlayList requestPlaylist = new PlayList();
        requestPlaylist.setName("Updated playlist");
        requestPlaylist.setDescription("Updated playlist description");
        requestPlaylist.setPublic(true);

        PlayList responsePlaylist = given(requestSpecification).when().get("/playlists/4GS0eXJNfL9wAS4HUpGuzn").then().spec(responseSpecification).assertThat().statusCode(200).extract().response().as(PlayList.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));


    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist() {
        PlayList requestPlaylist = new PlayList();
        requestPlaylist.setName("Updated playlist");
        requestPlaylist.setDescription("Updated playlist description");
        requestPlaylist.setPublic(true);

        given(requestSpecification).body(requestPlaylist).when().put("/playlists/4GS0eXJNfL9wAS4HUpGuzn").then().spec(responseSpecification).assertThat().statusCode(200);

    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName() {
        PlayList requestPlaylist = new PlayList();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("Updated playlist description");
        requestPlaylist.setPublic(false);


        Error error = given(requestSpecification).body(requestPlaylist).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(400).extract().as(Error.class);
        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken() {
        PlayList requestPlaylist = new PlayList();
        requestPlaylist.setName("Updated playlist");
        requestPlaylist.setDescription("Updated playlist description");
        requestPlaylist.setPublic(false);

        Error error = given().baseUri("https://api.spotify.com").basePath("v1").header("Authorization", "Bearer 12345").contentType(ContentType.JSON).log().all().body(requestPlaylist).when().post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists").then().spec(responseSpecification).assertThat().statusCode(401).extract().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(), equalTo("Invalid access token"));

    }
}
