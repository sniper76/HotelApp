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
import { IRoomType } from 'app/shared/model/room-type.model';
import { getEntity, updateEntity, createEntity, reset } from './room-type.reducer';

export const RoomTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const rooms = useAppSelector(state => state.room.entities);
  const roomTypeEntity = useAppSelector(state => state.roomType.entity);
  const loading = useAppSelector(state => state.roomType.loading);
  const updating = useAppSelector(state => state.roomType.updating);
  const updateSuccess = useAppSelector(state => state.roomType.updateSuccess);

  const handleClose = () => {
    navigate('/room-type' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRooms({}));
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
    if (values.creator !== undefined && typeof values.creator !== 'number') {
      values.creator = Number(values.creator);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    if (values.updater !== undefined && typeof values.updater !== 'number') {
      values.updater = Number(values.updater);
    }
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...roomTypeEntity,
      ...values,
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
          ...roomTypeEntity,
          createdAt: convertDateTimeFromServer(roomTypeEntity.createdAt),
          updatedAt: convertDateTimeFromServer(roomTypeEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelApp.roomType.home.createOrEditLabel" data-cy="RoomTypeCreateUpdateHeading">
            <Translate contentKey="hotelApp.roomType.home.createOrEditLabel">Create or edit a RoomType</Translate>
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
                  id="room-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hotelApp.roomType.name')}
                id="room-type-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hotelApp.roomType.description')}
                id="room-type-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.roomType.creator')}
                id="room-type-creator"
                name="creator"
                data-cy="creator"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.roomType.createdAt')}
                id="room-type-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hotelApp.roomType.updater')}
                id="room-type-updater"
                name="updater"
                data-cy="updater"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.roomType.updatedAt')}
                id="room-type-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/room-type" replace color="info">
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

export default RoomTypeUpdate;
