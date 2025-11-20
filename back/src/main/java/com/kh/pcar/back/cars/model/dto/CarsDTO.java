package com.kh.pcar.back.cars.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarsDTO {
	private Long carId;
	private String carName;
	private String carContent;
	private String carSeet;
	private String carSize;
	private Long battery;
	private String carImage;
	private String carStatus;
	private Long carDriving;
	private Long carEfficiency;
}
