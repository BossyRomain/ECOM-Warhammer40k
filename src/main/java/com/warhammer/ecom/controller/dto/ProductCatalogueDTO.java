package com.warhammer.ecom.controller.dto;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.model.ProductType;

import java.sql.Timestamp;

public class ProductCatalogueDTO {

    private Long id;

    private String name;

    private Integer stock;

    private Float unitPrice;

    private String description;

    private ProductType productType;

    private Timestamp releaseDate;

    private ProductImage catalogueImg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ProductImage getCatalogueImg() {
        return catalogueImg;
    }

    public void setCatalogueImg(ProductImage catalogueImg) {
        this.catalogueImg = catalogueImg;
    }

    public static ProductCatalogueDTO from(Product product) {
        ProductCatalogueDTO dto = new ProductCatalogueDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setStock(product.getStock());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setDescription(product.getDescription());
        dto.setProductType(product.getProductType());
        dto.setReleaseDate(product.getReleaseDate());
        dto.setCatalogueImg(product.getCatalogueImg());

        return dto;
    }
}
