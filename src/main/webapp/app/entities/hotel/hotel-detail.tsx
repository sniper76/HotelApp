import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hotel.reducer';

export const HotelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hotelEntity = useAppSelector(state => state.hotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hotelDetailsHeading">
          <Translate contentKey="hotelApp.hotel.detail.title">Hotel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hotelApp.hotel.name">Name</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="hotelApp.hotel.type">Type</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.type}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="hotelApp.hotel.location">Location</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.location}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="hotelApp.hotel.description">Description</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.description}</dd>
          <dt>
            <span id="creator">
              <Translate contentKey="hotelApp.hotel.creator">Creator</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.creator}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hotelApp.hotel.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.createdAt ? <TextFormat value={hotelEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updater">
              <Translate contentKey="hotelApp.hotel.updater">Updater</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.updater}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hotelApp.hotel.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{hotelEntity.updatedAt ? <TextFormat value={hotelEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hotelApp.hotel.user">User</Translate>
          </dt>
          <dd>{hotelEntity.user ? hotelEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hotel/${hotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HotelDetail;
