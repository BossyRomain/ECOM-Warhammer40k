package com.warhammer.ecom.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Profile("dev")
public class ProductImageServiceDev extends ProductImageService {

    private static final String DIR_PATH = System.getenv("ANGULAR_ASSETS_PATH") + "/images/";

    @Override
    protected String uploadImage(MultipartFile imgFile) {
        try {
            UUID uuid = UUID.randomUUID();
            final String fileName = uuid.toString() + ".png";
            new File(DIR_PATH).mkdirs();

            File file = new File(DIR_PATH + fileName);
            file.createNewFile();

            imgFile.transferTo(file);

            return "assets/images/" + fileName;
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
