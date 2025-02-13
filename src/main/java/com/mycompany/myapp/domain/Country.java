package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@RegisterForReflection
public class Country extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "country_name")
    public String countryName;

    @OneToOne
    @JoinColumn(unique = true)
    public Region region;

    @OneToOne(mappedBy = "country")
    public Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Country{" + "id=" + id + ", countryName='" + countryName + "'" + "}";
    }

    public Country update() {
        return update(this);
    }

    public Country persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Country update(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("country can't be null");
        }
        var entity = Country.<Country>findById(country.id);
        if (entity != null) {
            entity.countryName = country.countryName;
            entity.region = country.region;
            entity.location = country.location;
        }
        return entity;
    }

    public static Country persistOrUpdate(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("country can't be null");
        }
        if (country.id == null) {
            persist(country);
            return country;
        } else {
            return update(country);
        }
    }
}
