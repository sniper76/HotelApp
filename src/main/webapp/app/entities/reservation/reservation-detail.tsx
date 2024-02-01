import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">
          <Translate contentKey="hotelApp.reservation.detail.title">Reservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="checkInDate">
              <Translate contentKey="hotelApp.reservation.checkInDate">Check In Date</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.checkInDate ? (
              <TextFormat value={reservationEntity.checkInDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="checkOutDate">
              <Translate contentKey="hotelApp.reservation.checkOutDate">Check Out Date</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.checkOutDate ? (
              <TextFormat value={reservationEntity.checkOutDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="creator">
              <Translate contentKey="hotelApp.reservation.creator">Creator</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.creator}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hotelApp.reservation.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.createdAt ? <TextFormat value={reservationEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updater">
              <Translate contentKey="hotelApp.reservation.updater">Updater</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.updater}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hotelApp.reservation.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.updatedAt ? <TextFormat value={reservationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hotelApp.reservation.room">Room</Translate>
          </dt>
          <dd>{reservationEntity.room ? reservationEntity.room.no : ''}</dd>
          <dt>
            <Translate contentKey="hotelApp.reservation.voucher">Voucher</Translate>
          </dt>
          <dd>{reservationEntity.voucher ? reservationEntity.voucher.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
