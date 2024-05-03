import { ITimeSheetLine, NewTimeSheetLine } from './time-sheet-line.model';

export const sampleWithRequiredData: ITimeSheetLine = {
  id: 14033,
  monthlyDays: 13355.3,
};

export const sampleWithPartialData: ITimeSheetLine = {
  id: 26270,
  monthlyDays: 21004.54,
  extraHours: 10205.12,
};

export const sampleWithFullData: ITimeSheetLine = {
  id: 32355,
  monthlyDays: 22867.95,
  extraHours: 24425.27,
  comments: 'puisque chut',
};

export const sampleWithNewData: NewTimeSheetLine = {
  monthlyDays: 269.79,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
