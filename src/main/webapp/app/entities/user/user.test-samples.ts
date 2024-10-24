import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 18123,
  login: 'C5@q\\Yvz\\?t6',
};

export const sampleWithPartialData: IUser = {
  id: 8572,
  login: 'n~@eKwF9\\MKVcKJ2\\$6h\\=x8cT\\9xR1Sw',
};

export const sampleWithFullData: IUser = {
  id: 6473,
  login: 'igN',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
