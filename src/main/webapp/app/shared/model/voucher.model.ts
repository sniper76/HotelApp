import dayjs from 'dayjs';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IVoucher {
  id?: number;
  price?: number;
  salePrice?: number;
  creator?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updater?: number | null;
  updatedAt?: dayjs.Dayjs | null;
  reservations?: IReservation[] | null;
}

export const defaultValue: Readonly<IVoucher> = {};
