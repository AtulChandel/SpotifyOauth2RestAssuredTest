package spotify.oauth2.tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static spotify.oauth2.applicationApi.PlaylistApi.*;

import org.testng.annotations.Test;
import spotify.oauth2.applicationApi.PlaylistApi;
import spotify.oauth2.pojo.Error;
import spotify.oauth2.pojo.PlayList;
import spotify.oauth2.utils.DataLoader;

@Epic("Spotify Oauth2.0")
@Feature("Playlist APIs")
public class PlaylistTests {

    @Step
    public PlayList playlistBuilder(String name, String description, boolean publicFlag){
        return PlayList.builder().name(name).description("description")._public(publicFlag).build();
    }

    @Step
    public void assertPlaylistEqual(PlayList responsePlaylist, PlayList requestPlaylist){
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    @Step
    public void assertError(Error responseError, int expectedStatusCode, String expectedMessage){
        assertStatusCode(responseError.getError().getStatus(), expectedStatusCode);
        assertThat(responseError.getError().getMessage(), equalTo(expectedMessage));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, int expectedStatusCode){
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Story("Create a playlist story")
    @Issue("https://example.jira.org/ISSUE-123")
    @Link("https://developer.spotify.com/dashboard/a70fc2d0525349af95330282ad3bc033/endpoints")
    @Test(description = "Should be able to create a playlist")
    public void ShouldBeAbleToCreatePlaylist() {

        PlayList requestPlaylist = playlistBuilder("New Playlist","New playlist description", false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 201);

        PlayList responsePlaylist = response.as(PlayList.class);
        assertPlaylistEqual(requestPlaylist, responsePlaylist);

    }

    @Test(description = "Should be able to fetch playlist")
    public void ShouldBeAbleToGetPlaylist() {

        PlayList requestPlaylist = playlistBuilder("Updated Playlist", "Updated playlist description", true);
        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.getStatusCode(), 200);

        PlayList responsePlaylist = response.as(PlayList.class);
        assertStatusCode(response.getStatusCode(), 200);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }

    @Story("Create a playlist story")
    @Test(description = "Should be able to update a playlist")
    public void ShouldBeAbleToUpdatePlaylist() {
        PlayList requestPlaylist = playlistBuilder("Updated Playlist","Updated playlist description",false);
        Response response = update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.getStatusCode(), 200);
    }

    @Story("Create a playlist story")
    @Test(description = "Should not be able to create a playlist without a name")
    public void ShouldNotBeAbleToCreatePlaylistWithoutName() {
        PlayList requestPlaylist = playlistBuilder("","Updated playlist description",false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 400);

        Error error = response.as(Error.class);
        assertError(error, 400,"Missing required field: name");

    }

    @Test(description = "Should not be able to create a playlist with expired token")
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken() {

        PlayList requestPlaylist = playlistBuilder("New Playlist","New playlist description",false);
        Response response = PlaylistApi.post("zxcvbnm1234",requestPlaylist);
        assertStatusCode(response.getStatusCode(), 401);

        Error error = response.as(Error.class);
        assertError(error, 401, "Invalid access token");

    }
}
