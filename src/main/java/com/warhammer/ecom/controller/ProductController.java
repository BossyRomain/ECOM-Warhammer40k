package com.warhammer.ecom.controller;

import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/catalogue")
    public List<ProductCatalogueDTO> getProductsCatalogue(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.getProducts(page, size).stream().map(p -> ProductCatalogueDTO.fromProduct(p)).collect(Collectors.toList());
    }

    @GetMapping("")
    public List<Product> getProducts(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.getProducts(page, size);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        Product product = productService.getProduct(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Long> createProduct(@RequestBody Product product) {
        try {
            Product p = productService.createProduct(product);
            URI uri = new URI("/api/products/" + p.getId());
            return ResponseEntity.created(uri).body(p.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        try {
            productService.deleteProduct(productId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
