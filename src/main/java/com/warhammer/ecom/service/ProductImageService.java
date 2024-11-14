package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

public abstract class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductService productService;

    public ProductImage get(Long productImageId) throws NoSuchElementException {
        return productImageRepository.findById(productImageId).orElseThrow(NoSuchElementException::new);
    }

    public ProductImage create(MultipartFile imgFile, Long productId, String description, boolean isCatalogueImg) throws NoSuchElementException {
        ProductImage productImage = new ProductImage();
        Product product = productService.get(productId);
        if (product == null) {
            throw new NoSuchElementException("No product with the id " + productId);
        }
        productImage.setDescription(description);
        productImage.setProduct(product);
        final String URL = uploadImage(imgFile);

        productImage.setUrl(URL);

        if (isCatalogueImg) {
            product.setCatalogueImg(productImage);
            productService.create(product);
        }

        return productImageRepository.save(productImage);
    }

    public void delete(ProductImage productImage) {
        if(productImage != null) {
            final String URL = productImage.getUrl();
            productImageRepository.delete(productImage);
            if(URL != null) {
                deleteImage(URL);
            }
        }
    }

    protected abstract String uploadImage(MultipartFile imgFile);

    protected abstract void deleteImage(String URL);

    @Profile("dev")
    public ProductImage create(String url, String description, boolean isCatalogueImg, Product product) {
        ProductImage productImage = new ProductImage();
        productImage.setUrl(url);
        productImage.setDescription(description);
        productImage.setProduct(product);

        productImage = productImageRepository.save(productImage);
        if(isCatalogueImg) {
            product.setCatalogueImg(productImage);
            productService.create(product);
        }

        return productImage;
    }
}
