import dayjs from 'dayjs/esm';

import { IServiceContract, NewServiceContract } from './service-contract.model';

export const sampleWithRequiredData: IServiceContract = {
  id: 22791,
};

export const sampleWithPartialData: IServiceContract = {
  id: 5305,
  startDate: dayjs('2024-05-02'),
  extensionTerms: 'tout à fait',
};

export const sampleWithFullData: IServiceContract = {
  id: 13376,
  serviceLabel: 'au-devant au lieu de',
  dailyRate: 16889.92,
  startDate: dayjs('2024-05-03'),
  endDate: dayjs('2024-05-03'),
  extensionTerms: 'juriste trop',
  signedBySupplier: false,
  signedByClient: true,
};

export const sampleWithNewData: NewServiceContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
