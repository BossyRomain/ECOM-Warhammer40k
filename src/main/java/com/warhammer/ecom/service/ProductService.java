package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllWithFilters(int page, int size, float minPrice, float maxPrice, List<String> productTypes, List<String> groups, List<String> factions) {
        return productRepository.findFiltered(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "releaseDate")), minPrice, maxPrice, productTypes, groups, factions);
    }

    public Page<Product> getAll(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Product get(Long id) throws NoSuchElementException {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) throws NoSuchElementException {
        if (productRepository.existsById(product.getId())) {
            return productRepository.save(product);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
