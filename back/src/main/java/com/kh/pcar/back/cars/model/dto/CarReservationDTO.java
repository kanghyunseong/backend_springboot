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
public class CarReservationDTO {
	private ReservationDTO reservation;
	private CarsDTO car;
}
