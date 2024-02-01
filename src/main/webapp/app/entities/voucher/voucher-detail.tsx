import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './voucher.reducer';

export const VoucherDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const voucherEntity = useAppSelector(state => state.voucher.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="voucherDetailsHeading">
          <Translate contentKey="hotelApp.voucher.detail.title">Voucher</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.id}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="hotelApp.voucher.price">Price</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.price}</dd>
          <dt>
            <span id="salePrice">
              <Translate contentKey="hotelApp.voucher.salePrice">Sale Price</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.salePrice}</dd>
          <dt>
            <span id="creator">
              <Translate contentKey="hotelApp.voucher.creator">Creator</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.creator}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hotelApp.voucher.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.createdAt ? <TextFormat value={voucherEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updater">
              <Translate contentKey="hotelApp.voucher.updater">Updater</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.updater}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hotelApp.voucher.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{voucherEntity.updatedAt ? <TextFormat value={voucherEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/voucher" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/voucher/${voucherEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VoucherDetail;
