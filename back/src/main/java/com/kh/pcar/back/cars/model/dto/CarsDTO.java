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
	private long carId;
	private String carName;
	private String carContent;
	private String carSeet;
	private String carSize;
	private long battery;
	private String carImage;
	private String carStatus;
	private long carDriving;
	private long carEfficiency;
}
