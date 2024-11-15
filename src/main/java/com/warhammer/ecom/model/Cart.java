package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@SequenceGenerator(name="cartIdSeq", initialValue=1, allocationSize=100)
@Setter
@Getter
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

}
