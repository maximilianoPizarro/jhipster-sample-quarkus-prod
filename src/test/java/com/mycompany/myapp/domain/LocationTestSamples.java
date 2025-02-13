package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Location getLocationSample1() {
        Location location = new Location();
        location.id = 1L;
        location.streetAddress = "streetAddress1";
        location.postalCode = "postalCode1";
        location.city = "city1";
        location.stateProvince = "stateProvince1";
        return location;
    }

    public static Location getLocationSample2() {
        Location location = new Location();
        location.id = 2L;
        location.streetAddress = "streetAddress2";
        location.postalCode = "postalCode2";
        location.city = "city2";
        location.stateProvince = "stateProvince2";
        return location;
    }

    public static Location getLocationRandomSampleGenerator() {
        Location location = new Location();
        location.id = longCount.incrementAndGet();
        location.streetAddress = UUID.randomUUID().toString();
        location.postalCode = UUID.randomUUID().toString();
        location.city = UUID.randomUUID().toString();
        location.stateProvince = UUID.randomUUID().toString();
        return location;
    }
}
