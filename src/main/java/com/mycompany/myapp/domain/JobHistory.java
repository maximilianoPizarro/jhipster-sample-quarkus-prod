package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.Language;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A JobHistory.
 */
@Entity
@Table(name = "job_history")
@RegisterForReflection
public class JobHistory extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "start_date")
    public Instant startDate;

    @Column(name = "end_date")
    public Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    public Language language;

    @OneToOne
    @JoinColumn(unique = true)
    public Job job;

    @OneToOne
    @JoinColumn(unique = true)
    public Department department;

    @OneToOne
    @JoinColumn(unique = true)
    public Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobHistory)) {
            return false;
        }
        return id != null && id.equals(((JobHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "JobHistory{" +
            "id=" +
            id +
            ", startDate='" +
            startDate +
            "'" +
            ", endDate='" +
            endDate +
            "'" +
            ", language='" +
            language +
            "'" +
            "}"
        );
    }

    public JobHistory update() {
        return update(this);
    }

    public JobHistory persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static JobHistory update(JobHistory jobHistory) {
        if (jobHistory == null) {
            throw new IllegalArgumentException("jobHistory can't be null");
        }
        var entity = JobHistory.<JobHistory>findById(jobHistory.id);
        if (entity != null) {
            entity.startDate = jobHistory.startDate;
            entity.endDate = jobHistory.endDate;
            entity.language = jobHistory.language;
            entity.job = jobHistory.job;
            entity.department = jobHistory.department;
            entity.employee = jobHistory.employee;
        }
        return entity;
    }

    public static JobHistory persistOrUpdate(JobHistory jobHistory) {
        if (jobHistory == null) {
            throw new IllegalArgumentException("jobHistory can't be null");
        }
        if (jobHistory.id == null) {
            persist(jobHistory);
            return jobHistory;
        } else {
            return update(jobHistory);
        }
    }
}
