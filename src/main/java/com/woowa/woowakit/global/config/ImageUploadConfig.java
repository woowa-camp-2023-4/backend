package com.woowa.woowakit.global.config;

import com.woowa.woowakit.domain.product.application.ImageUploader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class ImageUploadConfig {

	@Bean
	@Profile("local")
	public ImageUploader imageUploader() {
		return new LocalImageUploader();
	}

	static class LocalImageUploader implements ImageUploader {

		@Override
		public String upload(MultipartFile multipartFile) {
			return "localUrl";
		}
	}
}
