package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        Employee employee = new Employee();
        employee.id = 1L;
        employee.firstName = "firstName1";
        employee.lastName = "lastName1";
        employee.email = "email1";
        employee.phoneNumber = "phoneNumber1";
        employee.salary = 1L;
        employee.commissionPct = 1L;
        return employee;
    }

    public static Employee getEmployeeSample2() {
        Employee employee = new Employee();
        employee.id = 2L;
        employee.firstName = "firstName2";
        employee.lastName = "lastName2";
        employee.email = "email2";
        employee.phoneNumber = "phoneNumber2";
        employee.salary = 2L;
        employee.commissionPct = 2L;
        return employee;
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        Employee employee = new Employee();
        employee.id = longCount.incrementAndGet();
        employee.firstName = UUID.randomUUID().toString();
        employee.lastName = UUID.randomUUID().toString();
        employee.email = UUID.randomUUID().toString();
        employee.phoneNumber = UUID.randomUUID().toString();
        employee.salary = longCount.incrementAndGet();
        employee.commissionPct = longCount.incrementAndGet();
        return employee;
    }
}
