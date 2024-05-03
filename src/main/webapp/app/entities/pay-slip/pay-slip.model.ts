import { IDocument } from 'app/entities/document/document.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IPaySlip {
  id: number;
  superGrossSalary?: number | null;
  grossSalary?: number | null;
  netSalary?: number | null;
  taxRate?: number | null;
  amountPaid?: number | null;
  totalExpenses?: number | null;
  document?: IDocument | null;
  employee?: IEmployee | null;
}

export type NewPaySlip = Omit<IPaySlip, 'id'> & { id: null };
