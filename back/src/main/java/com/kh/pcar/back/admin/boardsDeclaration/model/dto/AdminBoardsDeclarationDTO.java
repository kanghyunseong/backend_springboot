package com.kh.pcar.back.admin.boardsDeclaration.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter             // 필수: JSON 변환 시 필요
@Setter             // 필수: MyBatis가 데이터 넣을 때 필요
@NoArgsConstructor  // 필수
@AllArgsConstructor
@ToString
public class AdminBoardsDeclarationDTO {

    private int reportNo;           // 신고 번호
    private String targetType;      // 대상 종류 (BOARD, COMMENT 등)
    private int targetNo;           // 대상 번호
    
    private int reportedUser;       // 신고 당한 유저 번호 (ID)
    private String reportedUserName;// [추가] 신고 당한 유저 이름 (화면 표시용)
    
    private int reporter;           // 신고자 번호 (ID)
    private String reporterName;    // [추가] 신고자 이름 (화면 표시용)
    
    private String reason;          // 신고 사유
    private String status;          // 처리 상태 (char -> String 권장)
    private String rejected;        // 반려 여부 (char -> String 권장)
    private Date reportDate;        // 신고 날짜
    private String targetTitle;
}