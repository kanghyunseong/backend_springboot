package com.kh.pcar.back.station.model.dto;




import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	@NotBlank(message="아이디 값은 비어있을 수 없습니다.")
	private String commentContent;
	@NotBlank(message="내용 부분은 비어있을 수 없습니다.")
	@Size(min=10,max=50, message="내용은 10글자 이상 50글자 이하만 사용할 수 있습니다.")
	private String recommend;
	private String stationId;
	@NotBlank(message="충전소 아이디 값은 비어있을 수 없습니다.")
	private Long userNo;
	
	private Date createdAt;
	
	private String notRecommend;
	

}
