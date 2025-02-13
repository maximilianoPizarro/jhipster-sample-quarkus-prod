package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@RegisterForReflection
public class Job extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "job_title")
    public String jobTitle;

    @Column(name = "min_salary")
    public Long minSalary;

    @Column(name = "max_salary")
    public Long maxSalary;

    @ManyToMany
    @JoinTable(
        name = "rel_job__task",
        joinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Task> tasks = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "employee_id")
    public Employee employee;

    @OneToOne(mappedBy = "job")
    public JobHistory jobHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", jobTitle='" + jobTitle + "'" + ", minSalary=" + minSalary + ", maxSalary=" + maxSalary + "}";
    }

    public Job update() {
        return update(this);
    }

    public Job persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Job update(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job can't be null");
        }
        var entity = Job.<Job>findById(job.id);
        if (entity != null) {
            entity.jobTitle = job.jobTitle;
            entity.minSalary = job.minSalary;
            entity.maxSalary = job.maxSalary;
            entity.tasks = job.tasks;
            entity.employee = job.employee;
            entity.jobHistory = job.jobHistory;
        }
        return entity;
    }

    public static Job persistOrUpdate(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job can't be null");
        }
        if (job.id == null) {
            persist(job);
            return job;
        } else {
            return update(job);
        }
    }

    public static PanacheQuery<Job> findAllWithEagerRelationships() {
        return find("select distinct job from Job job left join fetch job.tasks");
    }

    public static Optional<Job> findOneWithEagerRelationships(Long id) {
        return find("select job from Job job left join fetch job.tasks where job.id =?1", id).firstResultOptional();
    }
}
