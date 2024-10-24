import { IAllegiance, NewAllegiance } from './allegiance.model';

export const sampleWithRequiredData: IAllegiance = {
  id: 28662,
};

export const sampleWithPartialData: IAllegiance = {
  id: 13956,
  faction: 'Space_Marine',
};

export const sampleWithFullData: IAllegiance = {
  id: 1374,
  group: 'Xenos',
  faction: 'Space_Marine',
};

export const sampleWithNewData: NewAllegiance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
