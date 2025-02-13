package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.service.RegionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class RegionServiceImpl implements RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Override
    @Transactional
    public Region persistOrUpdate(Region region) {
        log.debug("Request to save Region : {}", region);
        return Region.persistOrUpdate(region);
    }

    /**
     * Delete the Region by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        Region.findByIdOptional(id).ifPresent(region -> {
            region.delete();
        });
    }

    /**
     * Get one region by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Region> findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        return Region.findByIdOptional(id);
    }

    /**
     * Get all the regions.
     * @return the list of entities.
     */
    @Override
    public List<Region> findAll() {
        log.debug("Request to get all Regions");
        return Region.findAll().list();
    }
}
