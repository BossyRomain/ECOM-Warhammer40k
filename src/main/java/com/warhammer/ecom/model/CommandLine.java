package com.warhammer.ecom.model;

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
    private Cart command;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public void setProduct(Product product) {
        this.product = product;
    }
}
