package com.kh.pcar.back.exception;

import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kh.pcar.back.common.ResponseData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ===================== 인증 / 인가 ===================== */

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ResponseData<Object>> handleAuth(CustomAuthenticationException e) {
        log.warn("인증 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomAuthorizationException.class)
    public ResponseEntity<ResponseData<Object>> handleAuthorization(CustomAuthorizationException e) {
        log.warn("인가 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData<Object>> handleAccessDenied(AccessDeniedException e) {
        log.warn("접근 권한 거부: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ResponseData<Object>> handleLogin(LoginException e) {
        log.warn("로그인 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NaverAuthException.class)
    public ResponseEntity<ResponseData<Object>> handlerNaverAuthException(NaverAuthException e) {
        log.error("Naver 인증 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(KakaoAuthException.class)
    public ResponseEntity<ResponseData<Object>> handlerKakaoAuthException(KakaoAuthException e) {
        log.error("Kakao 인증 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /* ===================== Not Found ===================== */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerUserNotFoundException(UserNotFoundException e) {
        log.warn("사용자 찾기 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerReservationNotFoundException(ReservationNotFoundException e) {
        log.warn("예약 내역 조회 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerCarNotFoundException(CarNotFoundException e) {
        log.warn("차량 번호를 찾을 수 없음: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardsNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerBoardsNotFoundException(BoardsNotFoundException e) {
        log.error("게시글 찾기 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerNoticeNotFoundException(NoticeNotFoundException e) {
        log.warn("공지사항 찾기 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /* ===================== 회원 / 비즈니스 ===================== */

    @ExceptionHandler(IdDuplicateException.class)
    public ResponseEntity<ResponseData<Object>> handlerDuplicateId(IdDuplicateException e) {
        log.error("아이디 중복 오류: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseData<Object>> handlerUsernameNotFound(UsernameNotFoundException e) {
        log.error("유저 이름 못찾음: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberJoinException.class)
    public ResponseEntity<ResponseData<Object>> handleMemberJoin(MemberJoinException e) {
        log.error("회원가입 실패: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyReportedException.class)
    public ResponseEntity<ResponseData<Object>> handleAlreadyReported(AlreadyReportedException e) {
        log.warn("중복 신고 시도: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.CONFLICT);
    }

    /* ===================== 요청 / 검증 ===================== */

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ResponseData<Object>> handlerInvalidParameter(InvalidParameterException e) {
        log.warn("잘못된 파라미터: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<Object>> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("유효성 검증 실패: {}", msg);
        return ResponseData.failure(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseData<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("타입 불일치: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseData<Object>> handleJsonErrors(HttpMessageNotReadableException e) {
        log.warn("JSON 파싱 오류: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData<Object>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("잘못된 인자: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ResponseData<Object>> handleJsonProcessingException(JsonProcessingException e) {
        log.warn("JSON 처리 오류: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== DB / 외부 / 파일 ===================== */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseData<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e) {
        log.error("DB 무결성 위반: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseData<Object>> handleDataAccess(DataAccessException e) {
        log.error("DB 접근 오류: {}", e.getMessage());
        return ResponseData.failure("DB 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseData<Object>> handleSqlException(SQLException e) {
        log.error("SQL 오류: {}", e.getMessage());
        return ResponseData.failure("SQL 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ResponseData<Object>> handleURISyntaxException(URISyntaxException e) {
        log.warn("URI 문법 오류: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseData<Object>> handleHttpClientErrorException(HttpClientErrorException e) {
        log.warn("외부 API 오류: {}", e.getMessage());
        return ResponseData.failure("외부 API 오류", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ResponseData<Object>> handleResourceAccessException(ResourceAccessException e) {
        log.error("네트워크 오류: {}", e.getMessage());
        return ResponseData.failure("네트워크 오류", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseData<Object>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.warn("파일 업로드 용량 초과: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ResponseData<Object>> handleFileUploadException(FileUploadException e) {
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ResponseData<Object>> handleFileStorageException(FileStorageException e) {
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /* ===================== 공통 ===================== */

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseData<Object>> handleIllegalState(IllegalStateException e) {
        log.warn("잘못된 상태: {}", e.getMessage());
        return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseData<Object>> handleRuntimeException(RuntimeException e) {
        log.error("런타임 오류", e);
        return ResponseData.failure("서버 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleException(Exception e) {
        log.error("알 수 없는 오류", e);
        return ResponseData.failure("알 수 없는 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
