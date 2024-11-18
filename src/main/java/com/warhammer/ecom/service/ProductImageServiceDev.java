package com.warhammer.ecom.service;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Profile("dev")
@Transactional
public class ProductImageServiceDev extends ProductImageService {

    private static final String DIR_PATH = System.getProperty("angular.assets") + "/dev/images/";

    @Override
    protected String uploadImage(MultipartFile imgFile) {
        try {
            new File(DIR_PATH).mkdirs();

            File file = new File(DIR_PATH + imgFile.getName());
            file.createNewFile();

            imgFile.transferTo(file);

            return "assets/dev/images/" + imgFile.getName();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void deleteImage(String URL) {
        File file = new File(DIR_PATH + URL.substring(URL.lastIndexOf('/') + 1));
        file.delete();
    }
}
