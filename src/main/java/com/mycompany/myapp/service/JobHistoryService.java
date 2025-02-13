package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.JobHistory;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.JobHistory}.
 */
public interface JobHistoryService {
    /**
     * Save a jobHistory.
     *
     * @param jobHistory the entity to save.
     * @return the persisted entity.
     */
    JobHistory persistOrUpdate(JobHistory jobHistory);

    /**
     * Delete the "id" jobHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the jobHistories.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<JobHistory> findAll(Page page);

    /**
     * Get the "id" jobHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobHistory> findOne(Long id);
}
