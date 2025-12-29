package com.kh.pcar.back.admin.cars.model.dto;

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
@ToString
@Builder
public class AdminCarDTO {
	
	private Long carId;
	private String carName;
	private String carContent;
	private String carSeet;
	private String carSize;
	private Double battery;
	private String carImage;
	private String carStatus;
	private Long carDriving;
	private Double carEfficiency;
	

}
