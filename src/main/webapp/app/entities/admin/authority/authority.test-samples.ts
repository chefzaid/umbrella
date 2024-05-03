import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '20b9e7fe-eca5-4e35-ac2e-d844fd0e0113',
};

export const sampleWithPartialData: IAuthority = {
  name: '22b3e229-e4cf-4089-b420-c07fd409b2ba',
};

export const sampleWithFullData: IAuthority = {
  name: '79f7e267-558e-4504-bfed-daac08a29c9e',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
