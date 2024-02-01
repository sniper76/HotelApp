package com.hotel.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HotelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hotel getHotelSample1() {
        return new Hotel().id(1L).name("name1").location("location1").description("description1").creator(1L).updater(1L);
    }

    public static Hotel getHotelSample2() {
        return new Hotel().id(2L).name("name2").location("location2").description("description2").creator(2L).updater(2L);
    }

    public static Hotel getHotelRandomSampleGenerator() {
        return new Hotel()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .creator(longCount.incrementAndGet())
            .updater(longCount.incrementAndGet());
    }
}
