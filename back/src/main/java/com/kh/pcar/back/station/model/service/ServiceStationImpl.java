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
	private final StationDAO stationDao;

	@Value("${charge.client.id}")
	private String chargeClientId;

	@Value("${charge.redirect.url}")
	private String chargeRedirectUrl;

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * API 응답에서 "data" 필드(배열)를 Map 리스트로 안전하게 반환
	 */
	private List<Map<String, Object>> StationData() {
		String url = chargeRedirectUrl + "&perPage=300&" + chargeClientId;
		URI uri = URI.create(url);

		// RestTemplate에서 발생하는 예외는 전역 핸들러에서 처리됨
		String response = restTemplate.getForObject(uri, String.class);

		// JSON 파싱 실패 시 HttpClientErrorException 발생
		Map<String, Object> root;
		try {
			root = mapper.readValue(response, new TypeReference<Map<String, Object>>() {
			});
		} catch (JsonProcessingException e) {
			throw new HttpClientErrorException("충전소 API 응답 파싱 실패");
		}

		Object dataObj = root.get("data");
		if (dataObj == null)
			return Collections.emptyList();

		return mapper.convertValue(dataObj, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * stationId로 특정 충전소 조회
	 */
	private List<StationDTO> getStationById(Long stationId) {
		if (stationId == null) {
			throw new InvalidParameterException("stationId가 필요합니다.");
		}

		List<Map<String, Object>> dataList = StationData();

		List<StationDTO> result = dataList.stream()
				.filter(item -> String.valueOf(item.get("충전소아이디")).equals(String.valueOf(stationId)))
				.map(this::stationDTO).toList();

		if (result.isEmpty()) {
			throw new ReservationNotFoundException("해당 충전소를 찾을 수 없습니다. stationId=" + stationId);
		}

		return result;
	}

	/**
	 * Map → StationDTO 변환
	 */
	private StationDTO stationDTO(Map<String, Object> item) {
		String latitude = String.valueOf(item.get("위도"));
		String longitude = String.valueOf(item.get("경도"));
		String stationName = String.valueOf(item.get("충전소명"));
		String address = String.valueOf(item.get("충전소주소"));
		String stationId = String.valueOf(item.get("충전소아이디"));
		String detailAddress = String.valueOf(item.get("상세주소"));
		String tel = String.valueOf(item.get("연락처"));
		String useTime = String.valueOf(item.get("이용가능시간"));
		String regDate = String.valueOf(item.get("등록일자"));

		return new StationDTO(latitude, longitude, stationName, address, stationId, detailAddress, tel, useTime,
				regDate);
	}

	/**
	 * 두 좌표 사이 거리 계산 (km)
	 */
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double R = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	@Override
	public List<StationDTO> stations(String lat, String lng, String stationId) {
		List<Map<String, Object>> data = StationData();

		double userLat = Double.parseDouble(lat);
		double userLng = Double.parseDouble(lng);

		return data.stream().filter(item -> {
			double stLat = Double.parseDouble(String.valueOf(item.get("위도")));
			double stLng = Double.parseDouble(String.valueOf(item.get("경도")));
			double dist = distance(userLat, userLng, stLat, stLng);
			return dist <= 3; // 단위 km
		}).map(this::stationDTO).toList();
	}

	@Override
	public List<StationDTO> searchByName(String keyword) {
		List<Map<String, Object>> data = StationData();
		String kw = (keyword == null) ? "" : keyword.trim();

		return data.stream().filter(item -> {
			String name = String.valueOf(item.get("충전소명"));
			String addr = String.valueOf(item.get("충전소주소"));
			String latStr = String.valueOf(item.get("위도"));
			String lngStr = String.valueOf(item.get("경도"));
			return name.contains(kw) || addr.contains(kw) || latStr.contains(kw) || lngStr.contains(kw);
		}).map(this::stationDTO).toList();
	}

	@Override
	public List<StationDTO> searchDetail(Long stationId) {
		return getStationById(stationId);
	}

	@Override
	@Valid
	// 사용자가 이 작업을 할 권한이 있는지 검증
	public Long insertReview(ReviewDTO reviewDto, CustomUserDetails userDetails) {

		if (reviewDto == null) {
			throw new InvalidParameterException("필수 파라미터가 누락되었습니다.");
		}

		int result = reviewDto.setUserNo(userDetails.getUserNo());

		return result;
	}

	@Override
	@Valid
	// 사용자가 이 작업을 할 권한이 있는지 검증
	public int deleteReview(ReviewDTO reviewDto, CustomUserDetails userDetails) {
		log.info("{} , {}", reviewDto, userDetails);
		reviewDto.setUserNo(stationDao.searchDetail(reviewDto.getReviewId()));

		log.info(" {} ", reviewDto);
		// Long타입은 비교 연산자를 사용해서 비교할 수 없음 따라서 Objects.equals를 사용해야 함
		if (!Objects.equals(reviewDto.getUserNo(), userDetails.getUserNo())) {
			throw new CustomAuthenticationException("로그인한 유저와  게시글 글 작성자와 다릅니다.");
		}
		return stationDao.deleteReview(reviewDto);
	}

	@Override
	public List<ReviewDTO> findAll(String stationId) {

		return stationDao.findAll(stationId);
	}
}
