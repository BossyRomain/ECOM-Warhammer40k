package com.warhammer.ecom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "productIdSeq", initialValue = 1, allocationSize = 100)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productIdSeq")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer stock;

    private Float unitPrice;

    @Column(length = 8000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private Timestamp releaseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "allegiance_id", nullable = true)
    private Allegiance allegiance;

    @OneToOne
    private ProductImage catalogueImg;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<ProductImage> images;
    
    public Allegiance getAllegiance() {
        return allegiance.getFaction() == Faction.NONE || allegiance.getGroup() == Group.NONE ? null : allegiance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product product = (Product) obj;
            return id.equals(product.getId());
        }
        return false;
    }
}
