package spotify.oauth2.tests;

import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static spotify.oauth2.applicationApi.PlaylistApi.*;
import spotify.oauth2.applicationApi.PlaylistApi;
import spotify.oauth2.pojo.Error;
import spotify.oauth2.pojo.PlayList;
import spotify.oauth2.utils.DataLoader;

public class PlaylistTests {

    public PlayList playlistBuilder(String name, String description, boolean publicFlag){
        return PlayList.builder().name(name).description("description")._public(publicFlag).build();
    }

    public void assertPlaylistEqual(PlayList responsePlaylist, PlayList requestPlaylist){
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    public void assertError(Error responseError, int expectedStatusCode, String expectedMessage){
        assertStatusCode(responseError.getError().getStatus(), expectedStatusCode);
        assertThat(responseError.getError().getMessage(), equalTo(expectedMessage));
    }

    public void assertStatusCode(int actualStatusCode, int expectedStatusCode){
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Test
    public void ShouldBeAbleToCreatePlaylist() {

        PlayList requestPlaylist = playlistBuilder("New Playlist","New playlist description", false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 201);

        PlayList responsePlaylist = response.as(PlayList.class);
        assertPlaylistEqual(requestPlaylist, responsePlaylist);

    }

    @Test
    public void ShouldBeAbleToGetPlaylist() {

        PlayList requestPlaylist = playlistBuilder("Updated Playlist", "Updated playlist description", true);
        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.getStatusCode(), 200);

        PlayList responsePlaylist = response.as(PlayList.class);
        assertStatusCode(response.getStatusCode(), 200);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist() {
        PlayList requestPlaylist = playlistBuilder("Updated Playlist","Updated playlist description",false);
        Response response = update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.getStatusCode(), 200);
    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName() {
        PlayList requestPlaylist = playlistBuilder("","Updated playlist description",false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 400);

        Error error = response.as(Error.class);
        assertError(error, 400,"Missing required field: name");

    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken() {

        PlayList requestPlaylist = playlistBuilder("New Playlist","New playlist description",false);
        Response response = PlaylistApi.post("zxcvbnm1234",requestPlaylist);
        assertStatusCode(response.getStatusCode(), 401);

        Error error = response.as(Error.class);
        assertError(error, 401, "Invalid access token");

    }
}
