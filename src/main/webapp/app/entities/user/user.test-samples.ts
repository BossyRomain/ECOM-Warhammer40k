import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 32454,
  login: '*60wSR@pHRqaN\\(ON1\\(Bl',
};

export const sampleWithPartialData: IUser = {
  id: 3570,
  login: '=@qrQRb\\Xq\\pwT10d\\RZaBjs\\F-O',
};

export const sampleWithFullData: IUser = {
  id: 11221,
  login: 'I@WpKZ\\gIe\\SE\\WtFxk\\`G2SAH',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
