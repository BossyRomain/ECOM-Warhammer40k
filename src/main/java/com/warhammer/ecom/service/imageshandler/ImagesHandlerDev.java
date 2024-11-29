package com.warhammer.ecom.service.imageshandler;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Profile("dev")
@Component
public class ImagesHandlerDev implements ImagesHandler {

    private static final String DIR_PATH = System.getProperty("angular.assets") + "/dev/images/";

    @Override
    public String uploadImage(MultipartFile imgFile) {
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
    public void deleteImage(String URL) {
        File file = new File(DIR_PATH + URL.substring(URL.lastIndexOf('/') + 1));
        file.delete();
    }
}
