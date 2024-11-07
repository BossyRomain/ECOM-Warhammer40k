package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import jakarta.persistence.*;

@Entity
@SequenceGenerator(name="commandLineIdSeq", initialValue=1, allocationSize=100)
public class CommandLine {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="commandLineIdSeq")
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMMAND_FK")
    @JsonIgnore
    private Cart command;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_FK")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCommand() {
        return command;
    }

    public void setCommand(Cart command) {
        this.command = command;
    }

    public Product getProduct() {
        return product;
    }

    @JsonGetter("product")
    public ProductCatalogueDTO getProductJSON() {
        return ProductCatalogueDTO.fromProduct(product);
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
