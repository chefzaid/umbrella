import { IEnterprise, NewEnterprise } from './enterprise.model';

export const sampleWithRequiredData: IEnterprise = {
  id: 6965,
};

export const sampleWithPartialData: IEnterprise = {
  id: 25742,
  name: 'à la merci fonctionnaire',
  siren: 'ha ha rocher paf',
  salesTaxNumber: 'secours',
  bicSwift: 'chut',
  website: 'dès pendant',
  defaultInvoiceTerms: 'de par biathlète',
};

export const sampleWithFullData: IEnterprise = {
  id: 6401,
  name: 'repentir',
  companyStatus: 'tellement confondre',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  siret: 'tant pendant que',
  siren: 'gens novice',
  salesTaxNumber: 'avant que désormais',
  iban: 'FI3600930886060027',
  bicSwift: 'guider responsable',
  website: 'nonobstant ouah',
  defaultInvoiceTerms: 'patientèle infiniment',
};

export const sampleWithNewData: NewEnterprise = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
