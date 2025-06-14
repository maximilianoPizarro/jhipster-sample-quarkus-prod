package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAllPropertiesEquals(Location expected, Location actual) {
        assertLocationAutoGeneratedPropertiesEquals(expected, actual);
        assertLocationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAllUpdatablePropertiesEquals(Location expected, Location actual) {
        assertLocationUpdatableFieldsEquals(expected, actual);
        assertLocationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationAutoGeneratedPropertiesEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationUpdatableFieldsEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location relevant properties")
            .satisfies(e -> assertThat(e.streetAddress).as("check streetAddress").isEqualTo(actual.streetAddress))
            .satisfies(e -> assertThat(e.postalCode).as("check postalCode").isEqualTo(actual.postalCode))
            .satisfies(e -> assertThat(e.city).as("check city").isEqualTo(actual.city))
            .satisfies(e -> assertThat(e.stateProvince).as("check stateProvince").isEqualTo(actual.stateProvince));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLocationUpdatableRelationshipsEquals(Location expected, Location actual) {
        assertThat(expected)
            .as("Verify Location relationships")
            .satisfies(e -> assertThat(e.country).as("check country").isEqualTo(actual.country));
    }
}
