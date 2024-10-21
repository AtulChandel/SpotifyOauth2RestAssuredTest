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

    @Test
    public void ShouldBeAbleToCreatePlaylist() {

        PlayList requestPlaylist = new PlayList().setName("New Playlist").setDescription("New playlist description").setPublic(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(), equalTo(201));

        PlayList responsePlaylist = response.as(PlayList.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void ShouldBeAbleToGetPlaylist() {


        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertThat(response.getStatusCode(), equalTo(200));

        PlayList responsePlaylist = response.as(PlayList.class);
        assertThat(responsePlaylist.getName(), equalTo("Updated Playlist"));
        assertThat(responsePlaylist.getDescription(), equalTo("Updated playlist description"));
        assertThat(responsePlaylist.getPublic(), equalTo(true));
    }

    @Test
    public void ShouldBeAbleToUpdatePlaylist() {
        PlayList requestPlaylist = new PlayList().setName("Updated Playlist").setDescription("Updated playlist description").setPublic(false);
        Response response = update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertThat(response.getStatusCode(), equalTo(200));

    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithoutName() {
        PlayList requestPlaylist = new PlayList().setName("").setDescription("Updated playlist description").setPublic(false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(), equalTo(400));

        Error error = response.as(Error.class);
        assertThat(error.getError().getMessage(), equalTo("Missing required field: name"));

    }

    @Test
    public void ShouldNotBeAbleToCreatePlaylistWithExpiredToken() {

        PlayList requestPlaylist = new PlayList().setName("New Playlist").setDescription("New playlist description").setPublic(false);
        Response response = PlaylistApi.post("zxcvbnm1234",requestPlaylist);
        assertThat(response.getStatusCode(), equalTo(401));
        Error error = response.as(Error.class);

        assertThat(error.getError().getMessage(), equalTo("Invalid access token"));

    }
}
