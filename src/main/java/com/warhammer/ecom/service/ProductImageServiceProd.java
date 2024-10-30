package com.warhammer.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("prod")
public class ProductImageServiceProd extends ProductImageService {

    @Autowired
    private S3ImageService s3ImageService;

    @Override
    public String uploadImage(MultipartFile imgFile) {
        return s3ImageService.upload(imgFile);
    }

    @Override
    protected void deleteImage(String URL) {
        s3ImageService.delete(URL.substring(URL.lastIndexOf("/") + 1));
    }
}
