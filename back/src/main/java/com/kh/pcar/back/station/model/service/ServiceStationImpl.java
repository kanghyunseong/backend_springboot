package com.kh.pcar.back.station.model.service;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.pcar.back.station.model.dao.StationDAO;
import com.kh.pcar.back.station.model.dto.ReviewDTO;
import com.kh.pcar.back.station.model.dto.StationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceStationImpl implements ServiceStation {

    private final ReviewDTO reviewDto = new ReviewDTO();
    private final RestTemplate restTemplate = new RestTemplate();
    private final StationDAO stationDao;

    @Value("${charge.client.id}")
    private String chargeClientId;

    @Value("${charge.redirect.url}")
    private String chargeRedirectUrl;

    // 재사용 가능한 ObjectMapper
    private final ObjectMapper mapper = new ObjectMapper();


     // API 응답에서 "data" 필드(배열)를 Map 리스트로 안전하게 파싱해서 반환.
    
    private List<Map<String, Object>> StationData() {
        String url = chargeRedirectUrl + "&perPage=300&" + chargeClientId;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);

        try {
            
            Map<String, Object> root = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            Object dataObj = root.get("data");
            
            if (dataObj == null) return Collections.emptyList();

            // 안전하게 List<Map<String,Object>>로 변환
            List<Map<String, Object>> dataList = mapper.convertValue(
                    dataObj,
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            
            return dataList != null ? dataList : Collections.emptyList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("충전소 API 응답 파싱 실패", e);
        }
    }

    /**
     * 상세 1건 조회 (StatioDetail로 호출부와 일치시킴)
     */
    private List<StationDTO> getStationById(Long stationId) {
        String url = chargeRedirectUrl + "&perPage=300&" + chargeClientId; // 전체 데이터
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);

        try {
            Map<String, Object> root = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            Object dataObj = root.get("data");
            if (dataObj == null) return Collections.emptyList();

            List<Map<String, Object>> dataList = mapper.convertValue(
                    dataObj,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            // stationId로 필터링
            return dataList.stream()
                    .filter(item -> String.valueOf(item.get("충전소아이디")).equals(String.valueOf(stationId)))
                    .map(this::stationDTO)
                    .toList();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("API 파싱 실패", e);
        }
    }

    // Map → StationDTO 변환 (네가 작성한 키 사용: "위도","경도",...)
    private StationDTO stationDTO(Map<String, Object> item) {
        String latitude    = String.valueOf(item.get("위도"));
        String longitude   = String.valueOf(item.get("경도"));
        String stationName = String.valueOf(item.get("충전소명"));
        String address     = String.valueOf(item.get("충전소주소"));
        String stationId   = String.valueOf(item.get("충전소아이디"));
        String detailAddress = String.valueOf(item.get("상세주소"));
        String tel = String.valueOf(item.get("연락처"));
        String useTime = String.valueOf(item.get("이용가능시간"));
        String regDate = String.valueOf(item.get("등록일자"));

        return new StationDTO(latitude, longitude, stationName, address, stationId,
                detailAddress, tel, useTime, regDate);
    }

    // 거리 계산 (km 단위)
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

    // 내 위치 기준 충전소 리스트 (기본 반경 비교 유지)
    @Override
    public List<StationDTO> stations(String lat, String lng, String stationId) {
        List<Map<String, Object>> data = StationData();

        double userLat = Double.parseDouble(lat);
        double userLng = Double.parseDouble(lng);

        return data.stream()
                .filter(item -> {
                    double stLat = Double.parseDouble(String.valueOf(item.get("위도")));
                    double stLng = Double.parseDouble(String.valueOf(item.get("경도")));
                    double dist = distance(userLat, userLng, stLat, stLng);
                    return dist <= 50; // 필요하면 50 -> 3으로 변경하세요 (단위 km)
                })
                .map(this::stationDTO)
                .toList();
    }

    @Override
    public List<StationDTO> searchByName(String keyword) {
        List<Map<String, Object>> data = StationData();
        String kw = (keyword == null) ? "" : keyword.trim();

        return data.stream()
                .filter(item -> {
                    String name = String.valueOf(item.get("충전소명"));
                    String addr = String.valueOf(item.get("충전소주소"));
                    String latStr = String.valueOf(item.get("위도"));
                    String lngStr = String.valueOf(item.get("경도"));

                    return name.contains(kw)
                            || addr.contains(kw)
                            || latStr.contains(kw)
                            || lngStr.contains(kw);
                })
                .map(this::stationDTO)
                .toList();
    }

    @Override
    public List<StationDTO> searchDetail(Long stationId) {
      List<StationDTO> data = getStationById(stationId);
      
      log.info("data : {} " , data);
      
      return data;
        //return data.stream().map(this::stationDTO).toList();
    }

    @Override
    public int insertReview(ReviewDTO reviewDto) {
        return stationDao.insertReview(reviewDto);
    }

    @Override
    public int deleteReview(ReviewDTO reviewDto) {
        log.info("{}", reviewDto.getReviewId());
        return stationDao.deleteReview(reviewDto);
    }

    @Override
    public List<ReviewDTO> findAll(String stationId) {
        return stationDao.findAll(stationId);
    }
}
