package com.kh.pcar.back.admin.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kh.pcar.back.admin.cars.model.dao.AdminCarMapper;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.service.AdminCarServiceImpl;
import com.kh.pcar.back.exception.CarNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AdminCarsServiceTest {

	@Mock
	private AdminCarMapper adminCarMapper;

	@InjectMocks
	private AdminCarServiceImpl adminCarService;

	private final Long testCarId = 20L;
	private AdminCarDTO mockCar;
//	private Long carId;
//	private String carName;
//	private String carContent;
//	private String carSeet;
//	private String carSize;
//	private Double battery;
//	private String carImage;
//	private String carStatus;
//	private Long carDriving;
//	private Double carEfficiency;

	@BeforeEach
	void setup() {
		mockCar = AdminCarDTO.builder()
			                			.carId(testCarId)
			                			.carName("차량1")
			                			.carContent("차량 설명")
			                			.carSeet("4인승")
			                			.carSize("대형")
			                			.battery(100.0)
			                			.carImage("차량 사진")
			                			.carStatus("Y")
			                			.carDriving(400L)
			                			.carEfficiency(4.3)
			               			 	.build();
	}

	@Test
	@DisplayName("차량을 등록할 떄 사용하는 테스트 코드")
	void 차량등록() {

//		// given
//		Long carId = (long) 20;
//		String carName = "차량1";
//		String carContent = "차량 설명";
//		String carSeet = "4인승";
//		String carSize = "대형";
//		Double battery = 100.0;
//		String carImage = "차량 사진";
//		String carStatus = "Y";
//		Long carDriving = (long) 400;
//		Double carEfficiency = 4.3;

//		AdminCarDTO mockCar = AdminCarDTO.builder().carId(carId).carName(carName).carContent(carContent)
//				.carSeet(carSeet).carSize(carSize).battery(battery).carImage(carImage).carStatus(carStatus)
//				.carDriving(carDriving).carEfficiency(carEfficiency).build();

		adminCarService.registerCar(mockCar, null);

		verify(adminCarMapper, times(1)).insertCar(mockCar);

		// 레드 1단계 : 반환타입이 void인 경우에 에러가 발생 -> void의 경우에 verify를 통해 time로 호출 횟수로 검증을 해야함
		// 그린 : 최소구현
		// 리팩토링
	}

	@Test
	@DisplayName("차량 ID로 해당 차량을 조회하는 테스트 코드")
	void carId로조회() {
		Long carId = 20L;

		AdminCarDTO mockCar = new AdminCarDTO();
		mockCar.setCarId(carId);

		when(adminCarMapper.findCarById(carId)).thenReturn(mockCar);

		AdminCarDTO adminCar = adminCarService.findCarById(carId);
		assertThat(adminCar.getCarId()).isEqualTo(carId);
	}

	@Test
	@DisplayName("존재하는 차량을 찾을 때 사용하는 테스트 코드")
	void 차량삭제성공() {
		Long carId = 20L;

		when(adminCarMapper.updateCarStatus(carId)).thenReturn(1);

		adminCarService.deleteCarById(carId);

		verify(adminCarMapper, times(1)).updateCarStatus(carId);

	}

	@Test
	@DisplayName("차량이 존재하지 않을 때 삭제 시도 시 예외 발생")
	void 차량삭제실패() {
		Long carId = 99L;

		when(adminCarMapper.updateCarStatus(carId)).thenReturn(0);

		//adminCarService.deleteCarById(carId);

		// verify(adminCarMapper, times(1)).updateCarStatus(carId);

		assertThrows(CarNotFoundException.class, () -> adminCarService.deleteCarById(carId));
		
		// 레드 1단계 : verify메서드로 호출횟수를 비교하려고했지만 CarId를 찾을 수 없는 CarNotFoundException 커스텀
		// 예외가 발생하였습니다.
		// 그로인해 assertThrow를 통해 예외를 검증해줘야합니다.
		
		

	}

	@Test
	@DisplayName("차량 수정 성공 시 사용하는 테스트 코드")
	void 차량수정() {
//		Long carId = (long) 20;
//		String carName = "차량1";
//		String carContent = "차량 설명";
//		String carSeet = "4인승";
//		String carSize = "대형";
//		Double battery = 100.0;
//		String carImage = "차량 사진";
//		String carStatus = "Y";
//		Long carDriving = (long) 400;
//		Double carEfficiency = 4.3;

//		AdminCarDTO mockCar = AdminCarDTO.builder().carId(carId).carName(carName).carContent(carContent)
//				.carSeet(carSeet).carSize(carSize).battery(battery).carImage(carImage).carStatus(carStatus)
//				.carDriving(carDriving).carEfficiency(carEfficiency).build();
		
		// given
		mockCar.setCarName("변경할 차량명1");
		mockCar.setCarContent("변경할 차량 설명");
		mockCar.setCarSeet("변경할 차량 인승");
		mockCar.setCarSize("변경할 차량 사이즈");
		mockCar.setBattery(90.5);
		mockCar.setCarImage("변경할 차량 이미지");
		mockCar.setCarStatus("Y");
		mockCar.setCarDriving(6000L);
		mockCar.setCarEfficiency(5.3);
		
		adminCarService.updateCar(mockCar, null);
		
		verify(adminCarMapper, times(1)).updateCar(mockCar);
		// 레드 1단계 :
		// -> 해당 차량의 CarId를 찾을 수 없어서 nullPointerException이 발생
		// 레드 2단계 : mockCar를 필드로 선언해서 전역에서 사용할 수 있도록 했어야했는데
		// -> BeforeEach에서 새롭게 다시 정의해서 사용하여 nullPointerException이 발생
	}

	@Test
	@DisplayName("차량 수정 실패했을 때 사용하는 테스트 코드")
	void 차량수정실패() {
		
		doThrow(new NullPointerException()).when(adminCarMapper).updateCar(mockCar);

	    // when & then: 서비스 호출 시 NPE가 발생하는지 검증
	    assertThrows(NullPointerException.class, () -> {
	        adminCarService.updateCar(mockCar, null);
	    });
		// 레드 1단계 : 반환타입이 void여서 when절 오류발생
		
	}

}
