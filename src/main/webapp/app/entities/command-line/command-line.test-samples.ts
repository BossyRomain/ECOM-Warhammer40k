import { ICommandLine, NewCommandLine } from './command-line.model';

export const sampleWithRequiredData: ICommandLine = {
  id: 8569,
};

export const sampleWithPartialData: ICommandLine = {
  id: 2849,
  quanity: 6848,
};

export const sampleWithFullData: ICommandLine = {
  id: 24638,
  quanity: 5569,
};

export const sampleWithNewData: NewCommandLine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
