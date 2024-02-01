import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRoom } from 'app/shared/model/room.model';
import { getEntities as getRooms } from 'app/entities/room/room.reducer';
import { IVoucher } from 'app/shared/model/voucher.model';
import { getEntities as getVouchers } from 'app/entities/voucher/voucher.reducer';
import { IReservation } from 'app/shared/model/reservation.model';
import { getEntity, updateEntity, createEntity, reset } from './reservation.reducer';

export const ReservationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const rooms = useAppSelector(state => state.room.entities);
  const vouchers = useAppSelector(state => state.voucher.entities);
  const reservationEntity = useAppSelector(state => state.reservation.entity);
  const loading = useAppSelector(state => state.reservation.loading);
  const updating = useAppSelector(state => state.reservation.updating);
  const updateSuccess = useAppSelector(state => state.reservation.updateSuccess);

  const handleClose = () => {
    navigate('/reservation' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRooms({}));
    dispatch(getVouchers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.checkInDate = convertDateTimeToServer(values.checkInDate);
    values.checkOutDate = convertDateTimeToServer(values.checkOutDate);
    if (values.creator !== undefined && typeof values.creator !== 'number') {
      values.creator = Number(values.creator);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    if (values.updater !== undefined && typeof values.updater !== 'number') {
      values.updater = Number(values.updater);
    }
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...reservationEntity,
      ...values,
      room: rooms.find(it => it.id.toString() === values.room.toString()),
      voucher: vouchers.find(it => it.id.toString() === values.voucher.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          checkInDate: displayDefaultDateTime(),
          checkOutDate: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...reservationEntity,
          checkInDate: convertDateTimeFromServer(reservationEntity.checkInDate),
          checkOutDate: convertDateTimeFromServer(reservationEntity.checkOutDate),
          createdAt: convertDateTimeFromServer(reservationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(reservationEntity.updatedAt),
          room: reservationEntity?.room?.id,
          voucher: reservationEntity?.voucher?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelApp.reservation.home.createOrEditLabel" data-cy="ReservationCreateUpdateHeading">
            <Translate contentKey="hotelApp.reservation.home.createOrEditLabel">Create or edit a Reservation</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="reservation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hotelApp.reservation.checkInDate')}
                id="reservation-checkInDate"
                name="checkInDate"
                data-cy="checkInDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hotelApp.reservation.checkOutDate')}
                id="reservation-checkOutDate"
                name="checkOutDate"
                data-cy="checkOutDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hotelApp.reservation.creator')}
                id="reservation-creator"
                name="creator"
                data-cy="creator"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.reservation.createdAt')}
                id="reservation-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hotelApp.reservation.updater')}
                id="reservation-updater"
                name="updater"
                data-cy="updater"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.reservation.updatedAt')}
                id="reservation-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="reservation-room" name="room" data-cy="room" label={translate('hotelApp.reservation.room')} type="select">
                <option value="" key="0" />
                {rooms
                  ? rooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.no}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reservation-voucher"
                name="voucher"
                data-cy="voucher"
                label={translate('hotelApp.reservation.voucher')}
                type="select"
              >
                <option value="" key="0" />
                {vouchers
                  ? vouchers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reservation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservationUpdate;
