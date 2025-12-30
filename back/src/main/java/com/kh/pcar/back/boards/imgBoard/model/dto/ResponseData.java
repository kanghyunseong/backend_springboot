package com.kh.pcar.back.boards.imgBoard.model.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData {
	private String message;
	private Object data;
	private String success;
	
	private ResponseData(String message, Object data, String success) {
		this.message = message;
		this.success = success;
		this.data = data;
	}
	  
	// 성공 응답
	public static <T> ResponseEntity<ResponseData> ok(Object data) {
		return ResponseEntity.ok(new ResponseData(null, data, "요청 성공"));
	}
	
	public static <T> ResponseEntity<ResponseData> ok(Object data, String message) {
		return ResponseEntity.ok(new ResponseData(message, data, "요청 성공"));
	}
	
	public static <T> ResponseEntity<ResponseData> created(Object data) {
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseData("생성되었습니다.", data, "요청 성공"));
	}
	
	// 실패 응답
	public static <T> ResponseEntity<ResponseData> badRequest(String message, HttpStatus status) {
		return ResponseEntity.status(status).body(new ResponseData(message, null, "요청 실패"));
	}
}
