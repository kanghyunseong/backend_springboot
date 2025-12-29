package com.kh.pcar.back.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;


@Data
@Builder

public class ResponseData <T>{
	private String message;
	private Object data;
	private  String success;
	
	private ResponseData(String message, Object data, String success) {
		this.message = message;
		this.data =data;
		this.success= success;
		
		
	}
	
	 //성공 응답
	   public static <T> ResponseEntity<ResponseData<T>>ok(T data,String message){
		   return ResponseEntity.ok(new ResponseData<T>(message,data,"요청성공"));
		   
	   }
	   public  static <T> ResponseEntity<ResponseData<T>>ok(T data){
		   return ResponseEntity.ok(new ResponseData<T>("요청성공",data, "요청 성공"));
	   }
	   
	   public static <T> ResponseEntity<ResponseData <T>>created(Object object){
		   return ResponseEntity.status(HttpStatus.CREATED)
				   				.body(new ResponseData <T>("생성되었습니다.",object,"요청 성공"));
	   }
	   //실패응답
	   public static <T> ResponseEntity <ResponseData<T>>failure(String message,HttpStatus status){
		   return ResponseEntity.status(status).body(new ResponseData<T> (message,null, "요청실패"));
	   }
	}
	   


