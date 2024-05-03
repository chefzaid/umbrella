import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 823,
  city: 'Saint-Étienne',
};

export const sampleWithPartialData: IAddress = {
  id: 26442,
  postalCode: 'croâ',
  city: 'Le Tampon',
};

export const sampleWithFullData: IAddress = {
  id: 9564,
  streetAddress: 'rose afin que',
  postalCode: 'secouriste rédiger secouriste',
  city: 'Rouen',
  country: 'Libye',
};

export const sampleWithNewData: NewAddress = {
  city: 'Caen',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
