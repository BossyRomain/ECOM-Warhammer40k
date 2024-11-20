package com.warhammer.ecom.controller;

import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<ProductCatalogueDTO> getProductsCatalogue(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "minprice", defaultValue = "0.0") float minprice,
        @RequestParam(name = "maxprice", defaultValue = "999999.0") float maxprice,
        @RequestParam(name = "faction", required = false) List<String> factions,
        @RequestParam(name = "type", required = false) List<String> productTypes
    ) {
        if (factions != null) {
            factions = factions.stream().map(String::toUpperCase).collect(Collectors.toList());
        }

        if (productTypes != null) {
            productTypes = productTypes.stream().map(String::toUpperCase).collect(Collectors.toList());
        }

        return productService.getAllWithFilters(page, size, minprice, maxprice, productTypes, factions).map(ProductCatalogueDTO::fromProduct);
    }

    @GetMapping("")
    public Page<Product> getProducts(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.getAll(page, size);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        Product product = productService.get(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            throw new NoSuchElementException();
        }
    }

    @PostMapping("")
    public ResponseEntity<Long> createProduct(@RequestBody Product product) {
        try {
            Product p = productService.create(product);
            URI uri = new URI("/api/products/" + p.getId());
            return ResponseEntity.created(uri).body(p.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productService.get(productId));
        return ResponseEntity.noContent().build();
    }
}
