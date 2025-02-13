package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Department getDepartmentSample1() {
        Department department = new Department();
        department.id = 1L;
        department.departmentName = "departmentName1";
        return department;
    }

    public static Department getDepartmentSample2() {
        Department department = new Department();
        department.id = 2L;
        department.departmentName = "departmentName2";
        return department;
    }

    public static Department getDepartmentRandomSampleGenerator() {
        Department department = new Department();
        department.id = longCount.incrementAndGet();
        department.departmentName = UUID.randomUUID().toString();
        return department;
    }
}
