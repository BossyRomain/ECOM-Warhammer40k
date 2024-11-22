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

    public Page<Product> search(int page, int size, String query) {
        return productRepository.searchByName(PageRequest.of(page, size, DEFAULT_SORT), query);
    }

    public Page<Product> getAllWithFilters(int page, int size, float minPrice, float maxPrice,
                                           List<String> productTypes, List<String> groups, List<String> factions) {
        boolean filterGroups = groups != null;
        boolean filterFactions = factions != null;
        return productRepository.findFiltered(PageRequest.of(page, size, DEFAULT_SORT),
            minPrice, maxPrice, productTypes, filterGroups, groups, filterFactions, factions);
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
