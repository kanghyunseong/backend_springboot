package com.kh.pcar.back.common;
  
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;  // "success", "fail", "error" 등 상태 구분
    private String message; // 프론트엔드에 띄울 메시지 (예: "등록되었습니다.")
    private T data;         // 실제 데이터 (없으면 null)

    // 1. 성공 시 데이터와 메시지를 같이 보낼 때
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    // 2. 성공했지만 데이터는 없고 메시지만 보낼 때 (등록, 수정, 삭제 등)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>("success", message, null);
    }

    // 3. 실패/에러 시 메시지만 보낼 때
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}

