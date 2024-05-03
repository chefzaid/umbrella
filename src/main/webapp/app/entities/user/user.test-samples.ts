import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 16041,
  login: 'OxS',
};

export const sampleWithPartialData: IUser = {
  id: 23659,
  login: '_R',
};

export const sampleWithFullData: IUser = {
  id: 567,
  login: '.@py\\7y\\cX5i1\\Cc5',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
