import dayjs from 'dayjs';
import { IRoom } from 'app/shared/model/room.model';

export interface IRoomType {
  id?: number;
  name?: string;
  description?: string | null;
  creator?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updater?: number | null;
  updatedAt?: dayjs.Dayjs | null;
  room?: IRoom | null;
}

export const defaultValue: Readonly<IRoomType> = {};
