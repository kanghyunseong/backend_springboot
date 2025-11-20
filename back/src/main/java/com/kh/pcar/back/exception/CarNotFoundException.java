package com.kh.pcar.back.exception;

public class CarNotFoundException extends RuntimeException{

	public CarNotFoundException(String message)  {
		super(message);
	}
	
}
