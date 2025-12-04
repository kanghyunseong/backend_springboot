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
	public ResponseEntity<?> handlerInvalidParameter(InvalidParameterException e) {
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
		String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error-message", msg));
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
	public ResponseEntity<Map<String, String>> handlerDataIntegrityViolationException(
			DataIntegrityViolationException e) {
		log.error("DB 무결성 제약 조건 위반: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, String>> handlerIllegalState(IllegalStateException e) {
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<Map<String, String>> handlerURISyntaxException(URISyntaxException e) {
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Map<String, String>> handlerHttpClientErrorException(HttpClientErrorException e) {
		log.warn("외부 API버서 오류: {} ", e.getMessage());
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<Map<String, String>> handlerResourceAccessException(ResourceAccessException e) {
		log.error("네트워크 오류{}", e.getMessage());
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<Map<String, String>> hadlerJsonProcessingException(JsonProcessingException e) {
		log.warn("JSON 파싱 오류 {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoticeNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlerNotFoundException(NoticeNotFoundException e) {
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NaverAuthException.class)
	public ResponseEntity<Map<String, String>> handlerNaverAuthException(NaverAuthException e) {
		log.error("Naver인증 실패 : {} ", e.getMessage());
		return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(KakaoAuthException.class)
	public ResponseEntity<Map<String, String>> handlerKakaoAuthException(KakaoAuthException e) {
		log.error("Kakao 인증 실패: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BoardsNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlerBoardsNotFoundException(BoardsNotFoundException e) {
		log.error("삭제 실패 : {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
		log.error("런타임 오류 발생: ", e);
		return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, String>> handleTypeMismatch(
			org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e) {
		Map<String, String> error = new HashMap<>();
		error.put("error-message", "잘못된 요청 형식입니다. (입력 값 타입을 확인해주세요)");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleException(Exception e) {
		log.error("알 수 없는 서버 오류 발생: ", e);

		Map<String, String> error = new HashMap<>();
		error.put("error-message", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> handleMaxSizeException(
			org.springframework.web.multipart.MaxUploadSizeExceededException e) {
		log.warn("파일 업로드 용량 초과: {}", e.getMessage());

		Map<String, String> error = new HashMap<>();
		error.put("error-message", "업로드 가능한 파일 크기를 초과했습니다. (최대 10MB)");
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
	}
	
	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, String>> handleJsonErrors(org.springframework.http.converter.HttpMessageNotReadableException e) {
	    log.warn("JSON 파싱 오류: {}", e.getMessage());
	    
	    Map<String, String> error = new HashMap<>();
	    error.put("error-message", "요청 데이터(JSON)의 형식이 올바르지 않습니다. 오타나 쉼표를 확인해주세요.");
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class) 
	public ResponseEntity<Map<String, String>> handleAccessDenied(Exception e) {
	    Map<String, String> error = new HashMap<>();
	    error.put("error-message", "접근 권한이 없습니다. (관리자 전용)");
	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}
