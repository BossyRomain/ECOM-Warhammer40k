package com.warhammer.ecom.controller.dto;

import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductImage;
import com.warhammer.ecom.model.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCatalogueDTO {

    private Long id;

    private String name;

    private Integer stock;

    private Float unitPrice;

    private ProductType productType;

    private ProductImage catalogueImg;

    public static ProductCatalogueDTO fromProduct(Product product) {
        if (product == null) {
            return null;
        }

        ProductCatalogueDTO dto = new ProductCatalogueDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setStock(product.getStock());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setProductType(product.getProductType());
        dto.setCatalogueImg(product.getCatalogueImg());

        return dto;
    }
}
