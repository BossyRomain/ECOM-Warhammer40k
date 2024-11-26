package com.warhammer.ecom.service.imageshandler;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImagesHandlerDefault implements ImagesHandler {
    @Override
    public String uploadImage(MultipartFile imgFile) {
        return "assets/dev/images/" + imgFile.getName();
    }

    @Override
    public void deleteImage(String URL) {
    }
}
