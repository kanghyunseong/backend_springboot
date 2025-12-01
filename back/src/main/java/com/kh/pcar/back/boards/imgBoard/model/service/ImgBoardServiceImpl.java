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
import com.kh.pcar.back.boards.imgBoard.model.dao.AttachmentMapper;
import com.kh.pcar.back.boards.imgBoard.model.dao.ImgBoardMapper;
import com.kh.pcar.back.boards.imgBoard.model.dto.AttachmentDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgBoardDTO;
import com.kh.pcar.back.boards.imgBoard.model.dto.ImgPageResponseDTO;
import com.kh.pcar.back.boards.imgBoard.model.vo.AttachmentVO;
import com.kh.pcar.back.boards.imgBoard.model.vo.ImgBoardVO;
import com.kh.pcar.back.exception.CustomAuthenticationException;
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

        Long imgBoardNo = ib.getImgBoardNo();  // ✅ 이제 이 값으로 첨부파일 REF_INO 설정
        log.info("새로 저장된 IMG_BOARD_NO = {}", imgBoardNo);

        // 3) 파일 없으면 그냥 끝
        if (files == null || files.length == 0) {
            return;
        }

        // 4) 파일 여러 개 저장
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            // 물리 파일 저장 (FileService는 기존에 쓰던 거 그대로 사용)
            // 예: /uploads/2025/11/27/abcd-uuid.jpg 같은 경로 리턴된다고 가정
            String storedPath = fileService.store(file);

            // storedPath를 "경로 + 변경파일명"으로 쓰는지,
            // 경로/파일명 분리해서 저장하는지는 FileService 구현에 맞춰 조절
            // 예시로 그냥 전체를 FILE_PATH에 저장하고 CHANGE_NAME에는 파일명만 분리했다고 치자.
            String originName = file.getOriginalFilename();
            String changeName = extractFileName(storedPath); // util 메서드 만들어서 사용

            AttachmentVO avo = AttachmentVO.builder()
                    .refIno(imgBoardNo)
                    .originName(originName)
                    .changeName(changeName)
                    .filePath(storedPath)  // 혹은 디렉토리 경로만
                    .status("Y")
                    .build();

            attachmentMapper.insertAttachment(avo);
        }
    }

    // 예시용 유틸 (storedPath에서 파일명만 뽑는다고 가정)
    private String extractFileName(String path) {
        if (path == null) return null;
        int idx = path.lastIndexOf('/');
        if (idx == -1) return path;
        return path.substring(idx + 1);
    }		

	@Override
	public ImgPageResponseDTO<ImgBoardDTO> imgFindAll(int pageNo) {
		 int size = 10;
	    int offset = pageNo * size;


	    // RowBounds 정상 계산
	    RowBounds rb = new RowBounds(offset, size);

	    // 현재 페이지 목록
	    List<ImgBoardDTO> list = imgBoardMapper.imgFindAll(rb);

	    // 전체 개수
	    long total = imgBoardMapper.countImgBoards();

	    // 총 페이지 수
	    int totalPages = (int) Math.ceil(total / (double) size);

	    return new ImgPageResponseDTO<>(
	            list,
	            totalPages,
	            total,
	            pageNo,
	            size
	    );
	}
	
	@Override
    public ImgPageResponseDTO<ImgBoardDTO> searchImgBoards(String type, String keyword, int pageNo) {

        log.info("검색 service - type: {}, keyword: {}, page: {}", type, keyword, pageNo);

        int offset = pageNo * pageSize;

        // MyBatis에 넘길 파라미터
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);                         // title / writer / content
        params.put("keyword", "%" + keyword + "%");       // LIKE 검색용
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        // 목록 조회
        List<ImgBoardDTO> list = imgBoardMapper.searchImgBoards(params);

        // 전체 개수 조회
        int totalCount = imgBoardMapper.countSearchImgBoards(params);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return new ImgPageResponseDTO<>(list, pageNo, pageSize, totalPages, totalCount);
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
		if(!imgBoard.getImgBoardWriter().equals(userDetails.getUsername())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
		
		return imgBoard;
	}	
	
	@Override
	public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile[] files
						  ,Long imgBoardNo, CustomUserDetails userDetails) {
		
		// 1. 게시글 존재 + 작성자 검증 (공통 함수 사용)
	    ImgBoardDTO imgOrigin = validateImgBoard(imgBoardNo, userDetails);

	    // 2. 수정 적용
	    imgBoard.setImgBoardNo(imgBoardNo);
	    imgBoard.setImgBoardWriter(imgOrigin.getImgBoardWriter());
	    
	    // 여기서 userDetails 안에 userNo 있다고 가정
	    Long loginUserNo = userDetails.getUserNo();

	    imgBoardMapper.imgUpdate(imgBoard, loginUserNo);
	    
		// 기존 첨부파일은 무조건 전부 비활성화 (이미지 전부 삭제 효과)
	    attachmentMapper.disableByRefIno(imgBoardNo);

	    // 3. 최신 데이터 다시 조회해서 반환
	    if (files != null && files.length > 0) {
	    	// 새 첨부파일들 저장
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
	    // 4. 최신 데이터 다시 조회해서 반환 
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
	        throw new RuntimeException("삭제할 수 없습니다.");
	    }

	    // 3. 첨부파일도 같이 논리 삭제 (이미 이렇게 쓰고 있으면 유지)
	    attachmentMapper.disableByRefIno(imgBoardNo);
	}
}
