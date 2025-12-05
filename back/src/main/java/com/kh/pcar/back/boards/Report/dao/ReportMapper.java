package com.kh.pcar.back.boards.Report.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.pcar.back.boards.Report.vo.ReportVO;

@Mapper
public interface ReportMapper {
	
	void report(ReportVO report);

    ReportVO findByReporterAndTarget(
            @Param("reporter") Long reporter,
            @Param("targetType") String targetType,
            @Param("targetNo") Long targetNo);
}
