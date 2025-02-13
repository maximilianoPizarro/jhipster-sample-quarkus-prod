package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Country;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param country the entity to save.
     * @return the persisted entity.
     */
    Country persistOrUpdate(Country country);

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the countries.
     * @return the list of entities.
     */
    public List<Country> findAll();

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Country> findOne(Long id);

    /**
     * Get all the Country where Location is {@code null}.
     *
     * @return the list of entities.
     */
    List<Country> findAllWhereLocationIsNull();
}
