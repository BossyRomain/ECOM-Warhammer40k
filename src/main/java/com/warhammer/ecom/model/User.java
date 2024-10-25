package com.warhammer.ecom.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ECOM_USER")
@SequenceGenerator(name="userIdSeq", initialValue=1, allocationSize=100)
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="userIdSeq")
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(unique=true, nullable=false)
    private String password;

    @Column(nullable = false)
    private Authority authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
