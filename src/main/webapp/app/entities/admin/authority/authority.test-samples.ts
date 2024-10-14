import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '33e71bf6-620e-450f-8b76-9a885d55248e',
};

export const sampleWithPartialData: IAuthority = {
  name: '3aa8f580-e0e0-4a6e-8e25-0f824ef41d43',
};

export const sampleWithFullData: IAuthority = {
  name: '78bbbebe-a470-4e86-aa4c-19c7407b6117',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
