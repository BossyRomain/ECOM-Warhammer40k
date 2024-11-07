package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@SequenceGenerator(name="clientIdSeq", initialValue=1, allocationSize=100)
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="clientIdSeq")
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    @JoinColumn(name = "CURRENT_CART_FK", referencedColumnName = "id")
    private Cart currentCart;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "client")
    @JsonIgnore
    private Collection<Cart> carts;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthday;

    private boolean newsletter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }

    public Collection<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Collection<Cart> carts) {
        this.carts = carts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isNewsletter() {
        return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }
}
