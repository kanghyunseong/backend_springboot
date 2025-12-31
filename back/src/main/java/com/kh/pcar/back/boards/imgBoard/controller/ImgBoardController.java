package com.kh.pcar.back.boards.imgBoard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.Report.dto.ReportDTO;
import com.kh.pcar.back.boards.Report.service.ReportService;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.service.ImgBoardService;
import com.kh.pcar.back.common.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/imgBoards")
@RequiredArgsConstructor
public class ImgBoardController {

    private final ImgBoardService imgBoardService;
    private final ReportService reportService;

    // 게시글 작성 + 첨부파일
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(
            @Valid ImgBoardDTO imgBoard,
            @RequestParam(name = "files", required = false) MultipartFile[] files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("게시글 정보 : {}, 업로드 파일 개수 : {}", imgBoard, (files != null ? files.length : 0));
        imgBoardService.imgSave(imgBoard, files, userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글이 등록되었습니다."));
    }

    // 전체조회 (페이징)
    @GetMapping
    public ResponseEntity<PageResponseDTO<ImgBoardDTO>> imgFindAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNo) {
        return ResponseEntity.ok(imgBoardService.imgFindAll(pageNo));
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<ImgBoardDTO>> searchImgBoards(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int pageNo) {

        log.info("검색 요청 - type: {}, keyword: {}, page: {}", type, keyword, pageNo);
        return ResponseEntity.ok(imgBoardService.searchImgBoards(type, keyword, pageNo));
    }

    // 단일조회 + 조회수 증가
    @GetMapping("/{imgBoardNo}")
    public ResponseEntity<ImgBoardDTO> findByImgBoardNo(
            @PathVariable(name = "imgBoardNo")
            @Min(value = 1, message = "넘작아용") Long imgBoardNo) {

        imgBoardService.increaseImgView(imgBoardNo);
        ImgBoardDTO imgBoard = imgBoardService.findByImgBoardNo(imgBoardNo);
        return ResponseEntity.ok(imgBoard);
    }

    // 수정
    @PutMapping("/{imgBoardNo}")
    public ResponseEntity<ImgBoardDTO> imgUpdate(
            @PathVariable(name = "imgBoardNo") Long imgBoardNo,
            ImgBoardDTO imgBoard,
            @RequestParam(name = "files", required = false) MultipartFile[] files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        imgBoard.setImgBoardNo(imgBoardNo);
        ImgBoardDTO updated = imgBoardService.imgUpdate(imgBoard, files, imgBoardNo, userDetails);

        return ResponseEntity.ok(updated);
    }

    // 삭제
    @DeleteMapping("/{imgBoardNo}")
    public ResponseEntity<ApiResponse<Void>> deleteByImgBoardNo(
            @PathVariable(name = "imgBoardNo") Long imgBoardNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        imgBoardService.deleteByImgBoardNo(imgBoardNo, userDetails);

        return ResponseEntity
                .ok(ApiResponse.success("게시글이 삭제되었습니다."));
    }

    // 이미지 게시글 신고
    @PostMapping("/{imgBoardNo}/report")
    public ResponseEntity<ApiResponse<Void>> reportBoard(
            @PathVariable(name = "imgBoardNo") Long imgBoardNo,
            @RequestBody ReportDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("로그인이 필요합니다."));
        }

        ImgBoardDTO imgBoard = imgBoardService.findByImgBoardNo(imgBoardNo);
        log.info("정보조회 : {} ", imgBoard);

        Long reportedUser = imgBoard.getImgWriterNo(); // 신고 당하는 사람
        Long reporter = userDetails.getUserNo();       // 신고자

        ReportDTO dto = ReportDTO.builder()
                .targetType("IMGBOARD")
                .targetNo(imgBoardNo)
                .reportedUser(reportedUser)
                .reason(request.getReason())
                .build();

        reportService.report(reporter, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("신고가 접수되었습니다."));
    }
}
