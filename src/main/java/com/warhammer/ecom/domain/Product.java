package com.warhammer.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warhammer.ecom.domain.enumeration.ProductType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Float price;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Column(name = "release_date")
    private Instant releaseDate;

    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProductImage imageCatalogue;

    @ManyToOne(fetch = FetchType.LAZY)
    private Allegiance allegiance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @JsonIgnoreProperties(value = { "product", "cart" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "product")
    private CommandLine commandLine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Product stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getType() {
        return this.type;
    }

    public Product type(ProductType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Product releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ProductImage getImageCatalogue() {
        return this.imageCatalogue;
    }

    public void setImageCatalogue(ProductImage productImage) {
        this.imageCatalogue = productImage;
    }

    public Product imageCatalogue(ProductImage productImage) {
        this.setImageCatalogue(productImage);
        return this;
    }

    public Allegiance getAllegiance() {
        return this.allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }

    public Product allegiance(Allegiance allegiance) {
        this.setAllegiance(allegiance);
        return this;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setProduct(null));
        }
        if (images != null) {
            images.forEach(i -> i.setProduct(this));
        }
        this.images = images;
    }

    public Product images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Product addImages(Image image) {
        this.images.add(image);
        image.setProduct(this);
        return this;
    }

    public Product removeImages(Image image) {
        this.images.remove(image);
        image.setProduct(null);
        return this;
    }

    public CommandLine getCommandLine() {
        return this.commandLine;
    }

    public void setCommandLine(CommandLine commandLine) {
        if (this.commandLine != null) {
            this.commandLine.setProduct(null);
        }
        if (commandLine != null) {
            commandLine.setProduct(this);
        }
        this.commandLine = commandLine;
    }

    public Product commandLine(CommandLine commandLine) {
        this.setCommandLine(commandLine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", stock=" + getStock() +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            "}";
    }
}
