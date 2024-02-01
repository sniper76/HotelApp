import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RoomType from './room-type';
import RoomTypeDetail from './room-type-detail';
import RoomTypeUpdate from './room-type-update';
import RoomTypeDeleteDialog from './room-type-delete-dialog';

const RoomTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RoomType />} />
    <Route path="new" element={<RoomTypeUpdate />} />
    <Route path=":id">
      <Route index element={<RoomTypeDetail />} />
      <Route path="edit" element={<RoomTypeUpdate />} />
      <Route path="delete" element={<RoomTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RoomTypeRoutes;
