package com.kh.pcar.back.main.model.dto;

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
public class PopularCarDTO {

	private Long carId;
	private String carName;
	private String carImage;
	
}
