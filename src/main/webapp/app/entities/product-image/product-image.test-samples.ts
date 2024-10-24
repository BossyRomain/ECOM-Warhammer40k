import { IProductImage, NewProductImage } from './product-image.model';

export const sampleWithRequiredData: IProductImage = {
  id: 4410,
};

export const sampleWithPartialData: IProductImage = {
  id: 24883,
  description: 'phooey',
};

export const sampleWithFullData: IProductImage = {
  id: 24331,
  url: 'https://potable-reboot.org',
  description: 'ew cop-out',
};

export const sampleWithNewData: NewProductImage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
