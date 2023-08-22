package com.woowa.woowakit.domain.product.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile multipartFile);

}
