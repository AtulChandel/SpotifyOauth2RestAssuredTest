package spotify.oauth2.api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static spotify.oauth2.api.SpecBuilder.getResponseSpec;

public class RestResource {
        public static Response post(String path, String token, Object requestPlaylist){
            return given(getRequestSpec()).body(requestPlaylist).header("Authorization", "Bearer "+ token).post(path).then().spec(getResponseSpec()).extract().response();
        }

        public static Response get(String path, String token){
            return given(getRequestSpec()).header("Authorization", "Bearer "+ token).when().get(path).then().spec(getResponseSpec()).assertThat().extract().response();
        }

        public static Response update(String path, String token, Object requestPlaylist){
            return given(getRequestSpec()).body(requestPlaylist).header("Authorization", "Bearer "+ token).when().put(path).then().spec(getResponseSpec()).extract().response();
        }
    }
