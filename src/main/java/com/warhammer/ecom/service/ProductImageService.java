package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

public abstract class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductService productService;

    public ProductImage create(MultipartFile imgFile, Long productId, String description, boolean isCatalogueImg) throws NoSuchElementException {
        ProductImage productImage = new ProductImage();
        Product product = productService.getProduct(productId);
        if (product == null) {
            throw new NoSuchElementException("No product with the id " + productId);
        }
        productImage.setDescription(description);
        productImage.setProduct(product);
        final String URL = uploadImage(imgFile);

        productImage.setUrl(URL);

        if (isCatalogueImg) {
            product.setCatalogueImg(productImage);
        }

        return productImageRepository.save(productImage);
    }

    public void delete(Long productImageId) throws NoSuchElementException {
        ProductImage productImage = productImageRepository.findById(productImageId)
            .orElseThrow(() -> new NoSuchElementException("No product image with the id " + productImageId));

        final String URL = productImage.getUrl();
        productImageRepository.delete(productImage);
        deleteImage(URL);
    }

    protected abstract String uploadImage(MultipartFile imgFile);

    protected abstract void deleteImage(String URL);
}
