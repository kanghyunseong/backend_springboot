package com.kh.pcar.back.boards.Report.service;

import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.vo.ReportVO;

public interface ReportService {
	
	ReportVO report(Long reporterNo, ReportDTO report);
	
}
