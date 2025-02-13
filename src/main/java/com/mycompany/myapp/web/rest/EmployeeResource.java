package com.mycompany.myapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.mycompany.myapp.domain.Employee;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Employee}.
 */
@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @ConfigProperty(name = "application.name")
    String applicationName;

    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employee the employee to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new employee, or with status {@code 400 (Bad Request)} if the employee has already an ID.
     */
    @POST
    @Transactional
    public Response createEmployee(Employee employee, @Context UriInfo uriInfo) {
        log.debug("REST request to save Employee : {}", employee);
        if (employee.id != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Employee.persistOrUpdate(employee);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /employees} : Updates an existing employee.
     *
     * @param employee the employee to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated employee,
     * or with status {@code 400 (Bad Request)} if the employee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employee couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEmployee(Employee employee, @PathParam("id") Long id) {
        log.debug("REST request to update Employee : {}", employee);
        if (employee.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Employee.persistOrUpdate(employee);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employee.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employee to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEmployee(@PathParam("id") Long id) {
        log.debug("REST request to delete Employee : {}", id);
        Employee.findByIdOptional(id).ifPresent(employee -> {
            employee.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GET
    public Response getAllEmployees(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "filter") String filter
    ) {
        log.debug("REST request to get a page of Employees");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        var result = new Paged<>(Employee.findAll(sort).page(page));
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employee to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the employee, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<Employee> employee = Employee.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(employee);
    }
}
