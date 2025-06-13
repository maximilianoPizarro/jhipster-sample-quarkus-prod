package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
@RegisterForReflection
public class Region extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "region_name")
    public String regionName;

    @OneToOne(mappedBy = "region")
    public Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return id != null && id.equals(((Region) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Region{" + "id=" + id + ", regionName='" + regionName + "'" + "}";
    }

    public Region update() {
        return update(this);
    }

    public Region persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Region update(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("region can't be null");
        }
        var entity = Region.<Region>findById(region.id);
        if (entity != null) {
            entity.regionName = region.regionName;
            entity.country = region.country;
        }
        return entity;
    }

    public static Region persistOrUpdate(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("region can't be null");
        }
        if (region.id == null) {
            persist(region);
            return region;
        } else {
            return update(region);
        }
    }
}
