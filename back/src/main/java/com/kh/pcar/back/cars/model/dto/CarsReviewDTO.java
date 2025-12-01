package com.kh.pcar.back.cars.model.dto;

import java.sql.Date;

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
	private String reviewContent;
	private Date createDate;
	private String reviewStatus;
}
