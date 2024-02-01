package com.hotel.app.domain;

import static com.hotel.app.domain.ReservationTestSamples.*;
import static com.hotel.app.domain.RoomTestSamples.*;
import static com.hotel.app.domain.VoucherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hotel.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void roomTest() throws Exception {
        Reservation reservation = getReservationRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        reservation.setRoom(roomBack);
        assertThat(reservation.getRoom()).isEqualTo(roomBack);

        reservation.room(null);
        assertThat(reservation.getRoom()).isNull();
    }

    @Test
    void voucherTest() throws Exception {
        Reservation reservation = getReservationRandomSampleGenerator();
        Voucher voucherBack = getVoucherRandomSampleGenerator();

        reservation.setVoucher(voucherBack);
        assertThat(reservation.getVoucher()).isEqualTo(voucherBack);

        reservation.voucher(null);
        assertThat(reservation.getVoucher()).isNull();
    }
}
