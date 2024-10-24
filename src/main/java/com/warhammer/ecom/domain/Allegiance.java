package com.warhammer.ecom.domain;

import com.warhammer.ecom.domain.enumeration.Faction;
import com.warhammer.ecom.domain.enumeration.Group;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Allegiance.
 */
@Entity
@Table(name = "allegiance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Allegiance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_group")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "faction")
    private Faction faction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Allegiance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return this.group;
    }

    public Allegiance group(Group group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public Allegiance faction(Faction faction) {
        this.setFaction(faction);
        return this;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Allegiance)) {
            return false;
        }
        return getId() != null && getId().equals(((Allegiance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Allegiance{" +
            "id=" + getId() +
            ", group='" + getGroup() + "'" +
            ", faction='" + getFaction() + "'" +
            "}";
    }
}
