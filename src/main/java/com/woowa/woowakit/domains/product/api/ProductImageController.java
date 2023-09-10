package com.woowa.woowakit.domains.product.api;

import com.woowa.woowakit.domains.auth.annotation.Admin;
import com.woowa.woowakit.domains.product.application.ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/images")
@Slf4j
public class ProductImageController {

    private final ImageUploader imageUploader;

    @Admin
    @PostMapping
    public String upload(@RequestPart("data") final MultipartFile multipartFile) {
        log.info("이미지 저장");
        return imageUploader.upload(multipartFile);
    }
}
