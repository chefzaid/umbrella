import { ITimeSheet } from 'app/entities/time-sheet/time-sheet.model';

export interface ITimeSheetLine {
  id: number;
  monthlyDays?: number | null;
  extraHours?: number | null;
  comments?: string | null;
  timeSheet?: ITimeSheet | null;
}

export type NewTimeSheetLine = Omit<ITimeSheetLine, 'id'> & { id: null };
