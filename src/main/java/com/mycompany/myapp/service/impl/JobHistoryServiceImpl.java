package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.JobHistory;
import com.mycompany.myapp.service.JobHistoryService;
import com.mycompany.myapp.service.Paged;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class JobHistoryServiceImpl implements JobHistoryService {

    private final Logger log = LoggerFactory.getLogger(JobHistoryServiceImpl.class);

    @Override
    @Transactional
    public JobHistory persistOrUpdate(JobHistory jobHistory) {
        log.debug("Request to save JobHistory : {}", jobHistory);
        return JobHistory.persistOrUpdate(jobHistory);
    }

    /**
     * Delete the JobHistory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete JobHistory : {}", id);
        JobHistory.findByIdOptional(id).ifPresent(jobHistory -> {
            jobHistory.delete();
        });
    }

    /**
     * Get one jobHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<JobHistory> findOne(Long id) {
        log.debug("Request to get JobHistory : {}", id);
        return JobHistory.findByIdOptional(id);
    }

    /**
     * Get all the jobHistories.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<JobHistory> findAll(Page page) {
        log.debug("Request to get all JobHistories");
        return new Paged<>(JobHistory.findAll().page(page));
    }
}
