package com.kh.pcar.back.station.model.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.pcar.back.station.model.dto.StationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceStationImpl implements ServiceStation {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 공공데이터 API 상수
    private  final String API_KEY =
            "?serviceKey=379f167eb3f41af06081d27f407899ed21955011b09d34a54e3519d8544a89cb&perPage=300";
    private  final String BASE_URL =
            "https://api.odcloud.kr/api/15039545/v1/uddi:f8f879ad-68cf-40fb-8ccc-cb36eaf1baca";

    // ================== 공통 유틸 메서드들 ==================

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> StationData() {

        String url = BASE_URL + API_KEY;
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("잘못된 충전소 API URL", e);
        }

        log.info("call station api: {}", url);

        String response = restTemplate.getForObject(uri, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(response, Map.class);

            return (List<Map<String, Object>>) map.get("data");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("충전소 API 응답 파싱 실패", e);
        }
    }

    // Map → StationDTO 변환
    private StationDTO stationDTO(Map<String, Object> item) {
        String latitude    = String.valueOf(item.get("위도"));
        String longitude   = String.valueOf(item.get("경도"));
        String stationName = String.valueOf(item.get("충전소명"));
        String address     = String.valueOf(item.get("충전소주소")); 

        return new StationDTO(latitude, longitude, stationName, address);
    }

    // 거리 계산 
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

   

    // 내 위치 기준 3km 이내 충전소 리스트
    @Override
    public List<StationDTO> stations(String lat, String lng) {

        List<Map<String, Object>> data = StationData();

        double userLat = Double.parseDouble(lat);
        double userLng = Double.parseDouble(lng);

        return data.stream()
                .filter(item -> {
                    double stLat = Double.parseDouble(String.valueOf(item.get("위도")));
                    double stLng = Double.parseDouble(String.valueOf(item.get("경도")));
                    double dist = distance(userLat, userLng, stLat, stLng);
                    return dist <= 3; // 3km 이내
                })
                .map(item -> stationDTO(item))
                .toList();
    }

    // keyword(이름/주소/위도/경도)로 검색
    @Override
    public List<StationDTO> searchByName(String keyword) {
    	//리스트를 맵형태로 변
        List<Map<String, Object>> data = StationData();
        String kw = (keyword == null) ? "" : keyword.trim();

        return data.stream()
                .filter(item -> {
                	//조건
                    String name   = String.valueOf(item.get("충전소명"));
                    String addr   = String.valueOf(item.get("충전소주소"));
                    String latStr = String.valueOf(item.get("위도"));
                    String lngStr = String.valueOf(item.get("경도"));

                    return name.contains(kw)
                            || addr.contains(kw)
                            || latStr.contains(kw)
                            || lngStr.contains(kw);
                })
                //map은 스트림 안에 있는 요소를 다른 형태로 변환하는 함수 
                //현재 각 요소는 Map<String,Object> item
                .map(item -> stationDTO(item))
                //리스트로 모아서 돌려줌
                .toList();
        //최종 타입은List<StationDTO>타입임
    }
}
