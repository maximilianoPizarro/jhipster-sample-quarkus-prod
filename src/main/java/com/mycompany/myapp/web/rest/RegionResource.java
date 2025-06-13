package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.service.RegionService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Region}.
 */
@Path("/api/regions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    private static final String ENTITY_NAME = "region";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    RegionService regionService;

    /**
     * {@code POST  /regions} : Create a new region.
     *
     * @param region the region to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new region, or with status {@code 400 (Bad Request)} if the region has already an ID.
     */
    @POST
    public Response createRegion(Region region, @Context UriInfo uriInfo) {
        log.debug("REST request to save Region : {}", region);
        if (region.id != null) {
            throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = regionService.persistOrUpdate(region);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /regions} : Updates an existing region.
     *
     * @param region the region to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated region,
     * or with status {@code 400 (Bad Request)} if the region is not valid,
     * or with status {@code 500 (Internal Server Error)} if the region couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateRegion(Region region, @PathParam("id") Long id) {
        log.debug("REST request to update Region : {}", region);
        if (region.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = regionService.persistOrUpdate(region);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, region.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /regions/:id} : delete the "id" region.
     *
     * @param id the id of the region to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteRegion(@PathParam("id") Long id) {
        log.debug("REST request to delete Region : {}", id);
        regionService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /regions} : get all the regions.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of regions in body.
     */
    @GET
    public List<Region> getAllRegions(@QueryParam(value = "filter") String filter) {
        log.debug("REST request to get all Regions");
        return regionService.findAll();
    }

    /**
     * {@code GET  /regions/:id} : get the "id" region.
     *
     * @param id the id of the region to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the region, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getRegion(@PathParam("id") Long id) {
        log.debug("REST request to get Region : {}", id);
        Optional<Region> region = regionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(region);
    }
}
