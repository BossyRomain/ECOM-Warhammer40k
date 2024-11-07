package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@SequenceGenerator(name="cartIdSeq", initialValue=1, allocationSize=100)
public class Cart {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="cartIdSeq")
    private Long id;

    @Column(nullable = true)
    @JsonIgnore
    private Timestamp purchaseDate;

    @JsonIgnore
    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "command")
    private Collection<CommandLine> commandLines;

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

    public Collection<CommandLine> getCommandLines() {
        return commandLines;
    }

    public void setCommandLines(Collection<CommandLine> commandLines) {
        this.commandLines = commandLines;
    }
}
