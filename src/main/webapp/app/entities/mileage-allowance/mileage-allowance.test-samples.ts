import { IMileageAllowance, NewMileageAllowance } from './mileage-allowance.model';

export const sampleWithRequiredData: IMileageAllowance = {
  id: 5146,
  mileage: 30486.44,
};

export const sampleWithPartialData: IMileageAllowance = {
  id: 11357,
  mileage: 4531.82,
};

export const sampleWithFullData: IMileageAllowance = {
  id: 13940,
  mileage: 1388.47,
  multiplier: 21342,
};

export const sampleWithNewData: NewMileageAllowance = {
  mileage: 15741.5,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
