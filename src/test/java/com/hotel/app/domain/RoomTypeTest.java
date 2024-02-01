package com.hotel.app.domain;

import static com.hotel.app.domain.RoomTestSamples.*;
import static com.hotel.app.domain.RoomTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hotel.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomType.class);
        RoomType roomType1 = getRoomTypeSample1();
        RoomType roomType2 = new RoomType();
        assertThat(roomType1).isNotEqualTo(roomType2);

        roomType2.setId(roomType1.getId());
        assertThat(roomType1).isEqualTo(roomType2);

        roomType2 = getRoomTypeSample2();
        assertThat(roomType1).isNotEqualTo(roomType2);
    }

    @Test
    void roomTest() throws Exception {
        RoomType roomType = getRoomTypeRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        roomType.setRoom(roomBack);
        assertThat(roomType.getRoom()).isEqualTo(roomBack);
        assertThat(roomBack.getRoomType()).isEqualTo(roomType);

        roomType.room(null);
        assertThat(roomType.getRoom()).isNull();
        assertThat(roomBack.getRoomType()).isNull();
    }
}
