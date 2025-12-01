package com.kh.pcar.back.admin.Enviroments.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.pcar.back.admin.Enviroments.model.dto.AdminEnviromentsDTO;

@Mapper
public interface AdminEnviromentsMapper {

	@Select({
        "SELECT",
        "    u.USER_NAME AS name,",
        "    COUNT(r.RESERVATION_NO) AS reservationCount,",
        "    SUM((r.END_TIME - r.START_TIME) * 24) AS totalUsageHours, ", 
        "    ROUND(",
        "        SUM(CASE WHEN r.RETURN_STATUS = 'Y' AND R.END_TIME >= R.END_TIME THEN 1 ELSE 0 END) * 100 / ", 
        "        COUNT(r.RESERVATION_NO)",
        "    , 1) AS onTimeReturnRate", 
        "FROM",
        "    TB_RESERVATION r",
        "JOIN",
        "    TB_MEMBER u ON r.USER_NO = u.USER_NO",
        "WHERE",
        "    r.RESERVATION_STATUS = 'Y'", 
        "GROUP BY",
        "    u.USER_NAME",
        "ORDER BY",
        "    reservationCount DESC, u.USER_NAME ASC"
    })
	List<AdminEnviromentsDTO> findUserRankings();

}
