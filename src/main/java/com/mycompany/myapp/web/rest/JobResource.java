package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Job;
import com.mycompany.myapp.service.Paged;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.vm.PageRequestVM;
import com.mycompany.myapp.web.rest.vm.SortRequestVM;
import com.mycompany.myapp.web.util.HeaderUtil;
import com.mycompany.myapp.web.util.PaginationUtil;
import com.mycompany.myapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Job}.
 */
@Path("/api/jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    @ConfigProperty(name = "application.name")
    String applicationName;

    /**
     * {@code POST  /jobs} : Create a new job.
     *
     * @param job the job to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new job, or with status {@code 400 (Bad Request)} if the job has already an ID.
     */
    @POST
    @Transactional
    public Response createJob(Job job, @Context UriInfo uriInfo) {
        log.debug("REST request to save Job : {}", job);
        if (job.id != null) {
            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Job.persistOrUpdate(job);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /jobs} : Updates an existing job.
     *
     * @param job the job to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated job,
     * or with status {@code 400 (Bad Request)} if the job is not valid,
     * or with status {@code 500 (Internal Server Error)} if the job couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateJob(Job job, @PathParam("id") Long id) {
        log.debug("REST request to update Job : {}", job);
        if (job.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Job.persistOrUpdate(job);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, job.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /jobs/:id} : delete the "id" job.
     *
     * @param id the id of the job to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteJob(@PathParam("id") Long id) {
        log.debug("REST request to delete Job : {}", id);
        Job.findByIdOptional(id).ifPresent(job -> {
            job.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /jobs} : get all the jobs.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @GET
    @Transactional
    public Response getAllJobs(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "filter") String filter,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Jobs");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<Job> result;
        if (eagerload) {
            var jobs = Job.findAllWithEagerRelationships().page(page).list();
            var totalCount = Job.findAll().count();
            var pageCount = Job.findAll().page(page).pageCount();
            result = new Paged<>(page.index, page.size, totalCount, pageCount, jobs);
        } else {
            result = new Paged<>(Job.findAll(sort).page(page));
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /jobs/:id} : get the "id" job.
     *
     * @param id the id of the job to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the job, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getJob(@PathParam("id") Long id) {
        log.debug("REST request to get Job : {}", id);
        Optional<Job> job = Job.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(job);
    }
}
