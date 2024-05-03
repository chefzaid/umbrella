import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { ProjectType } from 'app/entities/enumerations/project-type.model';
import { ProjectState } from 'app/entities/enumerations/project-state.model';

export interface IProject {
  id: number;
  type?: keyof typeof ProjectType | null;
  state?: keyof typeof ProjectState | null;
  title?: string | null;
  description?: string | null;
  dailyRate?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  remoteWorkPct?: number | null;
  client?: IClient | null;
  employee?: IEmployee | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
