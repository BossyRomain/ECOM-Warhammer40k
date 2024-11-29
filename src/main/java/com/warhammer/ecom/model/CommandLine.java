package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "commandLineIdSeq", initialValue = 1, allocationSize = 100)
public class CommandLine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commandLineIdSeq")
    private Long id;

    private int quantity;

    @ManyToOne()
    @JoinColumn(name = "command_fk")
    @JsonIgnore
    private Cart command;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_FK")
    private Product product;

    @JsonGetter("product")
    public ProductCatalogueDTO getProductJSON() {
        return ProductCatalogueDTO.fromProduct(product);
    }

}
