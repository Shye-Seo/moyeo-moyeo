package com.service.eventus.aws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {
	
	@Value("eventuslab")
    private String bucket;
	
	@Autowired
	AwsS3Service awsS3Service;
	
	@Autowired
	AmazonS3Client amazonS3Client;

	@GetMapping("/upload_Eventfiles")
	public ResponseEntity<Object> upload_Eventfiles(MultipartFile[] multipartFileList) throws Exception {
		List<String> imagePathList = new ArrayList<>();
		
		for(MultipartFile multipartFile: multipartFileList) {
			String originalName = multipartFile.getOriginalFilename(); // 파일 이름
			long size = multipartFile.getSize(); // 파일 크기
			
			ObjectMetadata objectMetaData = new ObjectMetadata();
			objectMetaData.setContentType(multipartFile.getContentType());
			objectMetaData.setContentLength(size);
			
			// S3에 업로드
			amazonS3Client.putObject(
				new PutObjectRequest(bucket, originalName, multipartFile.getInputStream(), objectMetaData)
					.withCannedAcl(CannedAccessControlList.PublicRead)
			);
			
			String imagePath = amazonS3Client.getUrl(bucket, originalName).toString(); // 접근가능한 URL 가져오기
			imagePathList.add(imagePath);
		}
		
		return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
	}
}

