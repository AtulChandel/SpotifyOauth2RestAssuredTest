package spotify.oauth2.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static spotify.oauth2.api.SpecBuilder.getResponseSpec;

public class TokenManager {
    private static String access_token;
    private static Instant expiry_time;

    public static String getToken(){
        try{
            if(access_token == null || Instant.now().isAfter(expiry_time)){
                System.out.println("Renewing token");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurInSec = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurInSec - 300);
            }
            else{
                System.out.println("Reusing previous token as it is not yet expired");
            }
        }
        catch (Exception e){
            throw new RuntimeException("Failed to get token");
        }
        return access_token;
    }

    private static Response renewToken(){
        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("client_id", "a70fc2d0525349af95330282ad3bc033");
        formParams.put("client_secret", "029bc216096341fd92ac31e5cbb3e220");
        formParams.put("refresh_token", "AQCTvgnCaltGeVvXM_xvytDvMqHLbF0QQnrySN_Kph3U-sQrMDRS61KiDf62KcpQ6tirCUjMACy_F7SQ56K5qcX0uEkIxMesiZ5cdCC6tROnTcS1nodX4cNXY2lh1G89HjY");
        formParams.put("grant_type", "refresh_token");

        Response response = given().baseUri("https://accounts.spotify.com").contentType(ContentType.URLENC).formParams(formParams).log().all().when().post("/api/token").then().spec(getResponseSpec()).extract().response();

        if(response.statusCode() !=200){
            throw new RuntimeException("Renew token failed!!");
        }
        return response;
    }
}
