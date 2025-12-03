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

@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {
	
	private final AdminCarMapper adminCarMapper;
	private final Pagenation pagenation;
	private final FileSaveService fileSaveService;

	@Override
	public AdminCarPageResponseDTO findAllCars(int currentPage) {
		
		int totalCount = adminCarMapper.getTotalCount();
		
		int boardLimit = 10; // 한 페이지에 10개씩
        int pageLimit = 5;
		
        PageInfo pi = pagenation.getPageInfo(
                totalCount, 
                currentPage, 
                boardLimit, 
                pageLimit
            );
        
        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
        
		List<AdminCarDTO> cars = adminCarMapper.findAllCars(rowBounds);
		
		return new AdminCarPageResponseDTO(pi, cars);
        
		
	}

	@Override
	public void registerCar(AdminCarDTO carDTO, MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			String imgUrl = fileSaveService.saveFile(file);
			carDTO.setCarImage(imgUrl);
		}
		adminCarMapper.insertCar(carDTO);
	}

	@Override
    public void updateCar(AdminCarDTO carDTO, MultipartFile file) throws IOException {
        // 새 파일이 있을 때만 저장하고 DTO 업데이트
        // 파일이 없으면 carDTO.carImage는 null 상태로 넘어감 -> Mapper에서 처리
        if (file != null && !file.isEmpty()) {
            String imgUrl = fileSaveService.saveFile(file);
            carDTO.setCarImage(imgUrl);
        }
        adminCarMapper.updateCar(carDTO);
    }

	@Override
	public AdminCarDTO findCarById(Long carId) {
		AdminCarDTO carDTO = adminCarMapper.findCarById(carId);
		
		if (carDTO == null) {
	        throw new CarNotFoundException("차량 ID " + carId + "에 대한 정보를 찾을 수 없습니다.");
	    }
	    
	    // 3. DTO 반환
	    return carDTO;
		
	}

	@Override
	public void deleteCarById(Long carId) {
		int result = adminCarMapper.updateCarStatus(carId);
		
		if(result == 0) {
			throw new CarNotFoundException("차량 ID" + carId + "를 찾을 수 없습니다.");
		}
		
	}

	@Override
	public List<AdminCarsReservationDTO> findAllReservations() {
		
		List<AdminCarsReservationDTO> list = adminCarMapper.findAllReservations();
		
		if(list == null || list.isEmpty()) {
			throw new ReservationNotFoundException("조회된예약 내역이 없습니다.");
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
	
		if(result == 0) {
			throw new ReservationNotFoundException("예약 ID " + reservationNo + "를 찾을 수 없거나 취소할 수 없는 상태입니다.");
		}
	}

	

	
	
}
