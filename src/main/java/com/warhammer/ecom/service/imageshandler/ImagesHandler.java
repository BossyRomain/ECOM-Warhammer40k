package com.warhammer.ecom.service.imageshandler;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesHandler {

    String uploadImage(MultipartFile imgFile);

    void deleteImage(String URL);
    
}
