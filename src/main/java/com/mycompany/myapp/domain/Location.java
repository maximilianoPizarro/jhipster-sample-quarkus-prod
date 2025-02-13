package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "location")
@RegisterForReflection
public class Location extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "street_address")
    public String streetAddress;

    @Column(name = "postal_code")
    public String postalCode;

    @Column(name = "city")
    public String city;

    @Column(name = "state_province")
    public String stateProvince;

    @OneToOne
    @JoinColumn(unique = true)
    public Country country;

    @OneToOne(mappedBy = "location")
    public Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Location{" +
            "id=" +
            id +
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", postalCode='" +
            postalCode +
            "'" +
            ", city='" +
            city +
            "'" +
            ", stateProvince='" +
            stateProvince +
            "'" +
            "}"
        );
    }

    public Location update() {
        return update(this);
    }

    public Location persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Location update(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location can't be null");
        }
        var entity = Location.<Location>findById(location.id);
        if (entity != null) {
            entity.streetAddress = location.streetAddress;
            entity.postalCode = location.postalCode;
            entity.city = location.city;
            entity.stateProvince = location.stateProvince;
            entity.country = location.country;
            entity.department = location.department;
        }
        return entity;
    }

    public static Location persistOrUpdate(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location can't be null");
        }
        if (location.id == null) {
            persist(location);
            return location;
        } else {
            return update(location);
        }
    }
}
