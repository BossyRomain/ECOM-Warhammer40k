package com.warhammer.ecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "buying_date")
    private Instant buyingDate;

    @Column(name = "paid")
    private Boolean paid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "cart" }, allowSetters = true)
    private Set<CommandLine> commandLines = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "carts" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBuyingDate() {
        return this.buyingDate;
    }

    public Cart buyingDate(Instant buyingDate) {
        this.setBuyingDate(buyingDate);
        return this;
    }

    public void setBuyingDate(Instant buyingDate) {
        this.buyingDate = buyingDate;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Cart paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Set<CommandLine> getCommandLines() {
        return this.commandLines;
    }

    public void setCommandLines(Set<CommandLine> commandLines) {
        if (this.commandLines != null) {
            this.commandLines.forEach(i -> i.setCart(null));
        }
        if (commandLines != null) {
            commandLines.forEach(i -> i.setCart(this));
        }
        this.commandLines = commandLines;
    }

    public Cart commandLines(Set<CommandLine> commandLines) {
        this.setCommandLines(commandLines);
        return this;
    }

    public Cart addCommandLines(CommandLine commandLine) {
        this.commandLines.add(commandLine);
        commandLine.setCart(this);
        return this;
    }

    public Cart removeCommandLines(CommandLine commandLine) {
        this.commandLines.remove(commandLine);
        commandLine.setCart(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Cart client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return getId() != null && getId().equals(((Cart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", buyingDate='" + getBuyingDate() + "'" +
            ", paid='" + getPaid() + "'" +
            "}";
    }
}
