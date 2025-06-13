package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.service.DepartmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Override
    @Transactional
    public Department persistOrUpdate(Department department) {
        log.debug("Request to save Department : {}", department);
        return Department.persistOrUpdate(department);
    }

    /**
     * Delete the Department by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        Department.findByIdOptional(id).ifPresent(department -> {
            department.delete();
        });
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Department> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return Department.findByIdOptional(id);
    }

    /**
     * Get all the departments.
     * @return the list of entities.
     */
    @Override
    public List<Department> findAll() {
        log.debug("Request to get all Departments");
        return Department.findAll().list();
    }
}
