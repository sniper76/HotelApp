package com.hotel.app.domain;

import static com.hotel.app.domain.ReservationTestSamples.*;
import static com.hotel.app.domain.VoucherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hotel.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VoucherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voucher.class);
        Voucher voucher1 = getVoucherSample1();
        Voucher voucher2 = new Voucher();
        assertThat(voucher1).isNotEqualTo(voucher2);

        voucher2.setId(voucher1.getId());
        assertThat(voucher1).isEqualTo(voucher2);

        voucher2 = getVoucherSample2();
        assertThat(voucher1).isNotEqualTo(voucher2);
    }

    @Test
    void reservationTest() throws Exception {
        Voucher voucher = getVoucherRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        voucher.addReservation(reservationBack);
        assertThat(voucher.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getVoucher()).isEqualTo(voucher);

        voucher.removeReservation(reservationBack);
        assertThat(voucher.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getVoucher()).isNull();

        voucher.reservations(new HashSet<>(Set.of(reservationBack)));
        assertThat(voucher.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getVoucher()).isEqualTo(voucher);

        voucher.setReservations(new HashSet<>());
        assertThat(voucher.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getVoucher()).isNull();
    }
}
