package com.kh.pcar.back.exception;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	 
	 @ExceptionHandler(ReservationNotFoundException.class)
	    public ResponseEntity<Map<String, String>> handlerReservationNotFoundException(ReservationNotFoundException e) {
	        log.warn("예약 내역 조회 실패 : {} ", e.getMessage());
	        return createResponseEntity(e, HttpStatus.NOT_FOUND);
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
	 @ExceptionHandler( URISyntaxException.class)
	 public ResponseEntity<Map<String,String>> handlerURISyntaxException( URISyntaxException e) {
//		 log.warn("URL 문법 오류 : {}" , e.getMessage());
		 
		 return createResponseEntity(e,HttpStatus.BAD_REQUEST);
		 
	 }
	 @ExceptionHandler(HttpClientErrorException.class)
	 public ResponseEntity<Map<String,String>> handlerHttpClientErrorException(HttpClientErrorException e){
		 log.warn("외부 API버서 오류: {} ",e.getMessage()); 
//		 warn 노랑색으로 찍심 error 빨간 색오류 뜨는 거 INFO 오류 없음
		 return createResponseEntity(e,HttpStatus.BAD_REQUEST);
	 }
	 @ExceptionHandler(ResourceAccessException.class)
	 public ResponseEntity<Map<String,String>> handlerResourceAccessException(ResourceAccessException e){
		 log.error("네트워크 오류{}",e.getMessage());
		 return createResponseEntity(e,HttpStatus.BAD_REQUEST);
	 }
	 @ExceptionHandler( JsonProcessingException.class)
	 public ResponseEntity<Map<String , String>> hadlerJsonProcessingException(JsonProcessingException e) {
		 log.warn("JSON 파싱 오류 {}",e.getMessage());
		 return  createResponseEntity(e,HttpStatus.BAD_REQUEST);
	 }
	 

	 @ExceptionHandler(NoticeNotFoundException.class)
	 public ResponseEntity<Map<String, String>> handlerNotFoundException(NoticeNotFoundException e) {
		 return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	 }

	 @ExceptionHandler(NaverAuthException.class)
	 public ResponseEntity<Map<String, String>> handlerNaverAuthException(NaverAuthException e) {
		 log.error("Naver인증 실패 : {} " , e.getMessage());
	     return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	 }
	 
	 @ExceptionHandler(KakaoAuthException.class)
	 public ResponseEntity<Map<String, String>> handlerKakaoAuthException(KakaoAuthException e) {
	     log.error("Kakao 인증 실패: {}", e.getMessage());
	     return createResponseEntity(e, HttpStatus.UNAUTHORIZED);

	 }
}
