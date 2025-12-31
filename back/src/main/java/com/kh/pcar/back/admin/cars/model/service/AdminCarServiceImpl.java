package com.kh.pcar.back.admin.cars.model.service;


import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.cars.model.dao.AdminCarMapper;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarsReservationDTO;
import com.kh.pcar.back.exception.CarNotFoundException;
import com.kh.pcar.back.exception.ReservationNotFoundException;
import com.kh.pcar.back.util.PageInfo;
import com.kh.pcar.back.util.Pagenation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {

	private final AdminCarMapper adminCarMapper;
	private final Pagenation pagenation;
	private final FileSaveService fileSaveService;

	@Override
	@Transactional(readOnly = true)
	public AdminCarPageResponseDTO findAllCars(int currentPage) {

		int totalCount = adminCarMapper.getTotalCount();

		int boardLimit = 10;
		int pageLimit = 5;

		PageInfo pi = pagenation.getPageInfo(totalCount, currentPage, boardLimit, pageLimit);

		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		List<AdminCarDTO> cars = adminCarMapper.findAllCars(rowBounds);

		return new AdminCarPageResponseDTO(pi, cars);
	}

	@Override
	@Transactional(rollbackFor = Exception.class) 
	public void registerCar(AdminCarDTO carDTO, MultipartFile file) {
	    try {
	        if (file != null && !file.isEmpty()) {
	            String imgUrl = fileSaveService.saveFile(file);
	            carDTO.setCarImage(imgUrl);
	        }
	        
	        adminCarMapper.insertCar(carDTO);
	        
	    } catch (IOException e) {
	        log.error("차량 등록 중 S3 업로드 실패 : {}", e.getMessage());
	        throw new RuntimeException("이미지 서버 업로드에 실패하여 차량 등록이 취소되었습니다.", e);
	    } catch (Exception e) {
	        log.error("차량 등록 중 예상치 못한 오류 발생 : {}", e.getMessage());
	        throw new RuntimeException("차량 등록 처리 중 시스템 오류가 발생했습니다.", e);
	    }
	}

	@Override
	@Transactional
	public void updateCar(AdminCarDTO carDTO, MultipartFile file) {
		try {
			if (file != null && !file.isEmpty()) {
				String imgUrl = fileSaveService.saveFile(file);
				carDTO.setCarImage(imgUrl);
			}
			adminCarMapper.updateCar(carDTO);
		} catch (IOException e) {
			throw new RuntimeException("차량 수정 중 파일 저장 실패", e);
		}
	}

	@Override
	public AdminCarDTO findCarById(Long carId) {
		AdminCarDTO carDTO = adminCarMapper.findCarById(carId);
		if (carDTO == null) {
			throw new CarNotFoundException("차량 ID " + carId + "에 대한 정보를 찾을 수 없습니다.");
		}
		return carDTO;
	}

	@Override
	@Transactional 
	public void deleteCarById(Long carId) {
		int result = adminCarMapper.updateCarStatus(carId);
		if (result == 0) {
			throw new CarNotFoundException("차량 ID " + carId + "를 찾을 수 없습니다.");
		}
	}

	@Override
	public List<AdminCarsReservationDTO> findAllReservations() {
		List<AdminCarsReservationDTO> list = adminCarMapper.findAllReservations();

		if (list == null || list.isEmpty()) {
			throw new ReservationNotFoundException("조회된 예약 내역이 없습니다.");
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getDailyReservationStats() {
		return adminCarMapper.getDailyReservationStats();
	}

	@Override
	@Transactional
	public void cancelReservation(Long reservationNo) {
		int result = adminCarMapper.updateReservationStatus(reservationNo, "N");
		if (result == 0) {
			throw new ReservationNotFoundException("예약 ID " + reservationNo + "를 찾을 수 없거나 취소할 수 없는 상태입니다.");
		}
	}
}