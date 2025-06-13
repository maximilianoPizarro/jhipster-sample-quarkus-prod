package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.service.LocationService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Location}.
 */
@Path("/api/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    LocationService locationService;

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param location the location to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new location, or with status {@code 400 (Bad Request)} if the location has already an ID.
     */
    @POST
    public Response createLocation(Location location, @Context UriInfo uriInfo) {
        log.debug("REST request to save Location : {}", location);
        if (location.id != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = locationService.persistOrUpdate(location);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /locations} : Updates an existing location.
     *
     * @param location the location to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated location,
     * or with status {@code 400 (Bad Request)} if the location is not valid,
     * or with status {@code 500 (Internal Server Error)} if the location couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateLocation(Location location, @PathParam("id") Long id) {
        log.debug("REST request to update Location : {}", location);
        if (location.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = locationService.persistOrUpdate(location);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, location.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the location to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteLocation(@PathParam("id") Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GET
    public List<Location> getAllLocations(@QueryParam(value = "filter") String filter) {
        log.debug("REST request to get all Locations");
        return locationService.findAll();
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the location to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the location, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getLocation(@PathParam("id") Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<Location> location = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(location);
    }
}
