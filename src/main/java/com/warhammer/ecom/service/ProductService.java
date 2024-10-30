package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) throws NoSuchElementException {
        Product product = getProduct(id);
        if (product == null) {
            throw new NoSuchElementException("There is no product with id " + id);
        } else {
            productRepository.delete(product);
        }
    }
}
