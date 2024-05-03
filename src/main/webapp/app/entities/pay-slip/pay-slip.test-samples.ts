import { IPaySlip, NewPaySlip } from './pay-slip.model';

export const sampleWithRequiredData: IPaySlip = {
  id: 26440,
};

export const sampleWithPartialData: IPaySlip = {
  id: 32152,
  grossSalary: 2926.4,
  netSalary: 24615.99,
  amountPaid: 20282.48,
};

export const sampleWithFullData: IPaySlip = {
  id: 23145,
  superGrossSalary: 32097.56,
  grossSalary: 22042.12,
  netSalary: 21266.86,
  taxRate: 15197.17,
  amountPaid: 26787.38,
  totalExpenses: 24695.81,
};

export const sampleWithNewData: NewPaySlip = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
