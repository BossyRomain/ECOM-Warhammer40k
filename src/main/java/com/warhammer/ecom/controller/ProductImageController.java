package com.warhammer.ecom.controller;

import com.warhammer.ecom.controller.dto.AddProductImageDTO;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/products/{productId}/images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping("")
    public ResponseEntity<Long> addImage(
        @PathVariable("productId") Long productId,
        @RequestBody AddProductImageDTO productImageDTO,
        @RequestPart MultipartFile file
        ) {
        ProductImage pi = productImageService.create(file, productId, productImageDTO.getDescription(), productImageDTO.isCatalogueImage());
        try {
            URI uri = new URI("/api/products/" + productId + "/images");
            return ResponseEntity.created(uri).body(pi.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productImageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("productImageId") Long productImageId) {
        productImageService.delete(productImageService.get(productImageId));
        return ResponseEntity.noContent().build();
    }
}
