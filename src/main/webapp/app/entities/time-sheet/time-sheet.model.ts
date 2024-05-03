import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';
import { IDocument } from 'app/entities/document/document.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ITimeSheet {
  id: number;
  concernedMonth?: string | null;
  creationDate?: dayjs.Dayjs | null;
  submitDate?: dayjs.Dayjs | null;
  validationDate?: dayjs.Dayjs | null;
  project?: IProject | null;
  document?: IDocument | null;
  employee?: IEmployee | null;
}

export type NewTimeSheet = Omit<ITimeSheet, 'id'> & { id: null };
