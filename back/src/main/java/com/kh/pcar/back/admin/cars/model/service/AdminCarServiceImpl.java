package com.kh.pcar.back.admin.cars.model.service;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.admin.cars.model.dao.AdminCarMapper;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
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
        
        // 4. RowBounds 생성 (몇 개 건너뛰고(offset), 몇 개 가져와라(limit))
        int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
        RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
        
        // 5. Mapper 호출 (RowBounds 전달)
		List<AdminCarDTO> cars = adminCarMapper.findAllCars(rowBounds);
		
        // 6. PageInfo 객체와 List<UserDTO>를 하나의 응답 DTO로 묶어서 반환
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
	public Object findCarById(Long carId) {
		// TODO Auto-generated method stub
		return null;
	}

}
