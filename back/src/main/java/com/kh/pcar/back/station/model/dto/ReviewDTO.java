package com.kh.pcar.back.station.model.dto;




import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDTO {
	private Long reviewId;
	private String commentContent;
	private String recommend;
	private String stationId;
	private Long userNo;
	private Date createdAt;
	private String notRecommend;
	

}
