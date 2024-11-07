package com.warhammer.ecom.controller.dto;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;

public class ProductCatalogueDTO {

    private Long id;

    private String name;

    private Integer stock;

    private Float unitPrice;

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

    public ProductImage getCatalogueImg() {
        return catalogueImg;
    }

    public void setCatalogueImg(ProductImage catalogueImg) {
        this.catalogueImg = catalogueImg;
    }

    public static ProductCatalogueDTO fromProduct(Product product) {
        if(product == null) {
            return null;
        }

        ProductCatalogueDTO dto = new ProductCatalogueDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setStock(product.getStock());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setCatalogueImg(product.getCatalogueImg());

        return dto;
    }
}
