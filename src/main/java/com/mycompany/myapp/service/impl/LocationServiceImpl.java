package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.service.LocationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Override
    @Transactional
    public Location persistOrUpdate(Location location) {
        log.debug("Request to save Location : {}", location);
        return Location.persistOrUpdate(location);
    }

    /**
     * Delete the Location by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        Location.findByIdOptional(id).ifPresent(location -> {
            location.delete();
        });
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return Location.findByIdOptional(id);
    }

    /**
     * Get all the locations.
     * @return the list of entities.
     */
    @Override
    public List<Location> findAll() {
        log.debug("Request to get all Locations");
        return Location.findAll().list();
    }
}
