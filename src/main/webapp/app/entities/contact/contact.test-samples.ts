import { IContact, NewContact } from './contact.model';

export const sampleWithRequiredData: IContact = {
  id: 29262,
  firstName: 'Amiel',
  lastName: 'Charles',
  email: 'Landry9@yahoo.fr',
  phoneNumber: 'dès que fouiller',
};

export const sampleWithPartialData: IContact = {
  id: 2662,
  firstName: 'Morgan',
  lastName: 'Morin',
  email: 'Fidele50@gmail.com',
  phoneNumber: 'par suite de population du Québec',
  jobTitle: 'Coordinateur de recherche futur',
};

export const sampleWithFullData: IContact = {
  id: 6632,
  firstName: 'Clélie',
  lastName: 'Rodriguez',
  email: 'Bohemond_Charpentier9@yahoo.fr',
  phoneNumber: 'guide minuscule',
  jobTitle: 'Analyste des marchés interne',
};

export const sampleWithNewData: NewContact = {
  firstName: 'Clémentine',
  lastName: 'Rodriguez',
  email: 'Helier_Pons67@gmail.com',
  phoneNumber: 'en dehors de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
