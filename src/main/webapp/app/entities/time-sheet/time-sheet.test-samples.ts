import dayjs from 'dayjs/esm';

import { ITimeSheet, NewTimeSheet } from './time-sheet.model';

export const sampleWithRequiredData: ITimeSheet = {
  id: 4403,
  concernedMonth: 'ouf personnel professionnel',
};

export const sampleWithPartialData: ITimeSheet = {
  id: 29543,
  concernedMonth: 'dessus dissoudre fumer',
  creationDate: dayjs('2024-05-03'),
  submitDate: dayjs('2024-05-03T09:05'),
};

export const sampleWithFullData: ITimeSheet = {
  id: 27140,
  concernedMonth: 'gai personnel émerger',
  creationDate: dayjs('2024-05-03'),
  submitDate: dayjs('2024-05-03T02:34'),
  validationDate: dayjs('2024-05-03T01:55'),
};

export const sampleWithNewData: NewTimeSheet = {
  concernedMonth: 'guide',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
