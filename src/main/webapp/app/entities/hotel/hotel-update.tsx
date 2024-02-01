import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IHotel } from 'app/shared/model/hotel.model';
import { HotelType } from 'app/shared/model/enumerations/hotel-type.model';
import { getEntity, updateEntity, createEntity, reset } from './hotel.reducer';

export const HotelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const hotelEntity = useAppSelector(state => state.hotel.entity);
  const loading = useAppSelector(state => state.hotel.loading);
  const updating = useAppSelector(state => state.hotel.updating);
  const updateSuccess = useAppSelector(state => state.hotel.updateSuccess);
  const hotelTypeValues = Object.keys(HotelType);

  const handleClose = () => {
    navigate('/hotel' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
      ...hotelEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          type: 'HOTEL',
          ...hotelEntity,
          createdAt: convertDateTimeFromServer(hotelEntity.createdAt),
          updatedAt: convertDateTimeFromServer(hotelEntity.updatedAt),
          user: hotelEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelApp.hotel.home.createOrEditLabel" data-cy="HotelCreateUpdateHeading">
            <Translate contentKey="hotelApp.hotel.home.createOrEditLabel">Create or edit a Hotel</Translate>
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
                  id="hotel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hotelApp.hotel.name')}
                id="hotel-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('hotelApp.hotel.type')} id="hotel-type" name="type" data-cy="type" type="select">
                {hotelTypeValues.map(hotelType => (
                  <option value={hotelType} key={hotelType}>
                    {translate('hotelApp.HotelType.' + hotelType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hotelApp.hotel.location')}
                id="hotel-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.hotel.description')}
                id="hotel-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('hotelApp.hotel.creator')} id="hotel-creator" name="creator" data-cy="creator" type="text" />
              <ValidatedField
                label={translate('hotelApp.hotel.createdAt')}
                id="hotel-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('hotelApp.hotel.updater')} id="hotel-updater" name="updater" data-cy="updater" type="text" />
              <ValidatedField
                label={translate('hotelApp.hotel.updatedAt')}
                id="hotel-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="hotel-user" name="user" data-cy="user" label={translate('hotelApp.hotel.user')} type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hotel" replace color="info">
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

export default HotelUpdate;
