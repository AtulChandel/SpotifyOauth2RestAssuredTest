package spotify.oauth2.applicationApi;

import io.restassured.response.Response;
import spotify.oauth2.api.RestResource;
import spotify.oauth2.pojo.PlayList;
import static spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {

    public static Response post(PlayList requestPlaylist){
        return RestResource.post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists", getToken(), requestPlaylist).then().spec(getResponseSpec()).extract().response();
    }

    public static Response post(String token, PlayList requestPlaylist){
        return RestResource.post("/users/315jpcbxcid2bnvbjf5tx7tw46bu/playlists", token, requestPlaylist).then().spec(getResponseSpec()).assertThat().extract().response();
    }

    public static Response get(String requestPlaylistId){
        return RestResource.get("/playlists/" + requestPlaylistId, getToken()).then().spec(getResponseSpec()).assertThat().extract().response();
    }

    public static Response update(PlayList requestPlaylist, String requestPlaylistId){
        return RestResource.update("/playlists/" + requestPlaylistId, getToken(), requestPlaylist).then().spec(getResponseSpec()).extract().response();
    }
}
