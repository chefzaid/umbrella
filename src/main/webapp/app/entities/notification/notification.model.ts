import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface INotification {
  id: number;
  title?: string | null;
  description?: string | null;
  creationDate?: dayjs.Dayjs | null;
  readDate?: dayjs.Dayjs | null;
  hide?: boolean | null;
  author?: IEmployee | null;
  receiver?: IEmployee | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
