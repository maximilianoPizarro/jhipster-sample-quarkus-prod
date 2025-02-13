package com.mycompany.myapp.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.mycompany.myapp.TestUtil;
import com.mycompany.myapp.domain.Employee;
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
public class EmployeeResourceTest {

    private static final TypeRef<Employee> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<Employee>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Long DEFAULT_SALARY = 1L;
    private static final Long UPDATED_SALARY = 2L;

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;

    String adminToken;

    Employee employee;

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
    public static Employee createEntity() {
        var employee = new Employee();
        employee.firstName = DEFAULT_FIRST_NAME;
        employee.lastName = DEFAULT_LAST_NAME;
        employee.email = DEFAULT_EMAIL;
        employee.phoneNumber = DEFAULT_PHONE_NUMBER;
        employee.hireDate = DEFAULT_HIRE_DATE;
        employee.salary = DEFAULT_SALARY;
        employee.commissionPct = DEFAULT_COMMISSION_PCT;
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity();
    }

    @Test
    public void createEmployee() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee
        employee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Employee in the database
        var employeeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        var testEmployee = employeeList.stream().filter(it -> employee.id.equals(it.id)).findFirst().get();
        assertThat(testEmployee.firstName).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.lastName).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.phoneNumber).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.hireDate).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.salary).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.commissionPct).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void createEmployeeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee with an existing ID
        employee.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void updateEmployee() {
        // Initialize the database
        employee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
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
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the employee
        var updatedEmployee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employee.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the employee
        updatedEmployee.firstName = UPDATED_FIRST_NAME;
        updatedEmployee.lastName = UPDATED_LAST_NAME;
        updatedEmployee.email = UPDATED_EMAIL;
        updatedEmployee.phoneNumber = UPDATED_PHONE_NUMBER;
        updatedEmployee.hireDate = UPDATED_HIRE_DATE;
        updatedEmployee.salary = UPDATED_SALARY;
        updatedEmployee.commissionPct = UPDATED_COMMISSION_PCT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEmployee)
            .when()
            .put("/api/employees/" + employee.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Employee in the database
        var employeeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        var testEmployee = employeeList.stream().filter(it -> updatedEmployee.id.equals(it.id)).findFirst().get();
        assertThat(testEmployee.firstName).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.lastName).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.email).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.phoneNumber).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.hireDate).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.salary).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.commissionPct).isEqualTo(UPDATED_COMMISSION_PCT);
    }

    @Test
    public void updateNonExistingEmployee() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
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
            .body(employee)
            .when()
            .put("/api/employees/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployee() {
        // Initialize the database
        employee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
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
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/employees/{id}", employee.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var employeeList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEmployees() {
        // Initialize the database
        employee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the employeeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(employee.id.intValue()))
            .body("firstName", hasItem(DEFAULT_FIRST_NAME))
            .body("lastName", hasItem(DEFAULT_LAST_NAME))
            .body("email", hasItem(DEFAULT_EMAIL))
            .body("phoneNumber", hasItem(DEFAULT_PHONE_NUMBER))
            .body("hireDate", hasItem(TestUtil.formatDateTime(DEFAULT_HIRE_DATE)))
            .body("salary", hasItem(DEFAULT_SALARY.intValue()))
            .body("commissionPct", hasItem(DEFAULT_COMMISSION_PCT.intValue()));
    }

    @Test
    public void getEmployee() {
        // Initialize the database
        employee = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employee)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the employee
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/employees/{id}", employee.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employee.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(employee.id.intValue()))
            .body("firstName", is(DEFAULT_FIRST_NAME))
            .body("lastName", is(DEFAULT_LAST_NAME))
            .body("email", is(DEFAULT_EMAIL))
            .body("phoneNumber", is(DEFAULT_PHONE_NUMBER))
            .body("hireDate", is(TestUtil.formatDateTime(DEFAULT_HIRE_DATE)))
            .body("salary", is(DEFAULT_SALARY.intValue()))
            .body("commissionPct", is(DEFAULT_COMMISSION_PCT.intValue()));
    }

    @Test
    public void getNonExistingEmployee() {
        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
