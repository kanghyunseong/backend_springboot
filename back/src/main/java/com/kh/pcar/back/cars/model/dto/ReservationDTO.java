package com.kh.pcar.back.cars.model.dto;

import java.sql.Date;

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
	private String destination;
	private String reservationStatus;
	

}
