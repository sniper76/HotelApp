import hotel from 'app/entities/hotel/hotel.reducer';
import roomType from 'app/entities/room-type/room-type.reducer';
import room from 'app/entities/room/room.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
import voucher from 'app/entities/voucher/voucher.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  hotel,
  roomType,
  room,
  reservation,
  voucher,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
