import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room.reducer';

export const RoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roomEntity = useAppSelector(state => state.room.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomDetailsHeading">
          <Translate contentKey="hotelApp.room.detail.title">Room</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roomEntity.id}</dd>
          <dt>
            <span id="no">
              <Translate contentKey="hotelApp.room.no">No</Translate>
            </span>
          </dt>
          <dd>{roomEntity.no}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="hotelApp.room.price">Price</Translate>
            </span>
          </dt>
          <dd>{roomEntity.price}</dd>
          <dt>
            <span id="promoPrice">
              <Translate contentKey="hotelApp.room.promoPrice">Promo Price</Translate>
            </span>
          </dt>
          <dd>{roomEntity.promoPrice}</dd>
          <dt>
            <span id="creator">
              <Translate contentKey="hotelApp.room.creator">Creator</Translate>
            </span>
          </dt>
          <dd>{roomEntity.creator}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hotelApp.room.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.createdAt ? <TextFormat value={roomEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updater">
              <Translate contentKey="hotelApp.room.updater">Updater</Translate>
            </span>
          </dt>
          <dd>{roomEntity.updater}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hotelApp.room.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.updatedAt ? <TextFormat value={roomEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hotelApp.room.roomType">Room Type</Translate>
          </dt>
          <dd>{roomEntity.roomType ? roomEntity.roomType.id : ''}</dd>
          <dt>
            <Translate contentKey="hotelApp.room.hotel">Hotel</Translate>
          </dt>
          <dd>{roomEntity.hotel ? roomEntity.hotel.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room/${roomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomDetail;
