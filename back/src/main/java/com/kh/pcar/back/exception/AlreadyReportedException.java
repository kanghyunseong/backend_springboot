package com.kh.pcar.back.exception;

public class AlreadyReportedException extends RuntimeException {
	public AlreadyReportedException(String message) {
        super(message);
    }
}
