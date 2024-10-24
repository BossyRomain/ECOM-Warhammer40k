import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 15572,
};

export const sampleWithPartialData: IProduct = {
  id: 17881,
  price: 28430.63,
};

export const sampleWithFullData: IProduct = {
  id: 12631,
  name: 'pigsty',
  stock: 23191,
  price: 2668.79,
  description: 'separately',
  type: 'Decor',
  releaseDate: dayjs('2024-10-23T22:05'),
};

export const sampleWithNewData: NewProduct = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
