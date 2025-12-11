package com.kh.pcar.back.boards.Report.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReportVO {
	private Long reportNo;
    private String targetType;
    private Long targetNo;
    private Long reportedUser;
    private Long reporter;
    private String reason;
    private String status;
    private String rejected;
    private Date reportDate;
}
