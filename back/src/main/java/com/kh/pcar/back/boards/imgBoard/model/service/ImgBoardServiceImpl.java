package com.kh.pcar.back.boards.imgBoard.model.service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
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
	
	private void validateImgBoard(Long imgBoardNo, CustomUserDetails userDetails) {
		ImgBoardDTO imgBoard = getImgBoardOrThrow(imgBoardNo);
		if(!imgBoard.getImgBoardWriter().equals(userDetails.getUsername())) {
			throw new CustomAuthenticationException("게시글이 존재하지 않습니다.");
		}
	}	
	
	@Override
	public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile file
						  ,Long imgBoardNo, CustomUserDetails userDetails) {
		
		// 1. 원본 게시글 조회
		ImgBoardDTO imgOrigin = imgBoardMapper.findByImgBoardNo(imgBoardNo);
	    if (imgOrigin == null) {
	        throw new RuntimeException("게시글이 존재하지 않습니다.");
	    }

	    // 2. 작성자 체크
	    if (!imgOrigin.getImgBoardWriter().equals(userDetails.getUsername())) {
	        throw new RuntimeException("작성자만 수정 가능합니다.");
	    }

	    // 3. 수정 적용
	    imgBoard.setImgBoardNo(imgBoardNo);
	    imgBoard.setImgBoardWriter(imgOrigin.getImgBoardWriter());

	    imgBoardMapper.imgUpdate(imgBoard);

	    // 4. 최신 데이터 다시 조회해서 반환
	    return imgBoardMapper.findByImgBoardNo(imgBoardNo);
	}

	@Override
	public void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails) {
		
		validateImgBoard(imgBoardNo, userDetails);
		imgBoardMapper.deleteByImgBoardNo(imgBoardNo);

	}
}
