import { IExpenseType, NewExpenseType } from './expense-type.model';

export const sampleWithRequiredData: IExpenseType = {
  id: 4358,
};

export const sampleWithPartialData: IExpenseType = {
  id: 7886,
  label: 'boum au cas où',
  reimbursmentPct: 29804.22,
};

export const sampleWithFullData: IExpenseType = {
  id: 7760,
  label: 'membre titulaire',
  description: 'alors tolérer',
  reimbursmentPct: 30041.39,
};

export const sampleWithNewData: NewExpenseType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
