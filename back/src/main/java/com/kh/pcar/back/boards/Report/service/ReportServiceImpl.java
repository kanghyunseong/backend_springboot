package com.kh.pcar.back.boards.Report.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.kh.pcar.back.boards.Report.dao.ReportMapper;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.vo.ReportVO;
import com.kh.pcar.back.exception.AlreadyReportedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public ReportVO report(Long reporterNo, ReportDTO report) {

        // 1. 파라미터 기본 검증
        if (reporterNo == null) {
            throw new IllegalArgumentException("로그인 정보가 없습니다. 다시 로그인 후 시도해주세요.");
        }
        if (report == null) {
            throw new IllegalArgumentException("신고 정보가 비어 있습니다.");
        }
        if (report.getTargetType() == null || report.getTargetType().isBlank()) {
            throw new IllegalArgumentException("신고 대상 타입이 누락되었습니다.");
        }
        if (report.getTargetNo() == null) {
            throw new IllegalArgumentException("신고 대상 번호가 누락되었습니다.");
        }
        if (report.getReportedUser() == null) {
            throw new IllegalArgumentException("신고 당한 사용자 정보가 누락되었습니다.");
        }
        if (report.getReason() == null || report.getReason().isBlank()) {
            throw new IllegalArgumentException("신고 사유를 입력해주세요.");
        }

        try {
            // 2. 중복 신고 체크
            ReportVO exist = reportMapper.findByReporterAndTarget(
                    reporterNo,
                    report.getTargetType(),
                    report.getTargetNo()
            );
            if (exist != null) {
                throw new AlreadyReportedException("이미 신고한 대상입니다.");
            }

            // 3. VO 만들어서 insert
            ReportVO reportVo = ReportVO.builder()
                    .targetType(report.getTargetType())
                    .targetNo(report.getTargetNo())
                    .reportedUser(report.getReportedUser())
                    .reporter(reporterNo)
                    .reason(report.getReason())
                    .status("Y")
                    .rejected("N")
                    .build();

            reportMapper.report(reportVo);
            return reportVo;

        } catch (DataAccessException e) {
            throw new RuntimeException("신고 처리 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }
}
