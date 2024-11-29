package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "cartIdSeq", initialValue = 1, allocationSize = 100)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartIdSeq")
    private Long id;

    @Column(nullable = true)
    @JsonIgnore
    private Timestamp purchaseDate;

    @JsonIgnore
    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Client client;

    @OneToMany(mappedBy = "command", fetch = FetchType.EAGER)
    private List<CommandLine> commandLines;

}
