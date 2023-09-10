package com.woowa.woowakit.infra.aws.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.woowa.woowakit.domains.product.application.ImageUploader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3ImageUploader implements ImageUploader {

	private static final String IMAGES_DIR_NAME = "images";
	private final S3Uploader s3Uploader;

	public String upload(final MultipartFile multipartFile) {
		try {
			String fileName = UUID.randomUUID() + "." + parseExtension(multipartFile);
			return s3Uploader.upload(multipartFile, IMAGES_DIR_NAME + "/" + fileName);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private String parseExtension(final MultipartFile multipartFile) {
		return StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
	}
}
