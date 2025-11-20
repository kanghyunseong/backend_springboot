package com.kh.pcar.back.admin.cars.model.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths; // import 추가
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSaveService {

    // [수정] "uploads" 폴더를 프로젝트 실행 위치 기준 절대 경로로 지정
    private final String uploadPath = Paths.get("uploads").toAbsolutePath().toString();

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String storeFilename = timestamp + "_" + originalFilename;

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("업로드 폴더를 생성할 수 없습니다: " + uploadPath);
            }
        }

        String fullPath = uploadPath + File.separator + storeFilename;
        file.transferTo(new File(fullPath));

        return "http://localhost:8081/uploads/" + storeFilename;
    }
}