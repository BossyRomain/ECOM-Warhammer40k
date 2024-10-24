import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IClient {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDay?: dayjs.Dayjs | null;
  newsLetter?: boolean | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
