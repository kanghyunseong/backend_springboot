package com.kh.pcar.back.exception;

import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 공통 응답 포맷 (Exception 전체 커버)
    private ResponseEntity<Map<String, String>> createResponseEntity(Exception e, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", e.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    /* ===================== 인증 / 인가 ===================== */

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuth(CustomAuthenticationException e) {
        return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    // (선택) 작성자만 수정/삭제 같은 권한 예외 쓰고 있으면 사용
    @ExceptionHandler(CustomAuthorizationException.class)
    public ResponseEntity<Map<String, String>> handleAuthorization(CustomAuthorizationException e) {
        return createResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", "접근 권한이 없습니다. (관리자 전용)");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Map<String, String>> handleLogin(LoginException e) {
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

    /* ===================== 회원 / 예약 / 차량 / 게시판 도메인 예외 ===================== */

    @ExceptionHandler(IdDuplicateException.class)
    public ResponseEntity<Map<String, String>> handlerDuplicateId(IdDuplicateException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerUsernameNotFound(UsernameNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerUserNotFoundException(UserNotFoundException e) {
        log.warn("사용자 찾기 실패 : {} ", e.getMessage());
        return createResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerReservationNotFoundException(ReservationNotFoundException e) {
        log.warn("예약 내역 조회 실패 : {} ", e.getMessage());
        return createResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerCarNotFoundException(CarNotFoundException e) {
        log.warn("차량 번호를 찾을 수 없음 : {} ", e.getMessage());
        return createResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerBoardsNotFoundException(BoardsNotFoundException e) {
        log.error("삭제 실패 : {}", e.getMessage());
        return createResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlerNotFoundException(NoticeNotFoundException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberJoinException.class)
    public ResponseEntity<Map<String, String>> handleMemberJoin(MemberJoinException e) {
        return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ===================== 요청 검증 / 파라미터 / JSON ===================== */

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, String>> handlerInvalidParameter(InvalidParameterException e) {
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", "잘못된 요청 형식입니다. (입력 값 타입을 확인해주세요)");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonErrors(HttpMessageNotReadableException e) {
        log.warn("JSON 파싱 오류: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error-message", "요청 데이터(JSON)의 형식이 올바르지 않습니다. 오타나 쉼표를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /* ===================== DB / 외부 API / 네트워크 / 파일 ===================== */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handlerDataIntegrityViolationException(
            DataIntegrityViolationException e) {
        log.error("DB 무결성 제약 조건 위반: {}", e.getMessage());
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.warn("파일 업로드 용량 초과: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error-message", "업로드 가능한 파일 크기를 초과했습니다. (최대 10MB)");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    /* ===================== 공통 Runtime / Exception ===================== */

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handlerIllegalState(IllegalStateException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 오류 발생: ", e);
        // 여기까지 내려오는 건 "별도 핸들러 안 만든 RuntimeException" 들
        return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        log.error("알 수 없는 서버 오류 발생: ", e);
        Map<String, String> error = new HashMap<>();
        error.put("error-message", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(AlreadyReportedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyReported(AlreadyReportedException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error-message", e.getMessage()); // "이미 신고한 대상입니다."
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return createResponseEntity(e, HttpStatus.BAD_REQUEST);
    }
}
