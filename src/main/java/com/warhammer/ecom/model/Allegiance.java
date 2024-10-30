package com.warhammer.ecom.model;

import jakarta.persistence.*;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"group", "faction"}))
@SequenceGenerator(name="allegianceIdSeq", initialValue=1, allocationSize=100)
public class Allegiance {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="allegianceIdSeq")
    private Long id;

    private Group m_group;

    private Faction faction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return m_group;
    }

    public void setGroup(Group group) {
        this.m_group = group;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
