package com.mycompany.myapp.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * The Employee entity.
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "employee")
@RegisterForReflection
public class Employee extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "email")
    public String email;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "hire_date")
    public Instant hireDate;

    @Column(name = "salary")
    public Long salary;

    @Column(name = "commission_pct")
    public Long commissionPct;

    @OneToMany(mappedBy = "employee")
    public Set<Job> jobs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "manager_id")
    public Employee manager;

    @ManyToOne
    @JoinColumn(name = "department_id")
    public Department department;

    @OneToOne(mappedBy = "employee")
    public JobHistory jobHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Employee{" +
            "id=" +
            id +
            ", firstName='" +
            firstName +
            "'" +
            ", lastName='" +
            lastName +
            "'" +
            ", email='" +
            email +
            "'" +
            ", phoneNumber='" +
            phoneNumber +
            "'" +
            ", hireDate='" +
            hireDate +
            "'" +
            ", salary=" +
            salary +
            ", commissionPct=" +
            commissionPct +
            "}"
        );
    }

    public Employee update() {
        return update(this);
    }

    public Employee persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Employee update(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("employee can't be null");
        }
        var entity = Employee.<Employee>findById(employee.id);
        if (entity != null) {
            entity.firstName = employee.firstName;
            entity.lastName = employee.lastName;
            entity.email = employee.email;
            entity.phoneNumber = employee.phoneNumber;
            entity.hireDate = employee.hireDate;
            entity.salary = employee.salary;
            entity.commissionPct = employee.commissionPct;
            entity.jobs = employee.jobs;
            entity.manager = employee.manager;
            entity.department = employee.department;
            entity.jobHistory = employee.jobHistory;
        }
        return entity;
    }

    public static Employee persistOrUpdate(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("employee can't be null");
        }
        if (employee.id == null) {
            persist(employee);
            return employee;
        } else {
            return update(employee);
        }
    }
}
