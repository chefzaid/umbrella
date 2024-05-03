import { IParameterGroup, NewParameterGroup } from './parameter-group.model';

export const sampleWithRequiredData: IParameterGroup = {
  id: 1124,
};

export const sampleWithPartialData: IParameterGroup = {
  id: 19862,
  label: 'pis partout',
};

export const sampleWithFullData: IParameterGroup = {
  id: 293,
  label: 'parlementaire carrément',
  description: 'proche de vraiment pour que',
};

export const sampleWithNewData: NewParameterGroup = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
