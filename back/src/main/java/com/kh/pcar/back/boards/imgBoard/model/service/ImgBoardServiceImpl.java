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
import com.kh.pcar.back.file.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgBoardServiceImpl implements ImgBoardService {

    private final ImgBoardMapper imgBoardMapper;
    private final AttachmentMapper attachmentMapper;
    private final S3Service s3Service; 
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

        // 2) 게시글 INSERT
        imgBoardMapper.imgSave(ib);
        Long imgBoardNo = ib.getImgBoardNo(); 

        // 3) 파일 처리
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;

                // [수정] S3Service의 메서드명은 fileSave입니다.
                String storedPath = s3Service.fileSave(file);
                System.out.println("🔥 S3 upload start: " + file.getOriginalFilename());

                AttachmentVO avo = AttachmentVO.builder()
                        .refIno(imgBoardNo)
                        .originName(file.getOriginalFilename())
                        .changeName(extractFileName(storedPath))
                        .filePath(storedPath)
                        .status("Y")
                        .build();

                attachmentMapper.insertAttachment(avo);
            }
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

        return new PageResponseDTO<>(list, total, pageNo, size);
    }
    
    @Override
    public PageResponseDTO<ImgBoardDTO> searchImgBoards(String type, String keyword, int pageNo) {
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
    @Transactional 
    public ImgBoardDTO imgUpdate(ImgBoardDTO imgBoard, MultipartFile[] files,
                                 Long imgBoardNo, CustomUserDetails userDetails) {

        ImgBoardDTO imgOrigin = validateImgBoard(imgBoardNo, userDetails);

        imgBoard.setImgBoardNo(imgBoardNo);
        imgBoard.setImgBoardWriter(imgOrigin.getImgBoardWriter());
        imgBoardMapper.imgUpdate(imgBoard, userDetails.getUserNo());

        boolean hasNewFiles = files != null
                && java.util.Arrays.stream(files).anyMatch(f -> f != null && !f.isEmpty());

        if (hasNewFiles) {
            attachmentMapper.disableByRefIno(imgBoardNo);

            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;

                // [수정] s3Service.fileSave(file)로 명칭 변경
                String storedPath = s3Service.fileSave(file);

                AttachmentVO avo = AttachmentVO.builder()
                        .refIno(imgBoardNo)
                        .originName(file.getOriginalFilename())
                        .changeName(extractFileName(storedPath))
                        .filePath(storedPath)
                        .status("Y")
                        .build();

                attachmentMapper.insertAttachment(avo);
            }
        }
        
        return findByImgBoardNo(imgBoardNo);
    }

    @Override
    @Transactional
    public void deleteByImgBoardNo(Long imgBoardNo, CustomUserDetails userDetails) {
        validateImgBoard(imgBoardNo, userDetails);
        imgBoardMapper.deleteByImgBoardNo(imgBoardNo, userDetails.getUserNo());
        attachmentMapper.disableByRefIno(imgBoardNo);
    }
}