package com.mycompany.myapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.mycompany.myapp.TestUtil;
import com.mycompany.myapp.domain.JobHistory;
import com.mycompany.myapp.domain.enumeration.Language;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
public class JobHistoryResourceTest {

    private static final TypeRef<JobHistory> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<JobHistory>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    String adminToken;

    JobHistory jobHistory;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobHistory createEntity() {
        var jobHistory = new JobHistory();
        jobHistory.startDate = DEFAULT_START_DATE;
        jobHistory.endDate = DEFAULT_END_DATE;
        jobHistory.language = DEFAULT_LANGUAGE;
        return jobHistory;
    }

    @BeforeEach
    public void initTest() {
        jobHistory = createEntity();
    }

    @Test
    public void createJobHistory() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the JobHistory
        jobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the JobHistory in the database
        var jobHistoryList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        var testJobHistory = jobHistoryList.stream().filter(it -> jobHistory.id.equals(it.id)).findFirst().get();
        assertThat(testJobHistory.startDate).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJobHistory.endDate).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJobHistory.language).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    public void createJobHistoryWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the JobHistory with an existing ID
        jobHistory.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the JobHistory in the database
        var jobHistoryList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void updateJobHistory() {
        // Initialize the database
        jobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the jobHistory
        var updatedJobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories/{id}", jobHistory.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the jobHistory
        updatedJobHistory.startDate = UPDATED_START_DATE;
        updatedJobHistory.endDate = UPDATED_END_DATE;
        updatedJobHistory.language = UPDATED_LANGUAGE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedJobHistory)
            .when()
            .put("/api/job-histories/" + jobHistory.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the JobHistory in the database
        var jobHistoryList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        var testJobHistory = jobHistoryList.stream().filter(it -> updatedJobHistory.id.equals(it.id)).findFirst().get();
        assertThat(testJobHistory.startDate).isEqualTo(UPDATED_START_DATE);
        assertThat(testJobHistory.endDate).isEqualTo(UPDATED_END_DATE);
        assertThat(testJobHistory.language).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    public void updateNonExistingJobHistory() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .put("/api/job-histories/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the JobHistory in the database
        var jobHistoryList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteJobHistory() {
        // Initialize the database
        jobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the jobHistory
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/job-histories/{id}", jobHistory.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var jobHistoryList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(jobHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllJobHistories() {
        // Initialize the database
        jobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the jobHistoryList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(jobHistory.id.intValue()))
            .body("startDate", hasItem(TestUtil.formatDateTime(DEFAULT_START_DATE)))
            .body("endDate", hasItem(TestUtil.formatDateTime(DEFAULT_END_DATE)))
            .body("language", hasItem(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    public void getJobHistory() {
        // Initialize the database
        jobHistory = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(jobHistory)
            .when()
            .post("/api/job-histories")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the jobHistory
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/job-histories/{id}", jobHistory.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the jobHistory
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories/{id}", jobHistory.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(jobHistory.id.intValue()))
            .body("startDate", is(TestUtil.formatDateTime(DEFAULT_START_DATE)))
            .body("endDate", is(TestUtil.formatDateTime(DEFAULT_END_DATE)))
            .body("language", is(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    public void getNonExistingJobHistory() {
        // Get the jobHistory
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/job-histories/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
