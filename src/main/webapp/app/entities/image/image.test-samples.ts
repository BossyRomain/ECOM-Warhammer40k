import { IImage, NewImage } from './image.model';

export const sampleWithRequiredData: IImage = {
  id: 16345,
};

export const sampleWithPartialData: IImage = {
  id: 8186,
  description: 'silent warp',
};

export const sampleWithFullData: IImage = {
  id: 2685,
  url: 'https://frail-cross-contamination.biz/',
  description: 'essay parody',
};

export const sampleWithNewData: NewImage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
