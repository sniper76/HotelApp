package com.hotel.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoomTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoomType getRoomTypeSample1() {
        return new RoomType().id(1L).name("name1").description("description1").creator(1L).updater(1L);
    }

    public static RoomType getRoomTypeSample2() {
        return new RoomType().id(2L).name("name2").description("description2").creator(2L).updater(2L);
    }

    public static RoomType getRoomTypeRandomSampleGenerator() {
        return new RoomType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .creator(longCount.incrementAndGet())
            .updater(longCount.incrementAndGet());
    }
}
