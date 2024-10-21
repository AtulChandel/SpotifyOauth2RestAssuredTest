package spotify.oauth2.api;

import com.sun.xml.bind.v2.TODO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static spotify.oauth2.api.Route.API;
import static spotify.oauth2.api.Route.TOKEN;
import static spotify.oauth2.api.SpecBuilder.*;

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

        public static Response postAccount(HashMap<String, String> formParams){
            return given(getAccountRequestSpec()).formParams(formParams).log().all().when().post(API + TOKEN).then().spec(getResponseSpec()).extract().response();
        }
    }
