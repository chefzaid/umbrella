import dayjs from 'dayjs/esm';

import { IExpenseNote, NewExpenseNote } from './expense-note.model';

export const sampleWithRequiredData: IExpenseNote = {
  id: 23865,
};

export const sampleWithPartialData: IExpenseNote = {
  id: 26941,
  creationDate: dayjs('2024-05-03'),
  submitDate: dayjs('2024-05-03T08:52'),
};

export const sampleWithFullData: IExpenseNote = {
  id: 5153,
  label: 'fidèle euh',
  concernedMonth: 'cadre',
  creationDate: dayjs('2024-05-03'),
  submitDate: dayjs('2024-05-03T11:36'),
  validationDate: dayjs('2024-05-03T19:56'),
};

export const sampleWithNewData: NewExpenseNote = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
