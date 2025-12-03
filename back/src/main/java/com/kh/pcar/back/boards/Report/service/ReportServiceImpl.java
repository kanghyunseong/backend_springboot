package com.kh.pcar.back.boards.Report.service;

import org.springframework.stereotype.Service;

import com.kh.pcar.back.boards.Report.dao.ReportMapper;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.vo.ReportVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	
	private final ReportMapper reportMapper;
	
	@Override
    public ReportVO report(Long reporterNo, ReportDTO report) {

        // 1. 중복 신고 체크
        ReportVO exist = reportMapper.findByReporterAndTarget(reporterNo,
										        		report.getTargetType(),
										        		report.getTargetNo());
        if (exist != null) {
            throw new IllegalStateException("이미 신고한 대상입니다.");
        }

        
        // 2. VO 만들어서 insert
        ReportVO reportVo = ReportVO.builder()
                .targetType(report.getTargetType())
                .targetNo(report.getTargetNo())
                .reportedUser(report.getReportedUser())
                .reporter(reporterNo)      // 로그인한 유저 번호
                .reason(report.getReason())
                .status("Y")
                .rejected("N")
                .build();

        reportMapper.report(reportVo);
        
        return reportVo;
    }

}
