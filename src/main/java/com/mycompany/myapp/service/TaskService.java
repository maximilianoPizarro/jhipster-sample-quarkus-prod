package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Task;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Task}.
 */
public interface TaskService {
    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    Task persistOrUpdate(Task task);

    /**
     * Delete the "id" task.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the tasks.
     * @return the list of entities.
     */
    public List<Task> findAll();

    /**
     * Get the "id" task.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Task> findOne(Long id);

    /**
     * Get all the tasks with eager load of many-to-many relationships.
     * @return the list of entities.
     */
    public List<Task> findAllWithEagerRelationships();
}
