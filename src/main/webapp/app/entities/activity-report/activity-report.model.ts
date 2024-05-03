import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IActivityReport {
  id: number;
  month?: dayjs.Dayjs | null;
  balance?: number | null;
  comments?: string | null;
  employee?: IEmployee | null;
}

export type NewActivityReport = Omit<IActivityReport, 'id'> & { id: null };
