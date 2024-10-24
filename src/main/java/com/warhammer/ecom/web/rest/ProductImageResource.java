package com.warhammer.ecom.web.rest;

import com.warhammer.ecom.domain.ProductImage;
import com.warhammer.ecom.repository.ProductImageRepository;
import com.warhammer.ecom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.warhammer.ecom.domain.ProductImage}.
 */
@RestController
@RequestMapping("/api/product-images")
@Transactional
public class ProductImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageResource.class);

    private static final String ENTITY_NAME = "productImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductImageRepository productImageRepository;

    public ProductImageResource(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    /**
     * {@code POST  /product-images} : Create a new productImage.
     *
     * @param productImage the productImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productImage, or with status {@code 400 (Bad Request)} if the productImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductImage> createProductImage(@RequestBody ProductImage productImage) throws URISyntaxException {
        LOG.debug("REST request to save ProductImage : {}", productImage);
        if (productImage.getId() != null) {
            throw new BadRequestAlertException("A new productImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productImage = productImageRepository.save(productImage);
        return ResponseEntity.created(new URI("/api/product-images/" + productImage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString()))
            .body(productImage);
    }

    /**
     * {@code PUT  /product-images/:id} : Updates an existing productImage.
     *
     * @param id the id of the productImage to save.
     * @param productImage the productImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImage,
     * or with status {@code 400 (Bad Request)} if the productImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductImage> updateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImage productImage
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductImage : {}, {}", id, productImage);
        if (productImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productImage = productImageRepository.save(productImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString()))
            .body(productImage);
    }

    /**
     * {@code PATCH  /product-images/:id} : Partial updates given fields of an existing productImage, field will ignore if it is null
     *
     * @param id the id of the productImage to save.
     * @param productImage the productImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImage,
     * or with status {@code 400 (Bad Request)} if the productImage is not valid,
     * or with status {@code 404 (Not Found)} if the productImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the productImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductImage> partialUpdateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImage productImage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductImage partially : {}, {}", id, productImage);
        if (productImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductImage> result = productImageRepository
            .findById(productImage.getId())
            .map(existingProductImage -> {
                if (productImage.getUrl() != null) {
                    existingProductImage.setUrl(productImage.getUrl());
                }
                if (productImage.getDescription() != null) {
                    existingProductImage.setDescription(productImage.getDescription());
                }

                return existingProductImage;
            })
            .map(productImageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString())
        );
    }

    /**
     * {@code GET  /product-images} : get all the productImages.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productImages in body.
     */
    @GetMapping("")
    public List<ProductImage> getAllProductImages(@RequestParam(name = "filter", required = false) String filter) {
        if ("product-is-null".equals(filter)) {
            LOG.debug("REST request to get all ProductImages where product is null");
            return StreamSupport.stream(productImageRepository.findAll().spliterator(), false)
                .filter(productImage -> productImage.getProduct() == null)
                .toList();
        }
        LOG.debug("REST request to get all ProductImages");
        return productImageRepository.findAll();
    }

    /**
     * {@code GET  /product-images/:id} : get the "id" productImage.
     *
     * @param id the id of the productImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductImage : {}", id);
        Optional<ProductImage> productImage = productImageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productImage);
    }

    /**
     * {@code DELETE  /product-images/:id} : delete the "id" productImage.
     *
     * @param id the id of the productImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductImage : {}", id);
        productImageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
