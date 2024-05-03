import dayjs from 'dayjs/esm';

import { IActivityReport, NewActivityReport } from './activity-report.model';

export const sampleWithRequiredData: IActivityReport = {
  id: 16397,
};

export const sampleWithPartialData: IActivityReport = {
  id: 24310,
  balance: 21626.21,
};

export const sampleWithFullData: IActivityReport = {
  id: 6914,
  month: dayjs('2024-05-03'),
  balance: 21012.56,
  comments: 'mieux nonobstant d’autant que',
};

export const sampleWithNewData: NewActivityReport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
