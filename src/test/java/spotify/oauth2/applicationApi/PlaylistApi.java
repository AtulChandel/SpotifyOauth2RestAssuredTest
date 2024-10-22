package spotify.oauth2.applicationApi;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import spotify.oauth2.api.RestResource;
import spotify.oauth2.pojo.PlayList;
import spotify.oauth2.utils.ConfigLoader;

import static spotify.oauth2.api.Route.PLAYLISTS;
import static spotify.oauth2.api.Route.USERS;
import static spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {

    @Step
    public static Response post(PlayList requestPlaylist){
        return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUser()  + PLAYLISTS, getToken(), requestPlaylist).then().spec(getResponseSpec()).extract().response();
    }

    @Step
    public static Response post(String token, PlayList requestPlaylist){
        return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUser()  + PLAYLISTS, token, requestPlaylist).then().spec(getResponseSpec()).assertThat().extract().response();
    }

    @Step
    public static Response get(String requestPlaylistId){
        return RestResource.get(PLAYLISTS +"/" + requestPlaylistId, getToken()).then().spec(getResponseSpec()).assertThat().extract().response();
    }

    @Step
    public static Response update(PlayList requestPlaylist, String requestPlaylistId){
        return RestResource.update(PLAYLISTS+ "/" + requestPlaylistId, getToken(), requestPlaylist).then().spec(getResponseSpec()).extract().response();
    }
}
