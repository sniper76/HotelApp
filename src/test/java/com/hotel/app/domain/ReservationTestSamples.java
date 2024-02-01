package com.hotel.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reservation getReservationSample1() {
        return new Reservation().id(1L).creator(1L).updater(1L);
    }

    public static Reservation getReservationSample2() {
        return new Reservation().id(2L).creator(2L).updater(2L);
    }

    public static Reservation getReservationRandomSampleGenerator() {
        return new Reservation().id(longCount.incrementAndGet()).creator(longCount.incrementAndGet()).updater(longCount.incrementAndGet());
    }
}
