import dayjs from 'dayjs/esm';

import { IExpense, NewExpense } from './expense.model';

export const sampleWithRequiredData: IExpense = {
  id: 31020,
  label: "tant que à l'entour de",
  amount: 23709,
};

export const sampleWithPartialData: IExpense = {
  id: 8865,
  label: 'distraire',
  description: 'patientèle cot cot',
  amount: 7692.69,
  currency: 'outre groin groin',
  tax: 5978.64,
  rebillableToClient: true,
  comment: 'tant blablabla',
  submitDate: dayjs('2024-05-03T11:39'),
  validationDate: dayjs('2024-05-03T10:58'),
};

export const sampleWithFullData: IExpense = {
  id: 21577,
  label: 'porte-parole vlan',
  description: 'altruiste de la part de biathlète',
  amount: 6510.6,
  currency: 'euh afin que séculaire',
  tax: 32722.19,
  expenseDate: dayjs('2024-05-03'),
  rebillableToClient: true,
  comment: 'loin de conseil d’administration',
  submitDate: dayjs('2024-05-03T04:32'),
  validationDate: dayjs('2024-05-03T00:29'),
};

export const sampleWithNewData: NewExpense = {
  label: 'puiser grâce à',
  amount: 22315.21,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
