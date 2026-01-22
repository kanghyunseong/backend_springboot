package com.kh.pcar.back.station.model.service;

import java.net.URI;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.exception.CustomAuthenticationException;
import com.kh.pcar.back.exception.HttpClientErrorException;
import com.kh.pcar.back.exception.ReservationNotFoundException;
import com.kh.pcar.back.station.model.dao.StationDAO;
import com.kh.pcar.back.station.model.dto.ReviewDTO;
import com.kh.pcar.back.station.model.dto.StationDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceStationImpl implements ServiceStation {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper = new ObjectMapper();
	private final StationDAO stationDao;

	@Value("${charge.client.id}")
	private String chargeClientId;

	@Value("${charge.redirect.url}")
	private String chargeRedirectUrl;

	private List<Map<String, Object>> stationData() {

		String url = chargeRedirectUrl + "&perPage=300&" + chargeClientId;

		URI uri = URI.create(url);

		try {

			String response = restTemplate.getForObject(uri, String.class);

			Map<String, Object> root = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
			});

			Object dataObj = root.get("data");
			if (dataObj == null)
				return Collections.emptyList();

			return (List<Map<String, Object>>) dataObj;
		} catch (JsonProcessingException e) {
			throw new HttpClientErrorException("충전소 API 응답 파싱 실패");
		}

	}

	@Override
	public List<StationDTO> stations(String lat, String lng) {
		List<Map<String, Object>> data = stationData();

		double userLat = Double.parseDouble(lat);
		double userLng = Double.parseDouble(lng);

		return data.stream().filter(item -> {
			double stLat = Double.parseDouble(String.valueOf(item.get("위도")));
			double stLng = Double.parseDouble(String.valueOf(item.get("경도")));
			double dist = distance(userLat, userLng, stLat, stLng);
			return dist <= 5;
		}).map(this::stationDTO).toList();
	}

	@Override
	@Valid
	public int deleteReview(ReviewDTO reviewDto, CustomUserDetails userDetails) {

		reviewDto.setUserNo(stationDao.searchDetail(reviewDto.getReviewId()));

		if (!Objects.equals(reviewDto.getUserNo(), userDetails.getUserNo())) {
			throw new CustomAuthenticationException("로그인한 유저와  게시글 글 작성자와 다릅니다.");
		}
		return stationDao.deleteReview(reviewDto);
	}

	@Override
	public List<ReviewDTO> findAll(String stationId) {
		return stationDao.findAll(stationId);
	}

	/**
	 * stationId로 특정 충전소 조회
	 */
	private List<StationDTO> getStationById(Long stationId) {
		if (stationId == null) {
			throw new InvalidParameterException("stationId가 필요합니다.");
		}

		List<Map<String, Object>> dataList = stationData();

		List<StationDTO> result = dataList.stream()
				.filter(item -> String.valueOf(item.get("충전소아이디")).equals(String.valueOf(stationId)))
				.map(this::stationDTO).toList();

		if (result.isEmpty()) {
			throw new ReservationNotFoundException("해당 충전소를 찾을 수 없습니다. stationId=" + stationId);
		}

		return result;
	}

	@Override
	public List<StationDTO> searchStation(String keyword) {
		List<Map<String, Object>> data = stationData();
		String kw = (keyword == null) ? "" : keyword.trim();
		
		List<StationDTO> stations =	data.stream().filter(item -> {
			String name = String.valueOf(item.get("충전소명"));
			String addr = String.valueOf(item.get("충전소주소"));
			String latStr = String.valueOf(item.get("위도"));
			String lngStr = String.valueOf(item.get("경도"));
			return name.contains(kw) || addr.contains(kw) || latStr.contains(kw) || lngStr.contains(kw);
		}).map(this::stationDTO).toList();
		
		if (stations.isEmpty()) {
		    throw new InvalidParameterException("검색결과를 찾을수 없습니다.");
		}
		return stations; 
	}

	@Override
	public List<StationDTO> searchDetail(Long stationId) {
		return getStationById(stationId);
	}

	@Override
	@Valid
	public int insertReview(ReviewDTO reviewDto, CustomUserDetails userDetails) {

		if (reviewDto == null) {
			throw new InvalidParameterException("필수 파라미터가 누락되었습니다.");
		}

		reviewDto.setUserNo(userDetails.getUserNo());

		int result = stationDao.insertReview(reviewDto);

		if (result < 0) {
			throw new RuntimeException("리뷰 등록을 하지 못했습니다.");
		} else {

			return result;
		}
	}

	//거리 계산
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double r = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return r * c;
	}

	//Map->StationDTO
	private StationDTO stationDTO(Map<String, Object> item) {
		return StationDTO.getStation(item);
	}

}