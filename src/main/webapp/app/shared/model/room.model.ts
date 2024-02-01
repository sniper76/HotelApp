import dayjs from 'dayjs';
import { IRoomType } from 'app/shared/model/room-type.model';
import { IHotel } from 'app/shared/model/hotel.model';

export interface IRoom {
  id?: number;
  no?: string;
  price?: number | null;
  promoPrice?: number | null;
  creator?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updater?: number | null;
  updatedAt?: dayjs.Dayjs | null;
  roomType?: IRoomType | null;
  hotel?: IHotel | null;
}

export const defaultValue: Readonly<IRoom> = {};
