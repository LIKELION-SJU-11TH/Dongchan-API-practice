package com.dan.api_example.util;

import com.dan.api_example.model.GetBikeListRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ExternalApiUtil {

    private final String SEOUL_DATA_KEY = "66417562426c6463373545746f5775";
    private final String JSON_TYPE = "json";
    private final String XML_TYPE = "xml";

    /**
     * API 사용 주소 : https://data.seoul.go.kr/dataList/OA-15493/A/1/datasetView.do
     * <p>
     * 샘플 URL : http://openapi.seoul.go.kr:8088/(인증키)/json/bikeList/1/5/
     * key : 인증키 (String - 필수)
     * type : 요청 파일 타입 (String - 필수) / xml, json 선택
     * service : 서비스명 (String - 필수) / bikeList
     * start_index : 요청 시작 위치 (int - 필수) / 페이징 시작 번호
     * end_index : 요청 종료 위치 (int - 필수) / 페이징 끝 번호
     */
    public void getRentBikeList(int startIdx, int amount) throws TimeoutException {
        log.info("API GET RENT BIKE LIST INTRO");
        String serviceName = "bikeList";

        StringBuilder baseURL = new StringBuilder("http://openapi.seoul.go.kr:8088");
        baseURL.append("/");
        baseURL.append(SEOUL_DATA_KEY); // 인증키
        baseURL.append("/");
        baseURL.append(JSON_TYPE); // 타입
        baseURL.append("/");
        baseURL.append(serviceName); // 서비스명
        baseURL.append("/");
        baseURL.append(startIdx); // 시작
        baseURL.append("/");
        baseURL.append(startIdx + amount - 1); // 끝
        log.info("SEND URL : {}", baseURL.toString());
        HashMap<String, Object> response = getRequest(baseURL.toString());

        log.info("response Header : {}", response.get("header"));
        log.info("response Header's type : {}", response.get("header").getClass());
        log.info("response Body : {}", response.get("body"));
        log.info("response Body's type : {}", response.get("body").getClass());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GetBikeListRes.GetBikeResList getBikeListResList = objectMapper.readValue(response.get("body").toString(), GetBikeListRes.GetBikeResList.class);
            log.info("GetBikeResList : {}", getBikeListResList.getRentBikeStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getRequest(String url) throws TimeoutException {
        log.info("API GET REQUEST INTRO");
        HashMap<String, Object> result = new HashMap<>();
        String jsonInString = "";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(5*1000);
            factory.setReadTimeout(5*1000);
            restTemplate.setRequestFactory(factory);

            ResponseEntity<Object> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Object.class);

            result.put("statusCode", resultMap.getStatusCode());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());
        } catch (Exception e) {
            throw new TimeoutException();
        }

        return result;
    }
}
