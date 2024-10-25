package com.warhammer.ecom.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@SequenceGenerator(name="colorIdSeq", initialValue=1, allocationSize=100)
public class Color {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="colorIdSeq")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Product> products;
}
