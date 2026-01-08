package com.kh.pcar.back.exception;

public class StationNotFoundException extends RuntimeException {
	public StationNotFoundException(String message) {
        super(message);
    }
}
