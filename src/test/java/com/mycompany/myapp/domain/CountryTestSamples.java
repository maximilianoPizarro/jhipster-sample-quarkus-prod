package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Country getCountrySample1() {
        Country country = new Country();
        country.id = 1L;
        country.countryName = "countryName1";
        return country;
    }

    public static Country getCountrySample2() {
        Country country = new Country();
        country.id = 2L;
        country.countryName = "countryName2";
        return country;
    }

    public static Country getCountryRandomSampleGenerator() {
        Country country = new Country();
        country.id = longCount.incrementAndGet();
        country.countryName = UUID.randomUUID().toString();
        return country;
    }
}
