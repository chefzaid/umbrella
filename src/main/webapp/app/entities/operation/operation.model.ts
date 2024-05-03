import { IActivityReport } from 'app/entities/activity-report/activity-report.model';
import { OperationType } from 'app/entities/enumerations/operation-type.model';

export interface IOperation {
  id: number;
  description?: string | null;
  amount?: number | null;
  operationType?: keyof typeof OperationType | null;
  activityReport?: IActivityReport | null;
}

export type NewOperation = Omit<IOperation, 'id'> & { id: null };
