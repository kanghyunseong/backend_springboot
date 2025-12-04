package com.kh.pcar.back.boards.imgBoard.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.auth.model.vo.CustomUserDetails;
import com.kh.pcar.back.boards.PageResponseDTO;
import com.kh.pcar.back.boards.imgBoard.model.dao.AttachmentMapper;
import com.kh.pcar.back.boards.imgBoard.model.dao.ImgBoardMapper;
import com.kh.pcar.back.boards.imgBoard.model.dto.AttachmentDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.vo.AttachmentVO;
import com.kh.pcar.back.boards.imgBoard.model.vo.ImgBoardVO;
import com.kh.pcar.back.exception.CustomAuthorizationException;
import com.kh.pcar.back.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgBoardServiceImpl implements ImgBoardService {

	private final ImgBoardMapper imgBoardMapper;
	private final AttachmentMapper attachmentMapper;
	private final FileService fileService;
	private final int pageSize = 10;
	
    @Override
    @Transactional
    public void imgSave(ImgBoardDTO imgBoard, MultipartFile[] files, String userId) {

        // 1) 게시글 VO 생성
        ImgBoardVO ib = ImgBoardVO.builder()
                .imgBoardTitle(imgBoard.getImgBoardTitle())
                .imgBoardContent(imgBoard.getImgBoardContent())
                .imgBoardWriter(userId)
                .build();

        // 2) 게시글 INSERT -> selectKey로 imgBoardNo 채워짐
        imgBoardMapper.imgSave(ib);

        Long imgBoardNo = ib.getImgBoardNo();  // 이제 이 값으로 첨부파일 REF_INO 설정
        log.info("새로 저장된 IMG_BOARD_NO = {}", imgBoardNo);

        // 3) 파일 없으면 그냥 끝
        if (files == null || files.length == 0) {
            return;
        }

        // 4) 파일 여러 개 저장
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            // 물리 파일 저장 (FileService는 기존에 쓰던 거 그대로 사용)
            String storedPath = fileService.store(file);

            String originName = file.getOriginalFilename();
            String changeName = extractFileName(storedPath); // util 메서드 만들어서 사용

            AttachmentVO avo = AttachmentVO.builder()
                    .refIno(imgBoardNo)
                    .originName(originName)
                    .changeName(changeName)
                    .filePath(storedPath)
                    .status("Y")
                    .build();

            attachmentMapper.insertAttachment(avo);
        }
    }

    private String extractFileName(String path) {
        if (path == null) return null;
        int idx = path.lastIndexOf('/');
        if (idx == -1) return path;
        return path.substring(idx + 1);
    }		

    @Override
    public PageResponseDTO<ImgBoardDTO> imgFindAll(int pageNo) {
        int size = pageSize;
        int offset = pageNo * size;

        RowBounds rb = new RowBounds(offset, size);

        List<ImgBoardDTO> list = imgBoardMapper.imgFindAll(rb);
        long total = imgBoardMapper.countImgBoards();

        // totalPages 계산은 PageResponseDTO 안에서 처리
        return new PageResponseDTO<>(list, total, pageNo, size);
    }
	
    @Override
    public PageResponseDTO<ImgBoardDTO> searchImgBoards(String type, String keyword, int pageNo) {

        log.info("검색 service - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

        int offset = pageNo * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("keyword", "%" + keyword + "%");
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        List<ImgBoardDTO> list = imgBoardMapper.searchImgBoards(params);
        int totalCount = imgBoardMapper.countSearchImgBoards(params);

        return new PageResponseDTO<>(list, totalCount, pageNo, pageSize);
    }

	

	@Override
	public ImgBoardDTO findByImgBoardNo(Long imgBoardNo) {
	    ImgBoardDTO idto = getImgBoardOrThrow(imgBoardNo);
	    List<AttachmentVO> adto = attachmentMapper.findByRefIno(imgBoardNo);

	    // VO -> DTO 변환
	    List<AttachmentDTO> attachments = adto.stream()
								             .map(a -> new AttachmentDTO(
						            		 a.getFileNo(),
						            		 a.getRefIno(),
						            		 a.getOriginName(),
						            		 a.getChangeName(),
						            		 a.getFilePath()
								            ))
								            .toList();

	    idto.setAttachments(attachments);
	    return idto;
	}
	
	@Override
	public void increaseImgView(Long imgBoardNo) {
		imgBoardMapper.increaseImgView(imgBoardNo);
	}
	
	private ImgBoardDTO getImgBoardOrThrow(Long imgBoardNo) {
		ImgBoardDTO imgBoard = imgBoardMapper.findByImgBoardNo(imgBoardNo);
		if(imgBoard == null) {
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		return imgBoard;
	}
	
	private ImgBoardDTO validateImgBoard(Long imgBoardNo, CustomUserDetails userDetails) {
	    ImgBoardDTO imgBoard = getImgBoardOrThrow(imgBoardNo);
	    if (!imgBoard.getImgBoardWriter().equals(userDetails.getUsername())) {
	        throw new CustomAuthorizationException("작성자만 수정/삭제할 수 있습니다.");
	    }
	    return imgBoard;
	}
	
	@Override
	public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile[] files,
	                             Long imgBoardNo, CustomUserDetails userDetails) {

	    // 1. 게시글 존재 + 작성자 검증
	    ImgBoardDTO imgOrigin = validateImgBoard(imgBoardNo, userDetails);

	    // 2. 내용 수정
	    imgBoard.setImgBoardNo(imgBoardNo);
	    imgBoard.setImgBoardWriter(imgOrigin.getImgBoardWriter());

	    Long loginUserNo = userDetails.getUserNo();
	    imgBoardMapper.imgUpdate(imgBoard, loginUserNo);

	    // ====== 파일 처리 시작 ======

	    // 새 파일이 있는지 여부 체크
	    boolean hasNewFiles = files != null
	            && java.util.Arrays.stream(files)
	                               .anyMatch(f -> f != null && !f.isEmpty());

	    if (hasNewFiles) {
	        // 1) 기존 첨부파일 모두 비활성화
	        attachmentMapper.disableByRefIno(imgBoardNo);

	        // 2) 새 첨부파일 저장
	        for (MultipartFile file : files) {
	            if (file == null || file.isEmpty()) continue;

	            String storedPath = fileService.store(file);

	            String originName = file.getOriginalFilename();
	            String changeName = extractFileName(storedPath);

	            AttachmentVO avo = AttachmentVO.builder()
	                    .refIno(imgBoardNo)
	                    .originName(originName)
	                    .changeName(changeName)
	                    .filePath(storedPath)
	                    .status("Y")
	                    .build();

	            attachmentMapper.insertAttachment(avo);
	        }
	    }
	    // 새 파일이 하나도 없으면 → 기존 이미지 그대로 유지 (아무것도 안 함)

	    // 3. 최신 데이터 다시 조회해서 반환
	    return findByImgBoardNo(imgBoardNo);
	}


	@Override
	@Transactional
	public void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails) {
		
		// 1. 게시글 존재 + 작성자 검증 (공통 함수)
	    validateImgBoard(imgBoardNo, userDetails);

	    // 2. 실제 삭제는 userNo 기준으로
	    Long loginUserNo = userDetails.getUserNo();

	    int result = imgBoardMapper.deleteByImgBoardNo(imgBoardNo, loginUserNo);
	    if (result == 0) {
	        throw new InvalidParameterException("삭제할 게시글이 존재하지 않습니다.");
	    }

	    // 3. 첨부파일도 같이 논리 삭제 (이미 이렇게 쓰고 있으면 유지)
	    attachmentMapper.disableByRefIno(imgBoardNo);
	}

}
