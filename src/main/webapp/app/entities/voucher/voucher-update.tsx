import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVoucher } from 'app/shared/model/voucher.model';
import { getEntity, updateEntity, createEntity, reset } from './voucher.reducer';

export const VoucherUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const voucherEntity = useAppSelector(state => state.voucher.entity);
  const loading = useAppSelector(state => state.voucher.loading);
  const updating = useAppSelector(state => state.voucher.updating);
  const updateSuccess = useAppSelector(state => state.voucher.updateSuccess);

  const handleClose = () => {
    navigate('/voucher' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.salePrice !== undefined && typeof values.salePrice !== 'number') {
      values.salePrice = Number(values.salePrice);
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
      ...voucherEntity,
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
          ...voucherEntity,
          createdAt: convertDateTimeFromServer(voucherEntity.createdAt),
          updatedAt: convertDateTimeFromServer(voucherEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelApp.voucher.home.createOrEditLabel" data-cy="VoucherCreateUpdateHeading">
            <Translate contentKey="hotelApp.voucher.home.createOrEditLabel">Create or edit a Voucher</Translate>
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
                  id="voucher-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hotelApp.voucher.price')}
                id="voucher-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('hotelApp.voucher.salePrice')}
                id="voucher-salePrice"
                name="salePrice"
                data-cy="salePrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('hotelApp.voucher.creator')}
                id="voucher-creator"
                name="creator"
                data-cy="creator"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.voucher.createdAt')}
                id="voucher-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hotelApp.voucher.updater')}
                id="voucher-updater"
                name="updater"
                data-cy="updater"
                type="text"
              />
              <ValidatedField
                label={translate('hotelApp.voucher.updatedAt')}
                id="voucher-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/voucher" replace color="info">
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

export default VoucherUpdate;
