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

    private final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "releaseDate");

    public Page<Product> search(int page, int size, String query,
                                float minPrice, float maxPrice,
                                List<String> productTypes, List<String> groups, List<String> factions) {
        return productRepository.search(PageRequest.of(page, size, DEFAULT_SORT),
            query, minPrice, maxPrice, productTypes, groups != null, groups, factions != null, factions);
    }

    public Page<Product> getAll(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Product get(Long id) throws NoSuchElementException {
        return productRepository.findByIdWithLock(id).orElseThrow(NoSuchElementException::new);
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
