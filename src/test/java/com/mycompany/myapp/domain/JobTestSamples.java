package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Job getJobSample1() {
        Job job = new Job();
        job.id = 1L;
        job.jobTitle = "jobTitle1";
        job.minSalary = 1L;
        job.maxSalary = 1L;
        return job;
    }

    public static Job getJobSample2() {
        Job job = new Job();
        job.id = 2L;
        job.jobTitle = "jobTitle2";
        job.minSalary = 2L;
        job.maxSalary = 2L;
        return job;
    }

    public static Job getJobRandomSampleGenerator() {
        Job job = new Job();
        job.id = longCount.incrementAndGet();
        job.jobTitle = UUID.randomUUID().toString();
        job.minSalary = longCount.incrementAndGet();
        job.maxSalary = longCount.incrementAndGet();
        return job;
    }
}
