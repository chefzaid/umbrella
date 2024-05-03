import { IOperation, NewOperation } from './operation.model';

export const sampleWithRequiredData: IOperation = {
  id: 6048,
};

export const sampleWithPartialData: IOperation = {
  id: 13246,
  amount: 1054.69,
};

export const sampleWithFullData: IOperation = {
  id: 27508,
  description: 'sans doute outre',
  amount: 13113.95,
  operationType: 'DEBIT',
};

export const sampleWithNewData: NewOperation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
