package com.kh.pcar.back.admin.Enviroments.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;

@Mapper
public interface AdminEnviromentsMapper {

	@Select({
	    "SELECT",
	    "    u.USER_NAME AS \"name\",",
	    "    u.USER_ID AS \"userId\",",
	    "    COUNT(r.RESERVATION_NO) AS \"reservationCount\",",
	    "    ROUND(NVL(SUM((r.END_TIME - r.START_TIME) * 24), 0), 1) AS \"totalUsageHours\",",
	    "    ROUND(",
	    "        CASE WHEN COUNT(r.RESERVATION_NO) > 0 ",
	    "        THEN SUM(CASE WHEN r.RETURN_STATUS = 'Y' THEN 1 ELSE 0 END) * 100 / COUNT(r.RESERVATION_NO) ",
	    "        ELSE 0 END, 1) AS \"onTimeReturnRate\"", 
	    "FROM",
	    "    TB_MEMBER u",
	    "LEFT JOIN",
	    "    TB_RESERVATION r ON u.USER_NO = r.USER_NO", 
	    "GROUP BY",
	    "    u.USER_NAME, u.USER_ID",
	    "ORDER BY",
	    "    \"reservationCount\" DESC, u.USER_NAME ASC"
	})
	
	List<AdminEnviromentsDTO> findUserRankings();

}
