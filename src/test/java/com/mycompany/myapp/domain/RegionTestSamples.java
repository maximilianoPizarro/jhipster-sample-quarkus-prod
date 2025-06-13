package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RegionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Region getRegionSample1() {
        Region region = new Region();
        region.id = 1L;
        region.regionName = "regionName1";
        return region;
    }

    public static Region getRegionSample2() {
        Region region = new Region();
        region.id = 2L;
        region.regionName = "regionName2";
        return region;
    }

    public static Region getRegionRandomSampleGenerator() {
        Region region = new Region();
        region.id = longCount.incrementAndGet();
        region.regionName = UUID.randomUUID().toString();
        return region;
    }
}
