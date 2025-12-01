package com.kh.pcar.back.cars.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.cars.model.dto.CarsReviewDTO;

@Mapper
public interface CarsReviewMapper {
	
	@Select("""
	   SELECT
			  R.REVIEW_NO reviewNo
			, R.RESERVATION_NO reservationNo
			, M.USER_NAME userName
			, R.REVIEW_WRITER reviewWriter
			, R.REF_CARID refCarId
			, R.REVIEW_CONTENT reviewContent
			, R.CREATE_DATE createDate
			, R.REVIEW_STATUS reviewStatus 
	     FROM 
			  TB_REVIEW R
		 JOIN 
		      TB_MEMBER M
		   ON 
		      R.REVIEW_WRITER = M.USER_NO
	    WHERE  
		      R.REF_CARID = #{carId}
		  AND 
		      R.REVIEW_STATUS = 'Y'
		ORDER 
		   BY 
		      R.CREATE_DATE DESC
			""")
	List<CarsReviewDTO>findReview(Long carId);
	
}
