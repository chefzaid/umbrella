import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 1117,
  employeeNumber: 24215,
  birthDate: dayjs('2024-05-03'),
  birthPlace: 'sous',
};

export const sampleWithPartialData: IEmployee = {
  id: 1056,
  employeeNumber: 22527,
  birthDate: dayjs('2024-05-03'),
  birthPlace: 'zzzz aux alentours de de peur de',
  gender: 'MALE',
  dependentChildrenNumber: 20020,
  socialSecurityNumber: 'envers',
  iban: 'LU84153985E3191F3243',
  bicSwift: 'interroger en guise de',
};

export const sampleWithFullData: IEmployee = {
  id: 29502,
  employeeNumber: 15627,
  birthDate: dayjs('2024-05-03'),
  birthPlace: 'équipe après',
  nationality: 'maintenant trop',
  gender: 'FEMALE',
  maritalStatus: 'MARRIED',
  dependentChildrenNumber: 13226,
  socialSecurityNumber: 'concernant',
  iban: 'SE1125200900343866084261',
  bicSwift: 'prout membre de l’équipe délectable',
};

export const sampleWithNewData: NewEmployee = {
  employeeNumber: 28119,
  birthDate: dayjs('2024-05-03'),
  birthPlace: 'pas mal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
