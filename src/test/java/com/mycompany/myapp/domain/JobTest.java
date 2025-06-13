package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmployeeTestSamples.*;
import static com.mycompany.myapp.domain.JobHistoryTestSamples.*;
import static com.mycompany.myapp.domain.JobTestSamples.*;
import static com.mycompany.myapp.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.TestUtil;
import org.junit.jupiter.api.Test;

class JobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = getJobSample1();
        Job job2 = new Job();
        assertThat(job1).isNotEqualTo(job2);

        job2.id = job1.id;
        assertThat(job1).isEqualTo(job2);

        job2 = getJobSample2();
        assertThat(job1).isNotEqualTo(job2);
    }
}
