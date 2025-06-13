package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@RegisterForReflection
public class Department extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "department_name", nullable = false)
    public String departmentName;

    @OneToOne
    @JoinColumn(unique = true)
    public Location location;

    /**
     * A relationship
     */
    @ApiModelProperty(value = "A relationship")
    @OneToMany(mappedBy = "department")
    public Set<Employee> employees = new HashSet<>();

    @OneToOne(mappedBy = "department")
    public JobHistory jobHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", departmentName='" + departmentName + "'" + "}";
    }

    public Department update() {
        return update(this);
    }

    public Department persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Department update(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("department can't be null");
        }
        var entity = Department.<Department>findById(department.id);
        if (entity != null) {
            entity.departmentName = department.departmentName;
            entity.location = department.location;
            entity.employees = department.employees;
            entity.jobHistory = department.jobHistory;
        }
        return entity;
    }

    public static Department persistOrUpdate(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("department can't be null");
        }
        if (department.id == null) {
            persist(department);
            return department;
        } else {
            return update(department);
        }
    }
}
