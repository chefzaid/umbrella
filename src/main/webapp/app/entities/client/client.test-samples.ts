import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 9201,
  name: 'au-delà',
};

export const sampleWithPartialData: IClient = {
  id: 25250,
  name: 'retomber coupable prout',
  companyStatus: 'bof clientèle',
};

export const sampleWithFullData: IClient = {
  id: 10170,
  name: 'tic-tac dès',
  companyStatus: 'foule complètement',
};

export const sampleWithNewData: NewClient = {
  name: 'tant moyennant zzzz',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
