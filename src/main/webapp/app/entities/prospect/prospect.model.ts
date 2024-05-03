import dayjs from 'dayjs/esm';
import { IFormula } from 'app/entities/formula/formula.model';

export interface IProspect {
  id: number;
  dailyRate?: number | null;
  monthlyExpenses?: number | null;
  taxRate?: number | null;
  expectedStartDate?: dayjs.Dayjs | null;
  expectedClient?: string | null;
  notes?: string | null;
  formula?: IFormula | null;
}

export type NewProspect = Omit<IProspect, 'id'> & { id: null };
