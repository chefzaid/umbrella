import dayjs from 'dayjs/esm';
import { IParameter } from 'app/entities/parameter/parameter.model';
import { IProject } from 'app/entities/project/project.model';
import { IExpenseNote } from 'app/entities/expense-note/expense-note.model';

export interface IExpense {
  id: number;
  label?: string | null;
  description?: string | null;
  amount?: number | null;
  currency?: string | null;
  tax?: number | null;
  expenseDate?: dayjs.Dayjs | null;
  rebillableToClient?: boolean | null;
  comment?: string | null;
  submitDate?: dayjs.Dayjs | null;
  validationDate?: dayjs.Dayjs | null;
  paymentMethod?: IParameter | null;
  project?: IProject | null;
  expenseNote?: IExpenseNote | null;
}

export type NewExpense = Omit<IExpense, 'id'> & { id: null };
