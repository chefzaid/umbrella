import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 21350,
  invoiceNumber: 'porte-parole croâ diététiste',
  issueDate: dayjs('2024-05-03T18:45'),
};

export const sampleWithPartialData: IInvoice = {
  id: 12029,
  invoiceNumber: "aussitôt que à l'égard de tchou tchouu",
  issueDate: dayjs('2024-05-03T02:15'),
  currency: 'agréable via',
  salesTaxPct: 17505.34,
  terms: 'membre de l’équipe vroum',
};

export const sampleWithFullData: IInvoice = {
  id: 25941,
  invoiceNumber: 'toc croire grandement',
  label: 'appuyer après marron',
  issueDate: dayjs('2024-05-03T20:33'),
  currency: 'de façon que chef de cuisine',
  salesTaxPct: 24985.38,
  terms: 'coac coac',
};

export const sampleWithNewData: NewInvoice = {
  invoiceNumber: 'en outre de dès tic-tac',
  issueDate: dayjs('2024-05-03T19:55'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
