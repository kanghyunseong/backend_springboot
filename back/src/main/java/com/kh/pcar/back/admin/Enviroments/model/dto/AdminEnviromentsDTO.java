package com.kh.pcar.back.admin.Enviroments.model.dto;

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
public class AdminEnviromentsDTO {
	private String name;
    private int reservationCount;
    private double totalUsageHours; 
    private double onTimeReturnRate;
}
