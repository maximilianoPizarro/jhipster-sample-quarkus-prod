package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Task;
import com.mycompany.myapp.service.TaskService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    @Transactional
    public Task persistOrUpdate(Task task) {
        log.debug("Request to save Task : {}", task);
        return Task.persistOrUpdate(task);
    }

    /**
     * Delete the Task by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        Task.findByIdOptional(id).ifPresent(task -> {
            task.delete();
        });
    }

    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Task> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return Task.findOneWithEagerRelationships(id);
    }

    /**
     * Get all the tasks.
     * @return the list of entities.
     */
    @Override
    public List<Task> findAll() {
        log.debug("Request to get all Tasks");
        return Task.findAllWithEagerRelationships().list();
    }

    /**
     * Get all the tasks with eager load of many-to-many relationships.
     * @return the list of entities.
     */
    public List<Task> findAllWithEagerRelationships() {
        return Task.findAllWithEagerRelationships().list();
    }
}
