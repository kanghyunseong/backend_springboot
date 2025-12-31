package com.kh.pcar.back.main.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.main.model.dto.PopularCarDTO;

@Mapper
public interface MainMapper {

	@Select("SELECT COUNT(*) FROM TB_CAR WHERE CAR_STATUS = 'Y'")
	String countCar();

	@Select("SELECT COUNT(*) FROM TB_RESERVATION")
	String countReservation();

	@Select("SELECT COUNT(*) FROM TB_MEMBER")
	String countMember();

	@Select("SELECT COUNT(*) FROM TB_CAR C LEFT JOIN TB_RESERVATION R ON C.CAR_ID = R.CAR_ID WHERE R.RESERVATION_STATUS = 'Y'")
	String countRentalCars();
	
	@Select("""
			SELECT
				   c.CAR_ID carId,
			       c.CAR_NAME carName,
			       c.CAR_IMAGE carImage,
			       COUNT(r.RESERVATION_NO) AS RESERVATION_COUNT
			  FROM
			       TB_CAR c
			  JOIN
			       TB_RESERVATION r
			    ON
			       c.CAR_ID = r.CAR_ID
			 WHERE
			       r.RESERVATION_STATUS = 'N'
			 GROUP BY
			       c.CAR_ID, c.CAR_NAME, c.CAR_CONTENT, c.CAR_IMAGE
			 ORDER BY
			       RESERVATION_COUNT DESC
			 FETCH
			       FIRST 4 ROWS ONLY
			""")
	List<PopularCarDTO> findPopularCar();

}
