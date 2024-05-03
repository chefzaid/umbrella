import dayjs from 'dayjs/esm';

import { IProspect, NewProspect } from './prospect.model';

export const sampleWithRequiredData: IProspect = {
  id: 16852,
};

export const sampleWithPartialData: IProspect = {
  id: 16644,
  dailyRate: 18223,
  notes: "à l'exception de glouglou souple",
};

export const sampleWithFullData: IProspect = {
  id: 17613,
  dailyRate: 18693,
  monthlyExpenses: 6380,
  taxRate: 29616.71,
  expectedStartDate: dayjs('2024-05-03'),
  expectedClient: 'toutefois',
  notes: 'pour triathlète miam',
};

export const sampleWithNewData: NewProspect = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
