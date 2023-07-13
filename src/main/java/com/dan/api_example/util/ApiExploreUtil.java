package com.dan.api_example.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class ApiExploreUtil {

    private final String BASE_URL = "http://openapi.seoul.go.kr:8088/";
    private final String certificationKey = "66417562426c6463373545746f5775/";
    private final String returnType = "json/";
    private final String dataType = "bikeList/";

    public String makeUrl(int start, int end) {
        return BASE_URL + certificationKey + returnType + dataType + start + "/" + end;
    }

    public String getData(int start, int end) throws IOException {

        String stringUrl = makeUrl(start, end);
        log.info("url : {}", stringUrl);

        URL url = new URL(stringUrl);
        // 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    public void stringToJsonToDto() {
        String str = "{\"rackTotCnt\":\"user01\", \"userName\":\"홍길동\"}";
        String strList = "[{\"userID\":\"user01\", \"userName\":\"홍길동\"},{\"userID\":\"user02\", \"userName\":\"홍길순\"}]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            // 1. 스트링에서 DTO로 매핑하기
            UserDTO dto = mapper.readValue(str, UserDTO.class);
            System.out.println(dto);
            // UserDTO(userID=user01, userName=홍길동)

            // 2. 스트링에서 DTO LIST로 매핑하기
            List<UserDTO> dtos = Arrays.asList(mapper.readValue(strList, UserDTO[].class));
            System.out.println(dtos.size() + " : " +dtos);
            // 2 : [UserDTO(userID=user01, userName=홍길동), UserDTO(userID=user02, userName=홍길순)]

            // 객체를 JSON 스트링으로 변환하기
            String jsonStr = mapper.writeValueAsString(dtos);
            System.out.println(jsonStr);
            // [{"userID":"user01","userName":"홍길동"},{"userID":"user02","userName":"홍길순"}]

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
