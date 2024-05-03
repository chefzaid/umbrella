import { IIdDocument, NewIdDocument } from './id-document.model';

export const sampleWithRequiredData: IIdDocument = {
  id: 200,
  idType: 'NATIONAL_ID',
  idNumber: 'ferme tsoin-tsoin',
};

export const sampleWithPartialData: IIdDocument = {
  id: 14126,
  idType: 'PASSPORT',
  idNumber: "à l'exception de d'abord",
};

export const sampleWithFullData: IIdDocument = {
  id: 2680,
  idType: 'PASSPORT',
  idNumber: 'désormais assister plic',
};

export const sampleWithNewData: NewIdDocument = {
  idType: 'PASSPORT',
  idNumber: 'pin-pon boum bon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
