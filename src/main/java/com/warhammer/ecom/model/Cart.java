package com.warhammer.ecom.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@SequenceGenerator(name="cartIdSeq", initialValue=1, allocationSize=100)
public class Cart {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="cartIdSeq")
    private Long id;

    @Column(nullable = false)
    private Timestamp purchaseDate;

    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_FK")
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
