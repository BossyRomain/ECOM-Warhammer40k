package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductType;
import com.warhammer.ecom.repository.AllegianceRepository;
import com.warhammer.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AllegianceRepository allegianceRepository;

    private final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "releaseDate");

    @Transactional(readOnly = true)
    public Page<Product> search(int page, int size, String query,
                                float minPrice, float maxPrice,
                                List<String> types, List<String> groups, List<String> factions) {

        List<Allegiance> allegiances;
        boolean filterGroups = groups != null;
        boolean filterFactions = factions != null;
        if (filterGroups || filterFactions) {
            allegiances = allegianceRepository.findAll();
            Iterator<Allegiance> it = allegiances.iterator();
            while (it.hasNext()) {
                Allegiance allegiance = it.next();
                if (filterGroups && !groups.contains(allegiance.getGroup().toString())) {
                    it.remove();
                } else if (filterFactions && !factions.contains(allegiance.getFaction().toString())) {
                    it.remove();
                }
            }
        } else {
            allegiances = new ArrayList<>();
        }

        List<ProductType> productTypes;
        if (types == null) {
            productTypes = Arrays.stream(ProductType.values()).toList();
        } else {
            productTypes = types.stream().map(pt -> ProductType.valueOf(pt)).toList();
        }

        boolean filterAllegiances = filterGroups || filterFactions;
        return productRepository.search(PageRequest.of(page, size, DEFAULT_SORT),
            query != null ? query : "",
            minPrice,
            maxPrice,
            productTypes,
            filterAllegiances,
            allegiances);
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

    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
