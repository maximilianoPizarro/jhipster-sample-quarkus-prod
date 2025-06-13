package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Location;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Location}.
 */
public interface LocationService {
    /**
     * Save a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    Location persistOrUpdate(Location location);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the locations.
     * @return the list of entities.
     */
    public List<Location> findAll();

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Location> findOne(Long id);

    /**
     * Get all the Location where Department is {@code null}.
     *
     * @return the list of entities.
     */
    List<Location> findAllWhereDepartmentIsNull();
}
