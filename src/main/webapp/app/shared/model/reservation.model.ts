import dayjs from 'dayjs';
import { IRoom } from 'app/shared/model/room.model';
import { IVoucher } from 'app/shared/model/voucher.model';

export interface IReservation {
  id?: number;
  checkInDate?: dayjs.Dayjs | null;
  checkOutDate?: dayjs.Dayjs | null;
  creator?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updater?: number | null;
  updatedAt?: dayjs.Dayjs | null;
  room?: IRoom | null;
  voucher?: IVoucher | null;
}

export const defaultValue: Readonly<IReservation> = {};
