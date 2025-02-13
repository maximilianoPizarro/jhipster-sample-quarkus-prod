package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.JobHistory;
import com.mycompany.myapp.service.JobHistoryService;
import com.mycompany.myapp.service.Paged;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.vm.PageRequestVM;
import com.mycompany.myapp.web.rest.vm.SortRequestVM;
import com.mycompany.myapp.web.util.HeaderUtil;
import com.mycompany.myapp.web.util.PaginationUtil;
import com.mycompany.myapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.JobHistory}.
 */
@Path("/api/job-histories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class JobHistoryResource {

    private final Logger log = LoggerFactory.getLogger(JobHistoryResource.class);

    private static final String ENTITY_NAME = "jobHistory";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    JobHistoryService jobHistoryService;

    /**
     * {@code POST  /job-histories} : Create a new jobHistory.
     *
     * @param jobHistory the jobHistory to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new jobHistory, or with status {@code 400 (Bad Request)} if the jobHistory has already an ID.
     */
    @POST
    public Response createJobHistory(JobHistory jobHistory, @Context UriInfo uriInfo) {
        log.debug("REST request to save JobHistory : {}", jobHistory);
        if (jobHistory.id != null) {
            throw new BadRequestAlertException("A new jobHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = jobHistoryService.persistOrUpdate(jobHistory);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /job-histories} : Updates an existing jobHistory.
     *
     * @param jobHistory the jobHistory to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated jobHistory,
     * or with status {@code 400 (Bad Request)} if the jobHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobHistory couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateJobHistory(JobHistory jobHistory, @PathParam("id") Long id) {
        log.debug("REST request to update JobHistory : {}", jobHistory);
        if (jobHistory.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = jobHistoryService.persistOrUpdate(jobHistory);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobHistory.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /job-histories/:id} : delete the "id" jobHistory.
     *
     * @param id the id of the jobHistory to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteJobHistory(@PathParam("id") Long id) {
        log.debug("REST request to delete JobHistory : {}", id);
        jobHistoryService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /job-histories} : get all the jobHistories.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of jobHistories in body.
     */
    @GET
    public Response getAllJobHistories(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of JobHistories");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<JobHistory> result = jobHistoryService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /job-histories/:id} : get the "id" jobHistory.
     *
     * @param id the id of the jobHistory to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the jobHistory, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getJobHistory(@PathParam("id") Long id) {
        log.debug("REST request to get JobHistory : {}", id);
        Optional<JobHistory> jobHistory = jobHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobHistory);
    }
}
