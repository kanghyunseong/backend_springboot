package com.kh.pcar.back.admin.cars.model.dto;

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
public class AdminCarsReservationDTO {
	
	private Long reservationNo;
	private String customer;    
    private String affiliation;
    private String car;
    private String userId;
    private String phone;
    private String start;
    private String end;
    private String status;

}
