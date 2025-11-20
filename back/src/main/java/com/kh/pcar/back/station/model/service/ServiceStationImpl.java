package com.kh.pcar.back.station.model.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.pcar.back.station.model.dto.MyStationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceStationImpl implements ServiceStation {
	private RestTemplate restTemplate = new RestTemplate();
	private double distance(double lat1, double lon1, double lat2, double lon2) {
	    double R = 6371; // 지구 반지름 (km)

	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(dLon / 2) * Math.sin(dLon / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return R * c;
	}
	@Override
	public List<MyStationDTO> stations(String lat, String lng) {
		
		//인증키
		//충전소 위치 띄어줌
		final String API_KEY ="?serviceKey=379f167eb3f41af06081d27f407899ed21955011b09d34a54e3519d8544a89cb&perPage=300";
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.odcloud.kr/api/15039545/v1/uddi:f8f879ad-68cf-40fb-8ccc-cb36eaf1baca"+ API_KEY);

		URI uri = null;
		try {
			uri = new URI(sb.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		log.info("{}",sb.toString());
		//response==충전소 위치 JSON을 문자열로 받기
		String response = restTemplate.getForObject(uri,String.class);
		//ObjectMapper = JSON을 자바 객체로 바꿔주는 것 Map구조로 변환
		try {
		ObjectMapper mapper = new ObjectMapper();
								//response를 Map형태로 바꾼후 안의 APi로 호출된 값을 읽는다.
		Map<String, Object> map;
			map = mapper.readValue(response,Map.class);
		//data배열 꺼냄
		List<Map<String,Object>> data = (List<Map<String,Object>>) map.get("data");
		//사용자의 좌표 double변환
		double userLat = Double.parseDouble(lat);
		double userLng = Double.parseDouble(lng);
		
		
		List<MyStationDTO> stationList = data.stream()
				.filter(item-> {
					double stLat = Double.parseDouble(item.get("위도").toString());
					double stLng = Double.parseDouble(item.get("경도").toString());
					double dist =  distance(userLat,userLng,stLat,stLng);
					return dist<=3;
				}).map(item ->{
					//map내부에서 꺼내고 싶은 값만 추출
			String latitude = item.get("위도").toString();
			String longitude = item.get("경도").toString();
			String stationName = item.get("충전소명").toString();
		
			return new MyStationDTO(latitude,longitude,stationName);
		})
				.toList();
		return stationList;
		
		
	
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();			
		}
		return List.of();
	}


}
