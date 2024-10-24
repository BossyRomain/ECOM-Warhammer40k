import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';

export interface ICart {
  id: number;
  buyingDate?: dayjs.Dayjs | null;
  paid?: boolean | null;
  client?: IClient | null;
}

export type NewCart = Omit<ICart, 'id'> & { id: null };
