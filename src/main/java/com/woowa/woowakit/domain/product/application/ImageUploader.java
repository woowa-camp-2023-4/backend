package com.woowa.woowakit.domain.product.application;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile multipartFile) throws IOException;

}
