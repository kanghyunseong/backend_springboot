package com.kh.pcar.back.admin.cars.model.service;

import java.util.List;



import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.admin.cars.model.dao.AdminCarMapper;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarDTO;
import com.kh.pcar.back.admin.cars.model.dto.AdminCarPageResponseDTO;
import com.kh.pcar.back.admin.user.model.dto.UserPageResponseDTO;
import com.kh.pcar.back.util.PageInfo;
import com.kh.pcar.back.util.Pagenation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {
	
	private final AdminCarMapper adminCarMapper;
	private final Pagenation pagenation;

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

}
