import dayjs from 'dayjs/esm';

import { ICart, NewCart } from './cart.model';

export const sampleWithRequiredData: ICart = {
  id: 7814,
};

export const sampleWithPartialData: ICart = {
  id: 3467,
  buyingDate: dayjs('2024-10-23T19:50'),
};

export const sampleWithFullData: ICart = {
  id: 25885,
  buyingDate: dayjs('2024-10-24T05:27'),
  paid: false,
};

export const sampleWithNewData: NewCart = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
