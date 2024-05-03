import { IParameter, NewParameter } from './parameter.model';

export const sampleWithRequiredData: IParameter = {
  id: 23744,
};

export const sampleWithPartialData: IParameter = {
  id: 32564,
  label: 'au cas où',
};

export const sampleWithFullData: IParameter = {
  id: 27280,
  label: 'hi crac asseoir',
  value: 'aux environs de',
};

export const sampleWithNewData: NewParameter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
