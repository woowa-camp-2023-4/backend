package com.woowa.woowakit.domains.product.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile multipartFile);
}
