import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { HotelType } from 'app/shared/model/enumerations/hotel-type.model';

export interface IHotel {
  id?: number;
  name?: string;
  type?: keyof typeof HotelType;
  location?: string | null;
  description?: string | null;
  creator?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updater?: number | null;
  updatedAt?: dayjs.Dayjs | null;
  user?: IUser;
}

export const defaultValue: Readonly<IHotel> = {};
