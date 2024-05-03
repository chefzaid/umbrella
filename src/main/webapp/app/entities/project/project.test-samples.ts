import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 11557,
  dailyRate: 473,
};

export const sampleWithPartialData: IProject = {
  id: 179,
  state: 'STANDBY',
  dailyRate: 15185,
  startDate: dayjs('2024-05-02'),
  endDate: dayjs('2024-05-03'),
  remoteWorkPct: 19627.06,
};

export const sampleWithFullData: IProject = {
  id: 27152,
  type: 'NONBILLABLE',
  state: 'CURRENT',
  title: 'simple',
  description: 'afin que',
  dailyRate: 12398,
  startDate: dayjs('2024-05-03'),
  endDate: dayjs('2024-05-03'),
  remoteWorkPct: 14450.11,
};

export const sampleWithNewData: NewProject = {
  dailyRate: 4236,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
