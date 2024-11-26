package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.repository.ProductImageRepository;
import com.warhammer.ecom.service.imageshandler.ImagesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImagesHandler imagesHandler;

    public ProductImage get(Long productImageId) throws NoSuchElementException {
        return productImageRepository.findById(productImageId).orElseThrow(NoSuchElementException::new);
    }

    public ProductImage create(MultipartFile imgFile, Long productId, String description, boolean isCatalogueImg) throws NoSuchElementException {
        ProductImage productImage = new ProductImage();
        Product product = productService.get(productId);
        productImage.setDescription(description);
        productImage.setProduct(product);
        final String URL = imagesHandler.uploadImage(imgFile);

        productImage.setUrl(URL);

        if (isCatalogueImg) {
            product.setCatalogueImg(productImage);
        }

        productImage = productImageRepository.save(productImage);
        productService.update(product);
        return productImage;
    }

    public void delete(ProductImage productImage) {
        if (productImage != null) {
            final String URL = productImage.getUrl();
            productImageRepository.delete(productImage);
            if (URL != null) {
                imagesHandler.deleteImage(URL);
            }
        }
    }
}
