package com.hotel.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VoucherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Voucher getVoucherSample1() {
        return new Voucher().id(1L).price(1).salePrice(1).creator(1L).updater(1L);
    }

    public static Voucher getVoucherSample2() {
        return new Voucher().id(2L).price(2).salePrice(2).creator(2L).updater(2L);
    }

    public static Voucher getVoucherRandomSampleGenerator() {
        return new Voucher()
            .id(longCount.incrementAndGet())
            .price(intCount.incrementAndGet())
            .salePrice(intCount.incrementAndGet())
            .creator(longCount.incrementAndGet())
            .updater(longCount.incrementAndGet());
    }
}
