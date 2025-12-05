package com.kh.pcar.back.boards.Report.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReportDTO {
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
