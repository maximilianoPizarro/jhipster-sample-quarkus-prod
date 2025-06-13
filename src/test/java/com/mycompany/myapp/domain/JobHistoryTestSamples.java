package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class JobHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JobHistory getJobHistorySample1() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.id = 1L;
        return jobHistory;
    }

    public static JobHistory getJobHistorySample2() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.id = 2L;
        return jobHistory;
    }

    public static JobHistory getJobHistoryRandomSampleGenerator() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.id = longCount.incrementAndGet();
        return jobHistory;
    }
}
