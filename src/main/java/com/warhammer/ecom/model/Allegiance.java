package com.warhammer.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"group", "faction"}))
@SequenceGenerator(name="allegianceIdSeq", initialValue=1, allocationSize=100)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Allegiance {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="allegianceIdSeq")
    private Long id;

    @Column(name = "GROUP_NAME")
    @Enumerated(EnumType.STRING)
    private Group group;

    @Enumerated(EnumType.STRING)
    private Faction faction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
