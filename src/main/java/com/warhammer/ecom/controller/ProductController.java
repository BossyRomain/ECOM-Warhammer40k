package com.warhammer.ecom.controller;

import com.warhammer.ecom.business.ProductService;
import com.warhammer.ecom.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public Integer getProductsCount() {
        Product p = new Product();
        p.setTitle(UUID.randomUUID().toString());

        productService.create(p);

        return productService.countProducts();
    }
}
