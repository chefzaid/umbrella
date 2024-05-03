import dayjs from 'dayjs/esm';
import { IMileageAllowance } from 'app/entities/mileage-allowance/mileage-allowance.model';
import { IDocument } from 'app/entities/document/document.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IExpenseNote {
  id: number;
  label?: string | null;
  concernedMonth?: string | null;
  creationDate?: dayjs.Dayjs | null;
  submitDate?: dayjs.Dayjs | null;
  validationDate?: dayjs.Dayjs | null;
  mileageAllowance?: IMileageAllowance | null;
  document?: IDocument | null;
  employee?: IEmployee | null;
}

export type NewExpenseNote = Omit<IExpenseNote, 'id'> & { id: null };
