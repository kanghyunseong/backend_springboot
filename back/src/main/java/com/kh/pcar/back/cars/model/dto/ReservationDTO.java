package com.kh.pcar.back.cars.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationDTO {
	private Long reservationNo;
	private Long userNo;
	private Long carId;
	private Date reservationDate;
	private Date startTime;
	private Date endTime;
	@Size(max = 50, message = "목적지는 50자 이내로 입력해주세요.")
	private String destination;
	private String reservationStatus;
	private String returnStatus;
	
}
