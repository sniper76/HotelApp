import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRoomType } from 'app/shared/model/room-type.model';
import { getEntities as getRoomTypes } from 'app/entities/room-type/room-type.reducer';
import { IHotel } from 'app/shared/model/hotel.model';
import { getEntities as getHotels } from 'app/entities/hotel/hotel.reducer';
import { IRoom } from 'app/shared/model/room.model';
import { getEntity, updateEntity, createEntity, reset } from './room.reducer';

export const RoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const roomTypes = useAppSelector(state => state.roomType.entities);
  const hotels = useAppSelector(state => state.hotel.entities);
  const roomEntity = useAppSelector(state => state.room.entity);
  const loading = useAppSelector(state => state.room.loading);
  const updating = useAppSelector(state => state.room.updating);
  const updateSuccess = useAppSelector(state => state.room.updateSuccess);

  const handleClose = () => {
    navigate('/room' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRoomTypes({}));
    dispatch(getHotels({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.promoPrice !== undefined && typeof values.promoPrice !== 'number') {
      values.promoPrice = Number(values.promoPrice);
    }
    if (values.creator !== undefined && typeof values.creator !== 'number') {
      values.creator = Number(values.creator);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    if (values.updater !== undefined && typeof values.updater !== 'number') {
      values.updater = Number(values.updater);
    }
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...roomEntity,
      ...values,
      roomType: roomTypes.find(it => it.id.toString() === values.roomType.toString()),
      hotel: hotels.find(it => it.id.toString() === values.hotel.toString()),
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
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...roomEntity,
          createdAt: convertDateTimeFromServer(roomEntity.createdAt),
          updatedAt: convertDateTimeFromServer(roomEntity.updatedAt),
          roomType: roomEntity?.roomType?.id,
          hotel: roomEntity?.hotel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelApp.room.home.createOrEditLabel" data-cy="RoomCreateUpdateHeading">
            <Translate contentKey="hotelApp.room.home.createOrEditLabel">Create or edit a Room</Translate>
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
                  id="room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hotelApp.room.no')}
                id="room-no"
                name="no"
                data-cy="no"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('hotelApp.room.price')} id="room-price" name="price" data-cy="price" type="text" />
              <ValidatedField
                label={translate('hotelApp.room.promoPrice')}
                id="room-promoPrice"
                name="promoPrice"
                data-cy="promoPrice"
                type="text"
              />
              <ValidatedField label={translate('hotelApp.room.creator')} id="room-creator" name="creator" data-cy="creator" type="text" />
              <ValidatedField
                label={translate('hotelApp.room.createdAt')}
                id="room-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('hotelApp.room.updater')} id="room-updater" name="updater" data-cy="updater" type="text" />
              <ValidatedField
                label={translate('hotelApp.room.updatedAt')}
                id="room-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="room-roomType"
                name="roomType"
                data-cy="roomType"
                label={translate('hotelApp.room.roomType')}
                type="select"
              >
                <option value="" key="0" />
                {roomTypes
                  ? roomTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="room-hotel" name="hotel" data-cy="hotel" label={translate('hotelApp.room.hotel')} type="select">
                <option value="" key="0" />
                {hotels
                  ? hotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/room" replace color="info">
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

export default RoomUpdate;
