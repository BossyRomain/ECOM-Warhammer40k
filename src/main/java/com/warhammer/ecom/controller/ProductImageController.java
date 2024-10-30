package com.warhammer.ecom.controller;

import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products/{productId}/images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping("")
    public ResponseEntity<Long> addImage(
        @PathVariable("productId") Long productId,
        @RequestParam("description") String description,
        @RequestParam(name = "isCatalogueImage", defaultValue = "false") boolean isCatalogueImage,
        @RequestParam("file") MultipartFile file
    ) {
        ProductImage pi = productImageService.create(file, productId, description, isCatalogueImage);
        try {
            URI uri = new URI("/api/products/" + productId + "/images");
            return ResponseEntity.created(uri).body(pi.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productImageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("productImageId") Long productImageId) {
        try {
            productImageService.delete(productImageId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
