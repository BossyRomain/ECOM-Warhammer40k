package com.warhammer.ecom.business;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public int countProducts() {
        return productRepository.findAll().size();
    }

    public void create(Product product) {
        productRepository.save(product);
    }
}
