package com.woowa.woowakit.infra.aws.s3;

import com.woowa.woowakit.domain.product.application.ImageUploader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3ImageUploader implements ImageUploader {

    private final S3Uploader s3Uploader;

    public String upload(MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "images");
    }
}
