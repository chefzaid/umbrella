import dayjs from 'dayjs/esm';

import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 14667,
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
};

export const sampleWithPartialData: IDocument = {
  id: 7816,
  uploadDate: dayjs('2024-05-03T19:29'),
  issuingDate: dayjs('2024-05-03T20:51'),
  expirationDate: dayjs('2024-05-03T02:01'),
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
};

export const sampleWithFullData: IDocument = {
  id: 28744,
  label: 'émouvoir',
  uploadDate: dayjs('2024-05-03T12:11'),
  issuingDate: dayjs('2024-05-03T05:01'),
  expirationDate: dayjs('2024-05-03T11:59'),
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
};

export const sampleWithNewData: NewDocument = {
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
