package spotify.oauth2.api;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static spotify.oauth2.api.Route.API;
import static spotify.oauth2.api.Route.TOKEN;
import static spotify.oauth2.api.SpecBuilder.*;

public class RestResource {
        public static Response post(String path, String token, Object requestPlaylist){
            return given(getRequestSpec()).body(requestPlaylist).auth().oauth2(token).post(path).then().spec(getResponseSpec()).extract().response();
        }

        public static Response get(String path, String token){
            return given(getRequestSpec()).auth().oauth2(token).when().get(path).then().spec(getResponseSpec()).assertThat().extract().response();
        }

        public static Response update(String path, String token, Object requestPlaylist){
            return given(getRequestSpec()).body(requestPlaylist).auth().oauth2(token).when().put(path).then().spec(getResponseSpec()).extract().response();
        }

        public static Response postAccount(HashMap<String, String> formParams){
            return given(getAccountRequestSpec()).formParams(formParams).log().all().when().post(API + TOKEN).then().spec(getResponseSpec()).extract().response();
        }
    }
