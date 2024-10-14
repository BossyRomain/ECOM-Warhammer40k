import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'a3541d9b-3514-4cbe-a866-687892f68a17',
};

export const sampleWithPartialData: IAuthority = {
  name: 'eeff1ab4-9f86-42e5-ac24-8baced15417d',
};

export const sampleWithFullData: IAuthority = {
  name: 'b9d1e5d9-3f1b-4e43-a5f2-76a494191303',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
