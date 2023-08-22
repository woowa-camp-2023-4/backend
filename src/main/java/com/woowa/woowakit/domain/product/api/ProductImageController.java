package com.woowa.woowakit.domain.product.api;

import com.woowa.woowakit.domain.product.application.ImageUploader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/images")
@Slf4j
public class ProductImageController {

    private final ImageUploader imageUploader;

    @GetMapping
    public String index() {
        log.info("호출");
        return "index";
    }

    @PostMapping
    @ResponseBody
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        log.info("이미지 저장");
        return imageUploader.upload(multipartFile);
    }
}
