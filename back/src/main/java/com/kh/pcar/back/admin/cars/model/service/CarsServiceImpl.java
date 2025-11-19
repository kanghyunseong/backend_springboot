package com.kh.pcar.back.admin.cars.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.admin.cars.model.dao.CarsMapper;
import com.kh.pcar.back.admin.cars.model.dto.CarsDTO;
import com.kh.pcar.back.admin.cars.model.dto.CarsPageResponseDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.util.PageInfo;
import com.kh.pcar.back.util.Pagenation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {
	
	private final CarsMapper carsMapper;
	private final Pagenation pagenation;

	@Override
	public CarsPageResponseDTO findAllCars(int currentPage) {
		
		int totalCount = carsMapper.getTotalCount();
		
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
		List<CarsDTO> carss = carsMapper.findAllCars(rowBounds);
		
        // 6. PageInfo 객체와 List<UserDTO>를 하나의 응답 DTO로 묶어서 반환
		return new CarsPageResponseDTO(pi, carss);
        
		
	}

}
