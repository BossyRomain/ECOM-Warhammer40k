import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 28065,
  login: 'L',
};

export const sampleWithPartialData: IUser = {
  id: 7162,
  login: 'Wd_m@1',
};

export const sampleWithFullData: IUser = {
  id: 8658,
  login: 'F&2u{n@COpY',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
