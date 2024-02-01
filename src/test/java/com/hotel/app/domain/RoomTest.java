package com.hotel.app.domain;

import static com.hotel.app.domain.HotelTestSamples.*;
import static com.hotel.app.domain.RoomTestSamples.*;
import static com.hotel.app.domain.RoomTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hotel.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void roomTypeTest() throws Exception {
        Room room = getRoomRandomSampleGenerator();
        RoomType roomTypeBack = getRoomTypeRandomSampleGenerator();

        room.setRoomType(roomTypeBack);
        assertThat(room.getRoomType()).isEqualTo(roomTypeBack);

        room.roomType(null);
        assertThat(room.getRoomType()).isNull();
    }

    @Test
    void hotelTest() throws Exception {
        Room room = getRoomRandomSampleGenerator();
        Hotel hotelBack = getHotelRandomSampleGenerator();

        room.setHotel(hotelBack);
        assertThat(room.getHotel()).isEqualTo(hotelBack);

        room.hotel(null);
        assertThat(room.getHotel()).isNull();
    }
}
