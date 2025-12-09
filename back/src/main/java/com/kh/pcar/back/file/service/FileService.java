package com.kh.pcar.back.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.pcar.back.exception.FileStorageException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	private final Path fileLocation;

	public FileService() {
		this.fileLocation = Paths.get("uploads").toAbsolutePath().normalize();
	}

	public String store(MultipartFile file) {

		// 이름바꾸기 메소드 호출
		// 파일의 원본 파일명 가져오기
		String originalFilename = file.getOriginalFilename();
		// 파일을 저장할 경로 생성
		// this.fileLocation: 기본 저장 디렉토리 (예: "C:/uploads/")
		// resolve(): 경로 연결 메서드 (예: "C:/uploads/" + "profile.jpg" =
		// "C:/uploads/profile.jpg
		Path targetLocation = this.fileLocation.resolve(originalFilename);

		try {
			// 파일을 실제로 서버에 저장
			Files.copy(file.getInputStream() // 업로드된 파일 데이터(입력 스트림)
					, targetLocation // 저장할 위치
					, StandardCopyOption.REPLACE_EXISTING); // 같은 이름 파일 있으면 덮어쓰기

			// 저장 성공 시 파일에 접근할 수 있는 URL 반환
			// 프론트엔드에서 이 URL로 이미지를 표시할 수 있음
			return "http://localhost:8081/uploads/" + originalFilename;

		} catch (IOException e) {
			// 파일 저장 중 에러 발생 시 예외 던지기
			throw new FileStorageException("이상한 파일입니다.");
		}
	}
}
