package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"group", "faction"}))
@SequenceGenerator(name = "allegianceIdSeq", initialValue = 1, allocationSize = 100)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Allegiance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allegianceIdSeq")
    private Long id;

    @Column(name = "GROUP_NAME")
    @Enumerated(EnumType.STRING)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Faction faction;
}
