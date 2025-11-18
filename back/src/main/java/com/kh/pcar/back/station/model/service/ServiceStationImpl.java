package com.kh.pcar.back.station.model.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceStationImpl implements ServiceStation {
	private RestTemplate restTemplate = new RestTemplate();
	@Override
	public String station(String lat, String lng) {
		
		//인증키
		final String API_KEY ="?serviceKey=379f167eb3f41af06081d27f407899ed21955011b09d34a54e3519d8544a89cb";
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.odcloud.kr/api/15039545/v1/uddi:f8f879ad-68cf-40fb-8ccc-cb36eaf1baca/"+ API_KEY);

		URI uri = null;
		try {
			uri = new URI(sb.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		log.info("{}",sb.toString());
		String response = restTemplate.getForObject(uri,String.class);
		log.info(response);
		
		
		
		return "";
	}

}
