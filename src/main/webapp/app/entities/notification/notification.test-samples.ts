import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 19871,
};

export const sampleWithPartialData: INotification = {
  id: 10753,
  description: 'ouille ensuite',
  hide: true,
};

export const sampleWithFullData: INotification = {
  id: 6611,
  title: 'tôt même si diplomate',
  description: 'ferme arrière',
  creationDate: dayjs('2024-05-03T16:55'),
  readDate: dayjs('2024-05-03T15:19'),
  hide: true,
};

export const sampleWithNewData: NewNotification = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
