package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.service.CountryService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.util.HeaderUtil;
import com.mycompany.myapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Country}.
 */
@Path("/api/countries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    private static final String ENTITY_NAME = "country";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    CountryService countryService;

    /**
     * {@code POST  /countries} : Create a new country.
     *
     * @param country the country to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new country, or with status {@code 400 (Bad Request)} if the country has already an ID.
     */
    @POST
    public Response createCountry(Country country, @Context UriInfo uriInfo) {
        log.debug("REST request to save Country : {}", country);
        if (country.id != null) {
            throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = countryService.persistOrUpdate(country);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /countries} : Updates an existing country.
     *
     * @param country the country to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated country,
     * or with status {@code 400 (Bad Request)} if the country is not valid,
     * or with status {@code 500 (Internal Server Error)} if the country couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateCountry(Country country, @PathParam("id") Long id) {
        log.debug("REST request to update Country : {}", country);
        if (country.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = countryService.persistOrUpdate(country);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, country.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /countries/:id} : delete the "id" country.
     *
     * @param id the id of the country to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCountry(@PathParam("id") Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /countries} : get all the countries.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of countries in body.
     */
    @GET
    public List<Country> getAllCountries(@QueryParam(value = "filter") String filter) {
        log.debug("REST request to get all Countries");
        return countryService.findAll();
    }

    /**
     * {@code GET  /countries/:id} : get the "id" country.
     *
     * @param id the id of the country to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the country, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getCountry(@PathParam("id") Long id) {
        log.debug("REST request to get Country : {}", id);
        Optional<Country> country = countryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(country);
    }
}
