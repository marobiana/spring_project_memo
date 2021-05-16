package com.marobiana.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 일반적인 스프링 빈
public class FileManagerService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 실제 이미지가 저장될 경로
	// 프로젝트 밑에 images 폴더를 만들어 놓아야 한다!!!!
	public final static String FILE_UPLOAD_PATH = "C:\\Users\\신보람\\Documents\\강의\\강의용프로젝트\\webProjectMEMO\\images/";
	
	public String saveFile(String loginId, MultipartFile file) throws IOException {
		// 파일 디렉토리 경로 예: marobiana_1620995857660/sun.png    
		// 파일명이 겹치지 않게 현재시간을 경로에 붙여준다.
		String directoryName = loginId + "_" + System.currentTimeMillis() + "/";
		String filePath = FILE_UPLOAD_PATH + directoryName;
		
		File directory = new File(filePath);
		// directory.mkdir() -> 파일을 업로드 할 filePath 경로에 폴더(디렉토리) 생성을 한다. 
		if (directory.mkdir() == false) {
			logger.error("[파일업로드] 디렉토리 생성 실패");
			return null; // 디렉토리 생성 실패시 null 리턴
		}
		
		// 파일 업로드: byte 단위로 업로드한다.
		byte[] bytes = file.getBytes();
		Path path = Paths.get(filePath + file.getOriginalFilename()); // originalFilename은 input에 올린 파일명이다.
		Files.write(path, bytes);
		
		// 이미지 URL path를 리턴한다. (WebMvcConfig에서 매핑한 이미지 path)
		//   예) http://localhost/images/marobiana_1620995857660/sun.png
		return "/images/" + directoryName + file.getOriginalFilename();
	}
	
	public void deleteFile(String imagePath) throws IOException {
		// 파일 삭제
		// imagePath의 /images/marobiana_1620999391680/layout.png 에서 /images/ 를 제거한 path를 실제 저장경로 뒤에 붙인다.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		if (Files.exists(path)) {
			Files.delete(path);
		}
		
		// 디렉토리 삭제
		path = path.getParent();
		if (Files.exists(path)) {
			Files.delete(path);
		}
	}
}
