import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Hotel from './hotel';
import RoomType from './room-type';
import Room from './room';
import Reservation from './reservation';
import Voucher from './voucher';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="hotel/*" element={<Hotel />} />
        <Route path="room-type/*" element={<RoomType />} />
        <Route path="room/*" element={<Room />} />
        <Route path="reservation/*" element={<Reservation />} />
        <Route path="voucher/*" element={<Voucher />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
