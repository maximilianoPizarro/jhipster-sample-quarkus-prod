package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Task entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Task entity.\n@author The JHipster team.")
@Entity
@Table(name = "task")
@RegisterForReflection
public class Task extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "title")
    public String title;

    @Column(name = "description")
    public String description;

    @ManyToMany(mappedBy = "tasks")
    @JsonbTransient
    public Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", title='" + title + "'" + ", description='" + description + "'" + "}";
    }

    public Task update() {
        return update(this);
    }

    public Task persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Task update(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task can't be null");
        }
        var entity = Task.<Task>findById(task.id);
        if (entity != null) {
            entity.title = task.title;
            entity.description = task.description;
            entity.jobs = task.jobs;
        }
        return entity;
    }

    public static Task persistOrUpdate(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task can't be null");
        }
        if (task.id == null) {
            persist(task);
            return task;
        } else {
            return update(task);
        }
    }

    public static PanacheQuery<Task> findAllWithEagerRelationships() {
        return find("select distinct task from Task task");
    }

    public static Optional<Task> findOneWithEagerRelationships(Long id) {
        return find("select task from Task task where task.id =?1", id).firstResultOptional();
    }
}
