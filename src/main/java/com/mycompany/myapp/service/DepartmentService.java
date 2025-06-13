package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Department}.
 */
public interface DepartmentService {
    /**
     * Save a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    Department persistOrUpdate(Department department);

    /**
     * Delete the "id" department.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the departments.
     * @return the list of entities.
     */
    public List<Department> findAll();

    /**
     * Get the "id" department.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Department> findOne(Long id);

    /**
     * Get all the Department where JobHistory is {@code null}.
     *
     * @return the list of entities.
     */
    List<Department> findAllWhereJobHistoryIsNull();
}
