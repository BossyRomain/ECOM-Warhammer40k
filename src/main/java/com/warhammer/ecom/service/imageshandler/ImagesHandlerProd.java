package com.warhammer.ecom.service.imageshandler;

import com.warhammer.ecom.service.S3ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Profile("prod")
@Component
public class ImagesHandlerProd implements ImagesHandler {

    @Autowired
    private S3ImageService s3ImageService;

    @Override
    public String uploadImage(MultipartFile imgFile) {
        return s3ImageService.upload(imgFile);
    }

    @Override
    public void deleteImage(String URL) {
        s3ImageService.delete(URL.substring(URL.lastIndexOf("/") + 1));
    }
}
