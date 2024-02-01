import './home.scss';

import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import { type FieldError, useForm } from 'react-hook-form';
import { login } from 'app/shared/reducers/authentication';

export const Home = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);

  const loginError = useAppSelector(state => state.authentication.loginError);

  const loginProcess = ({ username, password, rememberMe }) => {
    handleLogin(username, password, rememberMe);
  };
  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));
  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const handleLoginSubmit = e => {
    handleSubmit(loginProcess)(e);
  };
  return (
    <Row>
      {account?.login ? (
        <Col md="12">
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        </Col>
      ) : (
        <Col md="12" className={'container_login'}>
          <div style={{ width: '30%' }}></div>
          <div>
            <Form onSubmit={handleLoginSubmit}>
              <Row>
                <Col md="12">
                  {loginError ? (
                    <Alert color="danger" data-cy="loginError">
                      <Translate contentKey="login.messages.error.authentication">
                        <strong>Failed to sign in!</strong> Please check your credentials and try again.
                      </Translate>
                    </Alert>
                  ) : null}
                </Col>
                <Col md="12">
                  <ValidatedField
                    name="username"
                    label={translate('global.form.username.label')}
                    placeholder={translate('global.form.username.placeholder')}
                    required
                    autoFocus
                    data-cy="username"
                    validate={{ required: 'Username cannot be empty!' }}
                    register={register}
                    error={errors.username as FieldError}
                    isTouched={touchedFields.username}
                  />
                  <ValidatedField
                    name="password"
                    type="password"
                    label={translate('login.form.password')}
                    placeholder={translate('login.form.password.placeholder')}
                    required
                    data-cy="password"
                    validate={{ required: 'Password cannot be empty!' }}
                    register={register}
                    error={errors.password as FieldError}
                    isTouched={touchedFields.password}
                  />
                  <ValidatedField
                    name="rememberMe"
                    type="checkbox"
                    check
                    label={translate('login.form.rememberme')}
                    value={true}
                    register={register}
                  />
                </Col>
              </Row>
              <div className="mt-1">&nbsp;</div>
              <Alert color="warning">
                <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
                  <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
                </Link>
              </Alert>
              <Alert color="warning">
                <span>
                  <Translate contentKey="global.messages.info.register.noaccount">You don&apos;t have an account yet?</Translate>
                </span>
                <Link to="/account/register">
                  <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                </Link>
              </Alert>
              <Button color="primary" type="submit" data-cy="submit">
                <Translate contentKey="login.form.button">Sign in</Translate>
              </Button>
            </Form>
          </div>
          <div style={{ width: '30%' }}></div>
        </Col>
      )}
    </Row>
  );
};

export default Home;
