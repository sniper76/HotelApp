package com.hotel.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RoomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Room getRoomSample1() {
        return new Room().id(1L).no("no1").price(1).promoPrice(1).creator(1L).updater(1L);
    }

    public static Room getRoomSample2() {
        return new Room().id(2L).no("no2").price(2).promoPrice(2).creator(2L).updater(2L);
    }

    public static Room getRoomRandomSampleGenerator() {
        return new Room()
            .id(longCount.incrementAndGet())
            .no(UUID.randomUUID().toString())
            .price(intCount.incrementAndGet())
            .promoPrice(intCount.incrementAndGet())
            .creator(longCount.incrementAndGet())
            .updater(longCount.incrementAndGet());
    }
}
