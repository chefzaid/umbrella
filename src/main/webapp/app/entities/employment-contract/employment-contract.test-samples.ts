import dayjs from 'dayjs/esm';

import { IEmploymentContract, NewEmploymentContract } from './employment-contract.model';

export const sampleWithRequiredData: IEmploymentContract = {
  id: 17783,
};

export const sampleWithPartialData: IEmploymentContract = {
  id: 11155,
  jobTitle: 'Consultant du web principal',
  hireDate: dayjs('2024-05-03'),
  yearlyWorkDays: 10316,
  signedByEmployer: false,
};

export const sampleWithFullData: IEmploymentContract = {
  id: 31274,
  type: 'TEMPORARY',
  jobTitle: 'Agent de la qualité principal',
  hireDate: dayjs('2024-05-03'),
  salary: 11954.23,
  yearlyWorkDays: 22502,
  signedByEmployer: false,
  signedByEmployee: false,
};

export const sampleWithNewData: NewEmploymentContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
