import { IInvoiceLine, NewInvoiceLine } from './invoice-line.model';

export const sampleWithRequiredData: IInvoiceLine = {
  id: 21187,
  label: 'plouf',
  price: 32060.56,
};

export const sampleWithPartialData: IInvoiceLine = {
  id: 17736,
  label: 'beaucoup',
  description: 'membre à vie',
  price: 28916.89,
  quantity: 21377.55,
};

export const sampleWithFullData: IInvoiceLine = {
  id: 19271,
  label: 'errer meuh assez',
  description: 'loin',
  price: 20101.74,
  quantity: 14491.05,
};

export const sampleWithNewData: NewInvoiceLine = {
  label: 'aussitôt que',
  price: 9921.94,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
