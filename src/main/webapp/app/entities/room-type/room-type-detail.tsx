import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room-type.reducer';

export const RoomTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roomTypeEntity = useAppSelector(state => state.roomType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomTypeDetailsHeading">
          <Translate contentKey="hotelApp.roomType.detail.title">RoomType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hotelApp.roomType.name">Name</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="hotelApp.roomType.description">Description</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.description}</dd>
          <dt>
            <span id="creator">
              <Translate contentKey="hotelApp.roomType.creator">Creator</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.creator}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hotelApp.roomType.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.createdAt ? <TextFormat value={roomTypeEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updater">
              <Translate contentKey="hotelApp.roomType.updater">Updater</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.updater}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hotelApp.roomType.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{roomTypeEntity.updatedAt ? <TextFormat value={roomTypeEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/room-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room-type/${roomTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomTypeDetail;
