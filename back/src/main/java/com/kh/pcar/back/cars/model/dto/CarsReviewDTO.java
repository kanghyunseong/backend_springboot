package com.kh.pcar.back.cars.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarsReviewDTO {
	private Long reviewNo;
	private Long reservationNo;
	private Long reviewWriter;
	private String userName; 
	private Long refCarId;
	@Size(max = 2000, message = "리뷰 내용은 2000자 이내로 입력해주세요.")
	private String reviewContent;
	private Date createDate;
	private String reviewStatus;
}
