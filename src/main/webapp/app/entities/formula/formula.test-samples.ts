import { IFormula, NewFormula } from './formula.model';

export const sampleWithRequiredData: IFormula = {
  id: 9924,
};

export const sampleWithPartialData: IFormula = {
  id: 15792,
  adminFeesPct: 17714.13,
};

export const sampleWithFullData: IFormula = {
  id: 24537,
  label: 'de façon à proche de commis de cuisine',
  adminFeesPct: 10558.96,
  additionalFeesPct: 24770.92,
};

export const sampleWithNewData: NewFormula = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
