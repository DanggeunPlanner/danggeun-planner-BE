package com.finalteam4.danggeunplanner.member.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.dto.request.OauthLoginRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.SOCIAL_LOGIN_ERROR;

@Slf4j
@Service
public class OauthService {
    @Value("${kakao.clientId}")
    private String clientId;
    @Value("${kakao.redirectUri}")
    private String redirectUri;
    private String grantType = "authorization_code";

    public String getKakaoAccessToken(String code){
        System.out.println(code);

        String access_Token = "";
//        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";


        try{
            URL url = new URL(reqURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=" + grantType);
            sb.append("&client_id=" + clientId);
            sb.append("&redirect_uri=" + redirectUri);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = connection.getResponseCode();
            log.info("KakaoAccessToken-ResponseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
//            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
//            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            throw new DanggeunPlannerException(SOCIAL_LOGIN_ERROR);
        }

        return access_Token;
    }
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            JsonElement element = getResponseJson(br);
//
//            access_Token = element.getAsJsonObject().get("access_token").getAsString();
//
//            log.info("Kakao-Accesstoken : " + access_Token);
//
//            br.close();
//            bw.close();
//
//            return access_Token;
//
//        } catch (IOException e) {
//            throw new DanggeunPlannerException(SOCIAL_LOGIN_ERROR);
//        }

    //카카오에서 준 access_Token으로 유저 정보 조회
    public OauthLoginRequest createKakaoUser(String token){

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("create-KakaoUser-responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = JsonParser.parseString(result);

//          int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

            String email = "";
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            log.info("kakao-user-email : " + email);

            br.close();

            return new OauthLoginRequest(email);

        } catch (IOException e) {
            throw new DanggeunPlannerException(SOCIAL_LOGIN_ERROR);
        }
    }


}
