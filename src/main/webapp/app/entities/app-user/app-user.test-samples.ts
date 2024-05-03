import dayjs from 'dayjs/esm';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 24510,
  username: "désagréable d'entre perplexe",
  password: 'smack',
};

export const sampleWithPartialData: IAppUser = {
  id: 4712,
  username: 'quand',
  password: 'tantôt sans doute',
  isAdmin: false,
  creationDate: dayjs('2024-05-03T11:51'),
};

export const sampleWithFullData: IAppUser = {
  id: 26950,
  username: 'bien que population du Québec adepte',
  password: 'solitaire',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  isAdmin: true,
  creationDate: dayjs('2024-05-03T03:49'),
};

export const sampleWithNewData: NewAppUser = {
  username: 'sur rédaction',
  password: 'grrr près',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
