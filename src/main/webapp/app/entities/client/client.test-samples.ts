import dayjs from 'dayjs/esm';

import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 31979,
};

export const sampleWithPartialData: IClient = {
  id: 20036,
  newsLetter: true,
};

export const sampleWithFullData: IClient = {
  id: 6560,
  firstName: 'Wilfredo',
  lastName: 'Daugherty',
  birthDay: dayjs('2024-10-23T22:56'),
  newsLetter: false,
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
