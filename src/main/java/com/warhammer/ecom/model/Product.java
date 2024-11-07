package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@SequenceGenerator(name="productIdSeq", initialValue=1, allocationSize=100)
public class Product {

    public static class ProductCatalogue {}

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="productIdSeq")
    @JsonView(ProductCatalogue.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(ProductCatalogue.class)
    private String name;

    @JsonView(ProductCatalogue.class)
    private Integer stock;

    @JsonView(ProductCatalogue.class)
    private Float unitPrice;

    @JsonView(ProductCatalogue.class)
    private String description;

    @JsonView(ProductCatalogue.class)
    private ProductType productType;

    @JsonView(ProductCatalogue.class)
    private Timestamp releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALLEGIANCE_FK")
    @JsonView(ProductCatalogue.class)
    private Allegiance allegiance;

    @OneToOne
    @JsonView(ProductCatalogue.class)
    private ProductImage catalogueImg;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<ProductImage> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLOR_FK", nullable = true)
    private Color color;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }

    public ProductImage getCatalogueImg() {
        return catalogueImg;
    }

    public void setCatalogueImg(ProductImage catalogueImg) {
        this.catalogueImg = catalogueImg;
    }

    public Collection<ProductImage> getImages() {
        return images;
    }

    public void setImages(Collection<ProductImage> images) {
        this.images = images;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
