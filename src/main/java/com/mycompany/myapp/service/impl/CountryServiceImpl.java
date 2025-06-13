package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.service.CountryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Override
    @Transactional
    public Country persistOrUpdate(Country country) {
        log.debug("Request to save Country : {}", country);
        return Country.persistOrUpdate(country);
    }

    /**
     * Delete the Country by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        Country.findByIdOptional(id).ifPresent(country -> {
            country.delete();
        });
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Country> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return Country.findByIdOptional(id);
    }

    /**
     * Get all the countries.
     * @return the list of entities.
     */
    @Override
    public List<Country> findAll() {
        log.debug("Request to get all Countries");
        return Country.findAll().list();
    }
}
