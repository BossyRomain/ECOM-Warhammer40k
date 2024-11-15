package com.warhammer.ecom.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!(dev|prod)")
public class ProductImageServiceEmpty extends ProductImageService {

    @Override
    protected String uploadImage(MultipartFile imgFile) {
        return "";
    }

    @Override
    protected void deleteImage(String URL) {

    }
}
