package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Task getTaskSample1() {
        Task task = new Task();
        task.id = 1L;
        task.title = "title1";
        task.description = "description1";
        return task;
    }

    public static Task getTaskSample2() {
        Task task = new Task();
        task.id = 2L;
        task.title = "title2";
        task.description = "description2";
        return task;
    }

    public static Task getTaskRandomSampleGenerator() {
        Task task = new Task();
        task.id = longCount.incrementAndGet();
        task.title = UUID.randomUUID().toString();
        task.description = UUID.randomUUID().toString();
        return task;
    }
}
