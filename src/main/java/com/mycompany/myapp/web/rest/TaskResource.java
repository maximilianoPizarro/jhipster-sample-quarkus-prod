package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Task;
import com.mycompany.myapp.service.TaskService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Task}.
 */
@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    TaskService taskService;

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param task the task to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new task, or with status {@code 400 (Bad Request)} if the task has already an ID.
     */
    @POST
    public Response createTask(Task task, @Context UriInfo uriInfo) {
        log.debug("REST request to save Task : {}", task);
        if (task.id != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = taskService.persistOrUpdate(task);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /tasks} : Updates an existing task.
     *
     * @param task the task to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateTask(Task task, @PathParam("id") Long id) {
        log.debug("REST request to update Task : {}", task);
        if (task.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = taskService.persistOrUpdate(task);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the task to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GET
    public List<Task> getAllTasks(@QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get all Tasks");
        return taskService.findAll();
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the task to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the task, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") Long id) {
        log.debug("REST request to get Task : {}", id);
        Optional<Task> task = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(task);
    }
}
