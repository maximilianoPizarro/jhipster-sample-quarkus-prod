package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Region;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Region}.
 */
public interface RegionService {
    /**
     * Save a region.
     *
     * @param region the entity to save.
     * @return the persisted entity.
     */
    Region persistOrUpdate(Region region);

    /**
     * Delete the "id" region.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the regions.
     * @return the list of entities.
     */
    public List<Region> findAll();

    /**
     * Get the "id" region.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Region> findOne(Long id);

    /**
     * Get all the Region where Country is {@code null}.
     *
     * @return the list of entities.
     */
    List<Region> findAllWhereCountryIsNull();
}
