package com.kh.pcar.back.exception;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<Map<String, String>> createResponseEntity(RuntimeException e, HttpStatus status) {
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuth(CustomAuthenticationException e) {

		return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(IdDuplicateException.class)
	public ResponseEntity<?> handlerDuplicateId(IdDuplicateException e) {
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}

	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handlerUsernameNotFound(UsernameNotFoundException e) {
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	
	 @ExceptionHandler(InvalidParameterException.class)
	   public ResponseEntity<?> handlerInvalidParameter(InvalidParameterException e){
	      return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	   }
	 
	 @ExceptionHandler(MemberJoinException.class)
	 public ResponseEntity<Map<String, String>> handleMemberJoin(MemberJoinException e) {
	     return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 

	 @ExceptionHandler(LoginException.class)
	 public ResponseEntity<Map<String, String>> handleLogin(LoginException e) {
	     return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<Map<String, String>> handleValidException(MethodArgumentNotValidException e) {
	     String msg = e.getBindingResult()
	             .getAllErrors()
	             .get(0)
	             .getDefaultMessage();

	     return ResponseEntity
	             .status(HttpStatus.BAD_REQUEST)
	             .body(Map.of("error-message", msg));
	 }
	 
	 @ExceptionHandler(UserNotFoundException.class)
	 public ResponseEntity<Map<String, String>> handlerUserNotFoundException(UserNotFoundException e) {
		 log.warn("사용자 찾기 실패 : {} ", e.getMessage());
		 return createResponseEntity(e, HttpStatus.NOT_FOUND);
	 }
	 
	 @ExceptionHandler(CarNotFoundException.class)
	 public ResponseEntity<Map<String, String>> handlerCarNotFoundException(CarNotFoundException e) {
		 log.warn("차량 번호를 찾을 수 없음 : {} ", e.getMessage());
		 
		 return createResponseEntity(e, HttpStatus.NOT_FOUND);
	 }
	 
	 @ExceptionHandler(DataIntegrityViolationException.class)
	 public ResponseEntity<Map<String, String>> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
		 log.error("DB 무결성 제약 조건 위반: {}", e.getMessage());
		 return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	 }
	 
	 @ExceptionHandler(IllegalStateException.class)
	 public ResponseEntity<Map<String, String>> handlerIllegalState(IllegalStateException e) {
		 return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	 }
}
